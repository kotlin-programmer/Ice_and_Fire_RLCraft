package com.github.alexthe666.iceandfire.api;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.github.alexthe666.iceandfire.integration.LycanitesCompat;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.EntityMultipartPart;
import com.github.alexthe666.iceandfire.message.MessageChainLightningFX;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.*;

public class ChainLightningUtils {

    public static void createChainLightningFromTarget(World world, EntityLivingBase target, EntityLivingBase attacker) {
        float damage = IceAndFireConfig.MISC_SETTINGS.chainLightningDamage;
        int hops = IceAndFireConfig.MISC_SETTINGS.chainLightningHops;

        createChainLightningFromTarget(world, target, attacker, damage, hops);
    }

    public static void createChainLightningFromTarget(World world, EntityLivingBase target, EntityLivingBase attacker, float damage, int hops) {
        int range = IceAndFireConfig.MISC_SETTINGS.chainLightningRange;

        createChainLightningFromTarget(world, target, attacker, damage, hops, range);
    }

    public static void createChainLightningFromTarget(World world, EntityLivingBase target, EntityLivingBase attacker, float damage, int hops, int range) {
        boolean isParalysisEnabled = IceAndFireConfig.MISC_SETTINGS.chainLightningParalysis;
        int[] paralysisTicks = IceAndFireConfig.MISC_SETTINGS.chainLightningParalysisTicks;
        int[] paralysisChance = IceAndFireConfig.MISC_SETTINGS.chainLightningParalysisChance;

        int hop = 0;

        float damageReductionPerHop = damage / (hops + 1);

        attackEntityWithLightningDamage(attacker, target, damage);
        if (isParalysisEnabled) {
            applyParalysis(world, target, hop, paralysisChance, paralysisTicks);
        }

        target.playSound(ModSounds.LIGHTNING_STRIKE, 1, 1);

        LightningSource lightningSource = new LightningSource(target);

        List<EntityLivingBase> entityLiving = new ArrayList<>();
        for (Entity ent : world.getEntitiesWithinAABBExcludingEntity(lightningSource.get(), lightningSource.getBoundingBox(range))) {
            if (ent instanceof EntityMultipartPart) {
                ent = ((EntityMultipartPart)ent).getParent();
            }
            if (ent instanceof EntityLivingBase
                    && !entityLiving.contains(ent)
                    && lightningSource.canChainTo((EntityLivingBase) ent, attacker)) {
                entityLiving.add((EntityLivingBase)ent);
            }
        }
        if(entityLiving.isEmpty()) return;

        entityLiving.sort(getFindByNearestComparator(lightningSource));

        LinkedList<Integer> alreadyTargetedEntities = new LinkedList<>();
        alreadyTargetedEntities.add(target.getEntityId());

        for (EntityLivingBase nextTarget : entityLiving) {
            hop++;

            if (hop > hops) break;
            if (alreadyTargetedEntities.contains(nextTarget.getEntityId())) continue;

            damage -= damageReductionPerHop;

            attackEntityWithLightningDamage(attacker, nextTarget, damage);
            if (isParalysisEnabled) {
                applyParalysis(world, target, hop, paralysisChance, paralysisTicks);
            }

            alreadyTargetedEntities.add(nextTarget.getEntityId());
            lightningSource.set(nextTarget);
        }

        if (!alreadyTargetedEntities.isEmpty()) {
            alreadyTargetedEntities.addFirst(target.getEntityId());

            IceAndFire.NETWORK_WRAPPER.sendToAllAround(
                    new MessageChainLightningFX(alreadyTargetedEntities),
                    new NetworkRegistry.TargetPoint(
                            target.dimension,
                            target.posX,
                            target.posY+ target.height / 2,
                            target.posZ,
                            60
                    )
            );
        }
    }

    private static void attackEntityWithLightningDamage(EntityLivingBase attacker, EntityLivingBase target, float damage) {
        if (IceAndFireConfig.MISC_SETTINGS.chainLightningTransformsMobs) {
            // Pig => Zombie Pigman, Villager => Witch
            if (target instanceof EntityPig || target instanceof EntityVillager) {
                strikeWithLightningBolt(target);
                return;
            }
        }

        // Crab => Larger Crab
        if (EventLiving.isQuarkCrab(target)) {
            strikeWithLightningBolt(target);
            return;
        }

        target.attackEntityFrom(new EntityDamageSourceIndirect("lightningBolt", attacker, attacker), damage);

        // Creeper => Charged Creeper
        if (target instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) target;
            if (!creeper.getPowered()) {
                NBTTagCompound compound = new NBTTagCompound();
                creeper.writeEntityToNBT(compound);
                compound.setBoolean("powered", true);
                creeper.readEntityFromNBT(compound);
            }
        }
    }

    private static void strikeWithLightningBolt(Entity entity) {
        EntityLightningBolt lightningBolt = new EntityLightningBolt(entity.world, entity.posX, entity.posY, entity.posZ, true);
        entity.onStruckByLightning(lightningBolt);
    }

    private static boolean shouldApplyParalysis(World world, int hop, int[] paralysisChance) {
        if (paralysisChance.length > hop) {
            int chance = paralysisChance[hop];
            if (chance == 100) {
                return true;
            }
            if (chance == 0) {
                return false;
            }
            return world.rand.nextInt(100) < chance;
        }
        return false;
    }

    private static int getParalysisTicks(int hop, int[] paralysisTicks) {
        if (paralysisTicks.length > hop) {
            return paralysisTicks[hop];
        }
        return 0;
    }

    private static void applyParalysis(World world, EntityLivingBase target, int hop, int[] paralysisChance, int[] paralysisTicks) {
        if (!shouldApplyParalysis(world, hop, paralysisChance)) {
            return;
        }
        int ticks = getParalysisTicks(hop, paralysisTicks);
        if (ticks <= 0) {
            return;
        }
        LycanitesCompat.applyParalysis(target, ticks);
    }

    private static Comparator<Entity> getFindByNearestComparator(LightningSource lightningSource) {
        return Comparator.comparingDouble(e -> e.getDistanceSq(lightningSource.get()));
    }

    private static class LightningSource {

        EntityLivingBase source;
        public LightningSource(EntityLivingBase source) {
            this.source = source;
        }

        public void set(EntityLivingBase source) {
            this.source = source;
        }

        public EntityLivingBase get() {
            return source;
        }

        private boolean canChainTo(EntityLivingBase target, EntityLivingBase attacker) {
            if (target instanceof EntityPlayer) {
                return false;
            }
            if (!DragonUtils.isAlive(target)) {
                return false;
            }
            if (target instanceof IEntityOwnable && ((IEntityOwnable) target).getOwner() != null) {
                if (target instanceof EntityLiving) {
                    EntityLivingBase attackTarget = ((EntityLiving) target).getAttackTarget();
                    EntityLivingBase revengeTarget = target.getRevengeTarget();
                    if (!attacker.equals(attackTarget) && !attacker.equals(revengeTarget)) {
                        return false;
                    }
                } else {
                    EntityLivingBase revengeTarget = target.getRevengeTarget();
                    if (!attacker.equals(revengeTarget)) {
                        return false;
                    }
                }
            }
            return source.canEntityBeSeen(target);
        }

        private AxisAlignedBB getBoundingBox(int range) {
            return new AxisAlignedBB(
                    source.posX - range,
                    source.posY - range,
                    source.posZ - range,
                    source.posX + range,
                    source.posY + range,
                    source.posZ + range
            );
        }
    }
}
