package com.github.alexthe666.iceandfire.capability.entityeffect;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.enums.EnumParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityEffectClientHandler {
    private static final ResourceLocation SIREN_SHADER = new ResourceLocation("iceandfire:shaders/post/siren.json");

    public static void tickUpdate(EntityLivingBase entity, World world, IEntityEffectCapability capability) {
        if(capability.isCharmed()) {
            EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
            EntitySiren siren = capability.getSiren(world);
            if(siren != null) {
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
            }
            if(entity == Minecraft.getMinecraft().player) {
                if(world.rand.nextInt(40) == 0) IceAndFire.PROXY.spawnParticle(EnumParticle.SIREN_APPEARANCE, world, entity.posX, entity.posY, entity.posZ, 0, 0, 0);
                if(IceAndFireConfig.CLIENT_SETTINGS.sirenShader && !renderer.isShaderActive()) renderer.loadShader(SIREN_SHADER);
            }
        }
        else {
            if(entity == Minecraft.getMinecraft().player) {
                EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
                if(IceAndFireConfig.CLIENT_SETTINGS.sirenShader &&
                        renderer != null &&
                        renderer.getShaderGroup() != null &&
                        renderer.getShaderGroup().getShaderGroupName() != null &&
                        SIREN_SHADER.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
                    renderer.stopUseShader();
                }
            }
        }

        if(capability.isFrozen()) {
            capability.tickTime();//Advance texture
            if(!entity.isBurning() || capability.getAdditionalData() > 0) {
                entity.motionX *= 0.25;
                entity.motionZ *= 0.25;
                if(!(entity instanceof EntityDragon) && !entity.onGround) entity.motionY -= 0.1D;
                if(capability.getAdditionalData() > 0) {
                    entity.motionX = 0;
                    entity.motionZ = 0;
                    if(entity.motionY > 0) entity.motionY = 0;
                    entity.rotationPitch = entity.prevRotationPitch;
                    entity.rotationYaw = entity.prevRotationYaw;
                    entity.onGround = false;
                }
            }
        }
        else if(capability.isBlazed()) {
            entity.motionX *= 0.75;
            entity.motionZ *= 0.75;
            if(capability.getAdditionalData() > 0) {
                entity.motionX *= 0.4;
                entity.motionZ *= 0.4;
                if (entity.motionY > 0) entity.motionY *= 0.4;
            }
        }
        else if(capability.isShocked()) {
            entity.motionX = 0;
            entity.motionZ = 0;
            if(entity.motionY > 0) entity.motionY = 0;
            entity.onGround = false;
            if(capability.getAdditionalData() > 0) {
                entity.rotationPitch = entity.prevRotationPitch;
                entity.rotationYaw = entity.prevRotationYaw;
            }
        }
        else if (capability.isSpooked()) {
            EntityGhost ghost = capability.getGhost(world);
            if (ghost != null) {
                if (ghost.getAnimation() == EntityGhost.ANIMATION_SCARE && ghost.getAnimationTick() == 3 && !ghost.isHauntedShoppingList()) {
                    if (world.rand.nextInt(3) == 0) {
                        IceAndFire.PROXY.spawnParticle(EnumParticle.GHOST_APPEARANCE, world, ghost.posX, ghost.posY, ghost.posZ, 0, 0, 0);
                    }
                }
            }
        }
        else if(capability.isStoned()) {
            boolean stonedPlayer = entity instanceof EntityStoneStatue;
            entity.motionX = 0;
            entity.motionZ = 0;
            entity.motionY -= 0.1D;
            entity.swingProgress = 0;
            entity.limbSwing = 0;
            entity.setInvisible(!stonedPlayer);
            if(entity instanceof EntityLiving) {
                EntityLiving living = (EntityLiving)entity;
                living.livingSoundTime = 0;
            }
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