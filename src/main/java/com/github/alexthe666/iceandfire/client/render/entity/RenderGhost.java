package com.github.alexthe666.iceandfire.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.github.alexthe666.iceandfire.client.model.ModelGhost;
import com.github.alexthe666.iceandfire.entity.EntityGhost;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderGhost extends RenderLiving<EntityGhost> {

	public static final ModelGhost MODEL = new ModelGhost(0.0F);
	public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_white.png");
	public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_blue.png");
	public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_green.png");
	public static final ResourceLocation TEXTURE_SHOPPING_LIST = new ResourceLocation("iceandfire:textures/models/ghost/haunted_shopping_list.png");

	public RenderGhost(RenderManager renderManager) {
		super(renderManager, MODEL, 0.55F);
		preRenderProfileGhostClean();
	}

	public void preRenderProfileGhostApply(EntityGhost entity, float partialTicks) {
		float alphaForRender = getAlphaForRender(entity, partialTicks);
		GlStateManager.color(1.0F, 1.0F, 1.0F, alphaForRender);
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.001F);
	}

	public void preRenderProfileGhostClean() {
		GlStateManager.disableBlend();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.depthMask(true);
	}

	private float getAlphaForRender(EntityGhost entity, float partialTicks) {
		if (entity.isDaytimeMode()) {
			return MathHelper.clamp((101 - Math.min(entity.getDaytimeCounter(), 100)) / 100.0F, 0.0F, 1.0F);
		}
		return MathHelper.clamp((MathHelper.sin((entity.ticksExisted + partialTicks) * 0.1F) + 1.0F) * 0.5F + 0.1F, 0.0F, 1.0F);
	}

	@Override
	public void preRenderCallback(EntityGhost entitylivingbaseIn, float partialTickTime) {
		this.shadowSize = 0.0F;
		preRenderProfileGhostApply(entitylivingbaseIn, partialTickTime);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGhost entity) {
		switch (entity.getColor()) {
			default: return TEXTURE_0;
			case 1: return TEXTURE_1;
			case 2: return TEXTURE_2;
			case -1: return TEXTURE_SHOPPING_LIST;
		}
	}

	@Override
	protected float getDeathMaxRotation(EntityGhost entityLivingBaseIn) {
		return 0.0F;
	}

}
