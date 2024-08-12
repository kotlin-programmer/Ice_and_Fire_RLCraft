package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadLichSkull;
import com.github.alexthe666.iceandfire.entity.projectile.EntityDreadLichSkull;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderDreadLichSkull extends Render<EntityDreadLichSkull> {

	public static final ModelDreadLichSkull MODEL = new ModelDreadLichSkull();
	public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_skull.png");

	public RenderDreadLichSkull(RenderManager manager) {
		super(manager);
	}

	@Override
	public void doRender(EntityDreadLichSkull entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.0F, -1.0F, 1.0F);
		GlStateManager.rotate(interpolateRotation(entity.prevRotationYaw, entity.rotationYaw, partialTicks) - 180.0F, 0.0F, 1.0F, 0.0F);

		GlStateManager.disableCull();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableLighting();
		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		this.bindEntityTexture(entity);

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
		MODEL.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, 0.0F, 0.0625F);

		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	private static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f = yawOffset - prevYawOffset;
		int i = MathHelper.floor(f);
		f = ((((i % 360) + 540) % 360) - 180) + (f - i);
		return prevYawOffset + partialTicks * f;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDreadLichSkull entity) {
		return TEXTURE;
	}

}
