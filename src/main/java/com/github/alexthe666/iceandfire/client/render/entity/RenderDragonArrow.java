package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.projectile.EntityDragonArrow;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDragonArrow extends Render<EntityDragonArrow> {

	private final RenderItem itemRenderer;

	public RenderDragonArrow(RenderManager render, RenderItem itemRenderer) {
		super(render);
		this.itemRenderer = itemRenderer;
	}

	@Override
	public void doRender(EntityDragonArrow entity, double x, double y, double z, float yaw, float partialTicks) {
		GlStateManager.pushMatrix();
		double posX = x, posY = y, posZ = z;
		if (entity.isAirBorne) {
			posX += (entity.motionX * partialTicks);
			posY += (entity.motionY * partialTicks);
			posZ += (entity.motionZ * partialTicks);
		}

		GlStateManager.translate((float) posX, (float) posY, (float) posZ);
		GlStateManager.scale(1.5d, 1.5d, 1.5d);
		GlStateManager.enableRescaleNormal();

		this.doRenderTransformations(entity, partialTicks);

		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		ItemStack arrow = new ItemStack(entity.getType().getArrow());
		this.itemRenderer.renderItem(arrow, ItemCameraTransforms.TransformType.GROUND);
		GlStateManager.translate(0.10, 0.20, 0.0);
		GlStateManager.rotate(90, 1.0f, 1.0f, 0.0f);
		GlStateManager.translate(-0.10, -0.20, 0.0);
		this.itemRenderer.renderItem(arrow, ItemCameraTransforms.TransformType.GROUND);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, yaw, partialTicks);
	}

	/**
	 * Performs the rendering transformations before the entity is rendered
	 * @param entity
	 * @param partialTicks
	 */
	protected void doRenderTransformations(EntityDragonArrow entity, float partialTicks)
	{
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks - 45.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.translate(-0.10, -0.20, 0.0);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDragonArrow arrow) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}