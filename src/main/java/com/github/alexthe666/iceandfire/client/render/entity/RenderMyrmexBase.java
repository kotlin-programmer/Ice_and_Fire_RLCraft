package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMyrmexBase extends RenderLiving<EntityMyrmexBase> {

	public RenderMyrmexBase(RenderManager renderManager, ModelBase model, float shadowSize) {
		super(renderManager, model, shadowSize);
		this.addLayer(new LayerMyrmexItem(this));
	}

	@Override
	public void preRenderCallback(EntityMyrmexBase myrmex, float partialTickTime) {
		float scale = myrmex.getModelScale();
		if(myrmex.getGrowthStage() == 0){
			scale /= 2;
		}
		if(myrmex.getGrowthStage() == 1){
			scale /= 1.5F;
		}
		GlStateManager.scale(scale, scale, scale);
		if(myrmex.isRiding() && myrmex.getGrowthStage() < 2){
			GlStateManager.rotate(90, 0, 1, 0);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMyrmexBase myrmex) {
		return myrmex.getTexture();
	}

	@SideOnly(Side.CLIENT)
	public static class LayerMyrmexItem implements LayerRenderer<EntityMyrmexBase> {

		private final RenderMyrmexBase livingEntityRenderer;

		public LayerMyrmexItem(RenderMyrmexBase livingEntityRendererIn) {
			this.livingEntityRenderer = livingEntityRendererIn;
		}

		@Override
		public void doRenderLayer(EntityMyrmexBase myrmex, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			if(myrmex instanceof EntityMyrmexWorker) {
				ItemStack stack = myrmex.getHeldItem(EnumHand.MAIN_HAND);
				if (!stack.isEmpty()) {
					GlStateManager.pushMatrix();
					if (myrmex.isSneaking()) {
						GlStateManager.translate(0.0F, 0.2F, 0.0F);
					}
					((ModelMyrmexBase)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, EnumHandSide.RIGHT);
					if(Minecraft.getMinecraft().getRenderItem().shouldRenderItemIn3D(stack)){
						GlStateManager.translate(0F, 0.25F, -1.65F);
					}else{
						GlStateManager.translate(0F, 1F, -2F);
					}
					GlStateManager.rotate(160, 1, 0, 0);
					GlStateManager.rotate(180, 0, 1, 0);
					Minecraft.getMinecraft().getItemRenderer().renderItem(myrmex, stack, ItemCameraTransforms.TransformType.HEAD);
					GlStateManager.popMatrix();
				}
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}
}