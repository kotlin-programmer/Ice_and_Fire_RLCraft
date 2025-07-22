package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelStonePlayer;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderStoneStatue extends RenderLiving<EntityStoneStatue> {

	private static final ModelStonePlayer MODEL = new ModelStonePlayer(0, false);
	private static final ModelStonePlayer MODEL_SLIM = new ModelStonePlayer(0, true);

	public RenderStoneStatue(RenderManager renderManager) {
		super(renderManager, MODEL, 0.5F);
		this.layerRenderers.add(new LayerStonePlayerEntityCrack(this));
		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				this.modelLeggings = new ModelStonePlayer(0.5F, true);
				this.modelArmor = new ModelStonePlayer(1.0F, true);
			}
		};
		this.addLayer(layerbipedarmor);
	}

	@Override
	public ModelBase getMainModel() {
		return MODEL;
	}

	@Override
	protected void renderModel(EntityStoneStatue entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		boolean flag = !entity.isInvisible() || this.renderOutlines;
		boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().player);

		if (flag || flag1) {
			if (!this.bindEntityTexture(entity)) {
				return;
			}
			if (flag1) {
				GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}
			if (entity.smallArms) {
				MODEL_SLIM.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			} else {
				MODEL.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			}
			if (flag1) {
				GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}
		}
	}

	private String getStoneType(ModelBase model) {
		int sizeX = clampTexture(model.textureWidth);
		int sizeY = clampTexture(model.textureHeight);
		if(sizeX > sizeY && sizeX/2 != sizeY) sizeY = sizeX/2;
		if(sizeY > sizeX && sizeY/2 != sizeX) sizeX = sizeY/2;
		return sizeX <= 16 && sizeY <= 16 ? "textures/blocks/stone.png" : "iceandfire:textures/models/gorgon/stone" + sizeX + "x" + sizeY + ".png";
	}

	private int clampTexture(int i) {
		if(i >= 128) return 128;
		if(i >= 64) return 64;
		if(i >= 32) return 32;
		return 16;
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityStoneStatue entity) {
		return new ResourceLocation(getStoneType(entity.smallArms ? MODEL : MODEL_SLIM));
	}

	@SideOnly(Side.CLIENT)
	public static class LayerStonePlayerEntityCrack implements LayerRenderer<EntityStoneStatue> {

		private static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png")};
		private final RenderLivingBase<EntityStoneStatue> renderer;

		public LayerStonePlayerEntityCrack(RenderLivingBase<EntityStoneStatue> renderer) {
			this.renderer = renderer;
		}

		@Override
		public void doRenderLayer(EntityStoneStatue stoneStatue, float f, float f1, float i, float f2, float f3, float f4, float f5) {
			int breakCount = stoneStatue.getCrackAmount();
			if(breakCount > 0) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.depthFunc(514);
				GlStateManager.depthMask(false);
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.matrixMode(5890);
				GlStateManager.loadIdentity();
				GlStateManager.scale((float)this.renderer.getMainModel().textureHeight / 16.0F, (float)this.renderer.getMainModel().textureWidth / 16.0F, 1.0F);
				GlStateManager.matrixMode(5888);
				this.renderer.bindTexture(DESTROY_STAGES[breakCount - 1]);
				this.renderer.getMainModel().render(stoneStatue, f, f1, f2, f3, f4, f5);
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.matrixMode(5890);
				GlStateManager.loadIdentity();
				GlStateManager.matrixMode(5888);
				GlStateManager.depthMask(true);
				GlStateManager.depthFunc(515);
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return true;
		}
	}
}