package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class RenderGhostSword<T extends EntityGhostSword> extends Render<T>
{
    private final RenderItem itemRenderer;

    public RenderGhostSword(RenderManager renderManagerIn, RenderItem itemRendererIn) {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        double posX = x;
        double posY = y;
        double posZ = z;
        if (entity.isAirBorne) {
            posX = x + entity.motionX * (double)partialTicks;
            posY = y + entity.motionY * (double)partialTicks;
            posZ = z + entity.motionZ * (double)partialTicks;
        }

        GlStateManager.translate((float)posX, (float)posY, (float)posZ);
        GlStateManager.scale(2.0, 2.0, 2.0);
        GlStateManager.enableRescaleNormal();
        this.doRenderTransformations(entity, partialTicks);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        this.itemRenderer.renderItem(ModItems.ghost_sword.getDefaultInstance(), TransformType.GROUND);

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void doRenderTransformations(T entity, float partialTicks) {
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks - 45.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate((entity.ticksExisted + partialTicks) * 40F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(-0.1, -0.2, 0.0);
    }

    protected ResourceLocation getEntityTexture(T entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}