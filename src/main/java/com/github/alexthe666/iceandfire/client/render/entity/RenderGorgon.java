package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.client.model.ModelGorgon;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGorgon extends RenderLiving<EntityGorgon> {

	private static final ResourceLocation PASSIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_passive.png");
	private static final ResourceLocation AGRESSIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_active.png");
	private static final ResourceLocation DEAD_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_decapitated.png");

	public RenderGorgon(RenderManager renderManager) {
		super(renderManager, new ModelGorgon(), 0.6F);
		this.layerRenderers.add(new LayerGorgonEyes(this));
	}

	@Override
	public void preRenderCallback(EntityGorgon entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(0.85F, 0.85F, 0.85F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGorgon gorgon) {
		if (gorgon.getAnimation() == EntityGorgon.ANIMATION_SCARE) {
			return AGRESSIVE_TEXTURE;
		} else if (gorgon.deathTime > 0) {
			return DEAD_TEXTURE;
		} else {
			return PASSIVE_TEXTURE;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class LayerGorgonEyes implements LayerRenderer<EntityGorgon> {

		private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_eyes.png");
		private final RenderGorgon render;

		public LayerGorgonEyes(RenderGorgon renderIn) {
			this.render = renderIn;
		}

		@Override
		public void doRenderLayer(EntityGorgon gorgon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			if (gorgon.getAnimation() == EntityGorgon.ANIMATION_SCARE || gorgon.getAnimation() == EntityGorgon.ANIMATION_HIT) {
				IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(gorgon);
				if (capability == null || !capability.isStoned()) {
					this.render.bindTexture(TEXTURE);
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
					GlStateManager.disableLighting();
					GlStateManager.depthMask(!gorgon.isInvisible());
					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
					GlStateManager.enableLighting();
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					this.render.getMainModel().render(gorgon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
					this.render.setLightmap(gorgon);
					GlStateManager.depthMask(true);
					GlStateManager.disableBlend();
				}
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}
}