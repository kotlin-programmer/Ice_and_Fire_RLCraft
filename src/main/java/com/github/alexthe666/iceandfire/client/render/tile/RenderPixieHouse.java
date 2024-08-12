package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.client.model.ModelPixieHouse;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;

import net.ilexiconn.llibrary.client.util.ItemTESRContext;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPixieHouse extends TileEntitySpecialRenderer<TileEntityPixieHouse> {

	private static final ModelPixieHouse MODEL = new ModelPixieHouse();
	private static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_0.png");
	private static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_1.png");
	private static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_2.png");
	private static final ResourceLocation TEXTURE_3 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_3.png");
	private static final ResourceLocation TEXTURE_4 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_4.png");
	private static final ResourceLocation TEXTURE_5 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_5.png");

	@Override
	public void render(TileEntityPixieHouse te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		float rotation = 0.0F;
		int meta = 0;
		IBlockState state;
		if (te != null && te.hasWorld() && (state = te.getWorld().getBlockState(te.getPos())).getBlock() instanceof BlockPixieHouse) {
			switch (state.getValue(BlockPixieHouse.FACING)) {
				case NORTH: rotation = 180.0F; break;
				case WEST: rotation = -90.0F; break;
				case EAST: rotation = 90.0F; break;
				default: break;
			}
			meta = te.houseType;
		} else if (!ItemTESRContext.INSTANCE.getCurrentStack().isEmpty()) {
			meta = ItemTESRContext.INSTANCE.getCurrentStack().getItemDamage();
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 1.501D, z + 0.5D);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);

		if (te != null && te.hasWorld() && te.hasPixie) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 0.95F, 0.0F);
			GlStateManager.scale(0.55F, 0.55F, 0.55F);
			GlStateManager.pushMatrix();
			GlStateManager.pushMatrix();

			switch (te.pixieType) {
				default: this.bindTexture(RenderPixie.TEXTURE_0); break;
				case 1: this.bindTexture(RenderPixie.TEXTURE_1); break;
				case 2: this.bindTexture(RenderPixie.TEXTURE_2); break;
				case 3: this.bindTexture(RenderPixie.TEXTURE_3); break;
				case 4: this.bindTexture(RenderPixie.TEXTURE_4); break;
				case 5: this.bindTexture(RenderPixie.TEXTURE_5); break;
			}

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.CONSTANT_ALPHA);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(true);
			GlStateManager.enableLighting();
			GlStateManager.enableColorMaterial();

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
			RenderPixie.PIXIE_MODEL.animateInHouse(te);
			int i = te.getWorld().getCombinedLight(te.getPos(), te.getWorld().getLightFor(EnumSkyBlock.BLOCK, te.getPos()));
			int j = i % 65536;
			int k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);

			GlStateManager.disableColorMaterial();
			GlStateManager.depthMask(true);
			GlStateManager.disableBlend();
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.enableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
		}

		GlStateManager.pushMatrix();

		GlStateManager.disableCull();

		switch (meta) {
			default: this.bindTexture(TEXTURE_0); break;
			case 1: this.bindTexture(TEXTURE_1); break;
			case 2: this.bindTexture(TEXTURE_2); break;
			case 3: this.bindTexture(TEXTURE_3); break;
			case 4: this.bindTexture(TEXTURE_4); break;
			case 5: this.bindTexture(TEXTURE_5); break;
		}

		MODEL.render(0.0625F);

		GlStateManager.enableCull();

		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}

}
