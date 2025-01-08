package com.github.alexthe666.iceandfire.capability.entityeffect;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.util.ParticleHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityEffectHandler {

    public static void tickUpdate(EntityLivingBase entity, World world, IEntityEffectCapability capability) {
        if(capability.isCharmed()) {
            EntitySiren siren = capability.getSiren(world);
            if(siren != null) {
                capability.tickTime();
                if(capability.getTime() > 0 && !siren.isDead && !entity.isDead && siren.isActuallySinging() && EntityEffectCapability.EntityEffectEnum.CHARMED.canBeApplied(entity) && entity.getDistanceSq(siren) < ((EntitySiren.SEARCH_RANGE*2)*EntitySiren.SEARCH_RANGE*2)) {
                    if(world.rand.nextInt(10) == 0) {
                        for(int i = 0; i < 3; i++) {
                            ParticleHelper.spawnParticle(entity.world,
                                    EnumParticleTypes.HEART,
                                    entity.posX + ((world.rand.nextDouble() - 0.5D) * 3),
                                    entity.posY + ((world.rand.nextDouble() - 0.5D) * 3),
                                    entity.posZ + ((world.rand.nextDouble() - 0.5D) * 3),
                                    0, 0, 0);
                        }
                    }

                    double d0 = siren.posX - entity.posX;
                    double d2 = siren.posZ - entity.posZ;
                    double d1 = siren.posY - 1 - entity.posY;
                    double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                    float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                    float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
                    entity.rotationPitch = updateRotation(entity.rotationPitch, f1, 30F);
                    entity.rotationYaw = updateRotation(entity.rotationYaw, f, 30F);
                    if(entity.collidedHorizontally){
                        if(entity instanceof EntityLiving) ((EntityLiving)entity).getJumpHelper().setJumping();
                        else if(entity.onGround) entity.motionY = 0.42F;
                    }
                    entity.motionX += (Math.signum(siren.posX - entity.posX) * 0.5D - entity.motionX) * 0.100000000372529;
                    entity.motionY += (Math.signum(siren.posY - entity.posY + 1) * 0.5D - entity.motionY) * 0.100000000372529;
                    entity.motionZ += (Math.signum(siren.posZ - entity.posZ) * 0.5D - entity.motionZ) * 0.100000000372529;
                    if(entity.isRiding()) entity.dismountRidingEntity();

                    if(entity.getDistanceSq(siren) < 25D) {//Within 5 blocks
                        capability.reset();
                        siren.singCooldown = IceAndFireConfig.ENTITY_SETTINGS.sirenTimeBetweenSongs;
                        siren.setSinging(false);
                        siren.setAttackTarget(entity);
                        siren.setAggressive(true);
                        siren.triggerOtherSirens(entity);
                    }
                    return;
                }
                //If siren exists but time is up or invalid, set cooldown
                siren.singCooldown = IceAndFireConfig.ENTITY_SETTINGS.sirenTimeBetweenSongs;
            }
            //Reset effect if ended or invalid
            capability.reset();
        }
        else if(capability.isFrozen()) {
            capability.tickTime();
            if(capability.getTime() > 0 && !entity.isDead && EntityEffectCapability.EntityEffectEnum.FROZEN.canBeApplied(entity) && !entity.isInLava()) {
                boolean extinguished = false;
                if(entity.isBurning()) {
                    extinguished = true;
                    entity.extinguish();
                }
                if(!extinguished || capability.getAdditionalData() > 0) {//Severity 0 if not burning and up
                    entity.motionX *= 0.25;
                    entity.motionZ *= 0.25;
                    if(!(entity instanceof EntityDragon) && !entity.onGround) entity.motionY -= 0.1D;
                    if(capability.getAdditionalData() > 0) {//Severity 1 and up
                        entity.motionX = 0;
                        entity.motionZ = 0;
                        if(entity.motionY > 0) entity.motionY = 0;
                        entity.rotationPitch = entity.prevRotationPitch;
                        entity.rotationYaw = entity.prevRotationYaw;
                        entity.onGround = false;
                        if(capability.getAdditionalData() > 1) {//Severity 2 and up
                            if(capability.getTime()%40 == 0) entity.attackEntityFrom(DamageSource.IN_WALL, 1.0F);
                        }
                    }
                    return;
                }
            }
            //Reset effect if ended or invalid
            capability.reset();
            //Spawn particle and sound if ending
            for(int i = 0; i < 4; i++) {
                ParticleHelper.spawnParticle(entity.world,
                        EnumParticleTypes.BLOCK_CRACK,
                        entity.posX + ((world.rand.nextDouble() - 0.5D) * entity.width),
                        entity.posY + ((world.rand.nextDouble()) * entity.height),
                        entity.posZ + ((world.rand.nextDouble() - 0.5D) * entity.width),
                        0, 0, 0,
                        Block.getIdFromBlock(IafBlockRegistry.dragon_ice));
            }
            entity.playSound(SoundEvents.BLOCK_GLASS_BREAK, 3, 1);
        }
        else if(capability.isBlazed()) {
            capability.tickTime();
            if(capability.getTime() > 0 &&
                    !entity.isDead &&
                    EntityEffectCapability.EntityEffectEnum.BLAZED.canBeApplied(entity) &&
                    !entity.isInWater()) {
                if(!entity.isBurning()) entity.setFire(Math.max(capability.getTime()/20, 1));
                entity.motionX *= 0.75;
                entity.motionZ *= 0.75;
                if(capability.getAdditionalData() < 2 && capability.getTime()%20 == 0) entity.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
                if(capability.getAdditionalData() > 0) {//Severity 1 and up
                    entity.motionX *= 0.4;
                    entity.motionZ *= 0.4;
                    if(entity.motionY > 0) entity.motionY *= 0.4;
                    if(capability.getAdditionalData() > 1) {//Severity 2 and up
                        if(capability.getTime()%20 == 0) entity.attackEntityFrom(DamageSource.LAVA, 2.0F);
                    }
                }
                return;
            }
            if(entity.isBurning()) entity.extinguish();
            //Reset effect if ended or invalid
            capability.reset();
            //Spawn particle and sound if ending
            for(int i = 0; i < 4; i++) {
                ParticleHelper.spawnParticle(entity.world,
                        EnumParticleTypes.SMOKE_NORMAL,
                        entity.posX + ((world.rand.nextDouble() - 0.5D) * entity.width),
                        entity.posY + ((world.rand.nextDouble()) * entity.height),
                        entity.posZ + ((world.rand.nextDouble() - 0.5D) * entity.width),
                        0, 0, 0);
            }
            entity.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 3, 1);
        }
        else if(capability.isShocked()) {
            capability.tickTime();
            if(capability.getTime() > 0 &&
                    !entity.isDead &&
                    EntityEffectCapability.EntityEffectEnum.SHOCKED.canBeApplied(entity)) {
                entity.motionX = 0;
                entity.motionZ = 0;
                if(entity.motionY > 0) entity.motionY = 0;
                entity.onGround = false;
                if(capability.getAdditionalData() > 0) {//Severity 1 and up
                    entity.rotationPitch = entity.prevRotationPitch;
                    entity.rotationYaw = entity.prevRotationYaw;
                    if(capability.getAdditionalData() > 1) {//Severity 2 and up
                        if(capability.getTime()%40 == 0) entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 2.0F);
                    }
                }
                return;
            }
            //Reset effect if ended or invalid
            capability.reset();
        }
        else if(capability.isStoned()) {
            boolean stonedPlayer = entity instanceof EntityStoneStatue;
            if(!entity.getPassengers().isEmpty()) {
                for(Entity e : entity.getPassengers()) {
                    e.dismountRidingEntity();
                }
            }
            entity.motionX = 0;
            entity.motionZ = 0;
            entity.motionY -= 0.1D;
            entity.swingProgress = 0;
            entity.limbSwing = 0;
            entity.setInvisible(!stonedPlayer);
            entity.hurtTime = 0;
            entity.hurtResistantTime = entity.maxHurtResistantTime - 1;
            entity.extinguish();
            if(entity instanceof EntityLiving) {
                EntityLiving living = (EntityLiving)entity;
                living.livingSoundTime = 0;
                if(!living.isAIDisabled()) living.setNoAI(true);
                if(living.getAttackTarget() != null) living.setAttackTarget(null);
            }
            if(entity instanceof EntityAnimal) ((EntityAnimal)entity).resetInLove();
            if(entity instanceof EntityHorse) {
                EntityHorse horse = (EntityHorse)entity;
                horse.tailCounter = 0;
                horse.setEatingHaystack(false);
            }
        }
    }

    private static float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = MathHelper.wrapDegrees(targetAngle - angle);
        if(f > maxIncrease) f = maxIncrease;
        if(f < -maxIncrease) f = -maxIncrease;
        return angle + f;
    }
}