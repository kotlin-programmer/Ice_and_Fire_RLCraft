package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerGenericGlowing implements LayerRenderer<EntityLiving> {
    private final RenderLiving render;
    private ResourceLocation texture;

    public LayerGenericGlowing(RenderLiving renderIn, ResourceLocation texture) {
        this.render = renderIn;
        this.texture = texture;
    }

    public void doRenderLayer(EntityLiving entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(entity);
        if (capability == null || !capability.isStoned()) {
            this.render.bindTexture(texture);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(!entity.isInvisible());
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.render.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            this.render.setLightmap(entity);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}