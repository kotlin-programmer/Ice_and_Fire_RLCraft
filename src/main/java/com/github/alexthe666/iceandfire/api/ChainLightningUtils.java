package com.github.alexthe666.iceandfire.api;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;
import com.github.alexthe666.iceandfire.integration.LycanitesCompat;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.util.EntityMultipartPart;
import com.github.alexthe666.iceandfire.message.MessageChainLightningFX;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.*;

public class ChainLightningUtils {

    public static void createChainLightningFromTarget(World world, EntityLivingBase target, EntityLivingBase attacker) {
        float[] damage = IceAndFireConfig.MISC_SETTINGS.chainLightningDamagePerHop;
        int range = IceAndFireConfig.MISC_SETTINGS.chainLightningRange;
        boolean isParalysisEnabled = IceAndFireConfig.MISC_SETTINGS.chainLightningParalysis;

        createChainLightningFromTarget(world, target, attacker, damage, range, isParalysisEnabled);
    }

    public static void createChainLightningFromTarget(World world, EntityLivingBase target, EntityLivingBase attacker, float[] damage, int range, boolean isParalysisEnabled) {
        int[] paralysisTicks = IceAndFireConfig.MISC_SETTINGS.chainLightningParalysisTicksPerHop;

        createChainLightningFromTarget(world, target, attacker, damage, range, isParalysisEnabled, paralysisTicks);
    }

    public static void createChainLightningFromTarget(
            World world,
            EntityLivingBase target,
            EntityLivingBase attacker,
            float[] damage,
            int range,
            boolean isParalysisEnabled,
            int[] paralysisTicks
    ) {
        if (!canHurt(target, attacker)) {
            return;
        }

        if (attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;
            if (player.getCooldownTracker().hasCooldown(IafItemRegistry.dragonbone_sword_lightning)) {
                return;
            }
            player.getCooldownTracker().setCooldown(IafItemRegistry.dragonbone_sword_lightning, IceAndFireConfig.MISC_SETTINGS.chainLightningCooldown);
        }

        int hop = 0;

        attackEntityWithLightningDamage(attacker, target, hop, damage);

        if (isParalysisEnabled) {
            applyParalysis(target, hop, paralysisTicks);
        }

        target.playSound(IafSoundRegistry.LIGHTNING_STRIKE, 1, 1);

        LightningSource lightningSource = new LightningSource(target);

        List<EntityLivingBase> entityLiving = new ArrayList<>();
        for (Entity ent : world.getEntitiesWithinAABBExcludingEntity(lightningSource.get(), lightningSource.getBoundingBox(range))) {
            if (ent instanceof EntityMultipartPart) {
                ent = ((EntityMultipartPart) ent).getParent();
            }
            if (ent instanceof EntityLivingBase
                    && !entityLiving.contains(ent)
                    && lightningSource.canChainTo((EntityLivingBase) ent, attacker)) {
                entityLiving.add((EntityLivingBase) ent);
            }
        }
        if(entityLiving.isEmpty()) return;

        entityLiving.sort(getFindByNearestComparator(lightningSource));

        LinkedList<Integer> alreadyTargetedEntities = new LinkedList<>();
        alreadyTargetedEntities.add(target.getEntityId());

        for (EntityLivingBase nextTarget : entityLiving) {
            hop++;

            if (hop >= damage.length) break;
            if (alreadyTargetedEntities.contains(nextTarget.getEntityId())) continue;

            attackEntityWithLightningDamage(attacker, nextTarget, hop, damage);
            if (isParalysisEnabled) {
                applyParalysis(nextTarget, hop, paralysisTicks);
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

    private static boolean canHurt(EntityLivingBase target, EntityLivingBase attacker) {
        if (target instanceof IDeadMob && ((IDeadMob) target).isMobDead()) {
            return false;
        }
        if (!target.attackable()) {
            return false;
        }
        if (target instanceof EntityLiving && ((EntityLiving)target).isAIDisabled()) {
            return false;
        }
        if (CompatLoadUtil.isLycanitesMobsLoaded()) {
            if (!LycanitesCompat.canHurt(target, attacker)) {
                return false;
            }
        }
        return target instanceof EntityLiving || target instanceof EntityPlayer;
    }

    private static void attackEntityWithLightningDamage(EntityLivingBase attacker, EntityLivingBase target, int hop, float[] damage) {
        // Crab => Larger Crab
        if (EventLiving.isQuarkCrab(target)) {
            strikeWithLightningBolt(target);
            return;
        }

        DamageSource damageSource = new EntityDamageSourceIndirect("lightningBolt", attacker, attacker);
        if (IceAndFireConfig.MISC_SETTINGS.chainLightningBypassesArmor) {
            damageSource = damageSource.setDamageBypassesArmor();
        }

        target.attackEntityFrom(damageSource, damage[hop]);

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

    private static int getParalysisTicks(int hop, int[] paralysisTicks) {
        if (paralysisTicks.length > hop) {
            return paralysisTicks[hop];
        }
        return 0;
    }

    private static void applyParalysis(EntityLivingBase target, int hop, int[] paralysisTicks) {
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
            if (!canHurt(target, attacker)) {
                return false;
            }
            if (isBlacklistedFromLightningChaining(target)) {
                return false;
            }
            if (target instanceof IEntityOwnable && ((IEntityOwnable) target).getOwner() instanceof EntityPlayer) {
                if (attacker == null) {
                    return false;
                } else if (target instanceof EntityLiving) {
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

    private static boolean isBlacklistedFromLightningChaining(Entity entity) {
        ResourceLocation id = EntityList.getKey(entity);
        return id != null && IceAndFireConfig.getChainLightningEntityBlacklist().contains(id);
    }
}
