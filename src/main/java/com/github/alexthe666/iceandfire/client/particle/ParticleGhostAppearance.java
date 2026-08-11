package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleMobAppearance;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleGhostAppearance extends ParticleMobAppearance {
    private boolean fromLeft;
    private EntityLivingBase entity;

    public ParticleGhostAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.particleMaxAge = 15;
        fromLeft = worldIn.rand.nextBoolean();
    }

    public int getFXLayer() {
        return 3;
    }

    public void onUpdate() {
        super.onUpdate();

        if (this.entity == null) {
            IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(Minecraft.getMinecraft().player);
            EntityGhost ghost = null;
            if (capability != null && capability.isSpooked()) {
               ghost = capability.getGhost(Minecraft.getMinecraft().player.world);
            }
            this.entity = ghost;
        }
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (this.entity != null) {
            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setRenderPosition(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);
            float f1 = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float f2 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f2, f2);
            GlStateManager.pushMatrix();
            float f3 = 0.05F + 0.5F * MathHelper.sin(f1 * (float) Math.PI);
            GlStateManager.color(1.0F, 1.0F, 1.0F, f3);
            GlStateManager.translate(0.0F, 1.8F, 0.0F);
            GlStateManager.rotate(180.0F - entityIn.rotationYaw, 0.0F, 1.0F, 0.0F);

            if (fromLeft) {
                GlStateManager.rotate(60.0F - 150.0F * f1 - entityIn.rotationPitch, 0.0F, -1.0F, 0.0F);
                GlStateManager.rotate(60.0F - 150.0F * f1 - entityIn.rotationPitch, 0.0F, 0.0F, -1.0F);
            } else {
                GlStateManager.rotate(60.0F - 150.0F * f1 - entityIn.rotationPitch, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(60.0F - 150.0F * f1 - entityIn.rotationPitch, 0.0F, 0.0F, 1.0F);
            }

            GlStateManager.translate(0.0F, -1.2F, -1.25F);
            GlStateManager.scale(0.6F, 0.6F, 0.6F);

            float rotationYaw = this.entity.rotationYaw;
            float rotationYawHead = this.entity.rotationYawHead;
            float prevRotationYaw = this.entity.prevRotationYaw;
            float prevRotationYawHead = this.entity.prevRotationYawHead;

            this.entity.rotationYaw = 0.0F;
            this.entity.rotationYawHead = 0.0F;
            this.entity.prevRotationYaw = 0.0F;
            this.entity.prevRotationYawHead = 0.0F;

            EntityGhost ghost = (EntityGhost) entity;
            ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
            Render<?> render = rendermanager.getEntityRenderObject(ghost);
            if (render instanceof RenderLivingBase) {
                ((RenderLivingBase<?>) render).getMainModel().setRotationAngles(0, 0, ghost.ticksExisted + partialTicks, 0, 0, 0.0625f, entity);
            }
            rendermanager.renderEntity(ghost, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);

            this.entity.rotationYaw = rotationYaw;
            this.entity.rotationYawHead = rotationYawHead;
            this.entity.prevRotationYaw = prevRotationYaw;
            this.entity.prevRotationYawHead = prevRotationYawHead;

            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }
}
