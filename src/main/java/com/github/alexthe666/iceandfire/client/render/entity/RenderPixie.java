package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPixie extends RenderLiving<EntityPixie> {

	public static final ModelPixie PIXIE_MODEL = new ModelPixie();
	public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_0.png");
	public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_1.png");
	public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_2.png");
	public static final ResourceLocation TEXTURE_3 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_3.png");
	public static final ResourceLocation TEXTURE_4 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_4.png");

	public RenderPixie(RenderManager renderManager) {
		super(renderManager, PIXIE_MODEL, 0.2F);
		this.layerRenderers.add(new LayerPixieItem(this));
		this.layerRenderers.add(new LayerPixieGlow(this));
	}

	@Override
	public void preRenderCallback(EntityPixie entitylivingbaseIn, float partialTickTime) {
		GL11.glScalef(0.55F, 0.55F, 0.55F);
		if (entitylivingbaseIn.isSitting()) {
			GL11.glTranslatef(0F, 0.5F, 0F);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPixie pixie) {
		switch (pixie.getColor()) {
			default: return TEXTURE_0;
			case 1: return TEXTURE_1;
			case 2: return TEXTURE_2;
			case 3: return TEXTURE_3;
			case 4: return TEXTURE_4;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class LayerPixieItem implements LayerRenderer<EntityPixie> {

		private final RenderPixie renderer;

		public LayerPixieItem(RenderPixie renderer) {
			this.renderer = renderer;
		}

		@Override
		public void doRenderLayer(EntityPixie entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			ItemStack itemstack = entity.getHeldItem(EnumHand.MAIN_HAND);
			if (!itemstack.isEmpty()) {
				GlStateManager.color(1.0F, 1.0F, 1.0F);
				GlStateManager.pushMatrix();
				if (this.renderer.getMainModel().isChild) {
					GlStateManager.translate(0.0F, 0.625F, 0.0F);
					GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
					GlStateManager.scale(0.5F, 0.5F, 0.5F);
				}
				GlStateManager.translate(-0.0625F, 0.53125F, 0.21875F);
				GlStateManager.translate(-0.075F, 0, -0.05F);
				GlStateManager.rotate(-10, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.05F, 0.55F, -0.4F);
				GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(140.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(12.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(220.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-15.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(40.0F, 0.0F, 0.0F, 1.0F);
				Minecraft.getMinecraft().getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.GROUND);
				GlStateManager.popMatrix();
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class LayerPixieGlow implements LayerRenderer<EntityPixie> {

		private final RenderPixie render;

		public LayerPixieGlow(RenderPixie renderIn) {
			this.render = renderIn;
		}

		@Override
		public void doRenderLayer(EntityPixie pixie, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(pixie);
			if (capability == null || !capability.isStoned()) {
				switch (pixie.getColor()) {
					default: this.render.bindTexture(RenderPixie.TEXTURE_0); break;
					case 1: this.render.bindTexture(RenderPixie.TEXTURE_1); break;
					case 2: this.render.bindTexture(RenderPixie.TEXTURE_2); break;
					case 3: this.render.bindTexture(RenderPixie.TEXTURE_3); break;
					case 4: this.render.bindTexture(RenderPixie.TEXTURE_4); break;
				}
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
				GlStateManager.disableLighting();
				GlStateManager.depthMask(!pixie.isInvisible());
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
				GlStateManager.enableLighting();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.render.getMainModel().render(pixie, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
				this.render.setLightmap(pixie);
				GlStateManager.depthMask(true);
				GlStateManager.disableBlend();
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}
}