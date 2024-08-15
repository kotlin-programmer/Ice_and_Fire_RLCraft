package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderJar extends TileEntitySpecialRenderer<TileEntityJar> {

	@Override
	public void render(TileEntityJar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te.hasPixie) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y + 1.501D, z + 0.5D);
			GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translate(0.0F, te.hasProduced ? 0.9F : 0.6F, 0.0F);
			GlStateManager.rotate(interpolateRotation(te.prevRotationYaw, te.rotationYaw, partialTicks), 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);

			GlStateManager.disableCull();

			switch (te.pixieType) {
				default: this.bindTexture(RenderPixie.TEXTURE_0); break;
				case 1: this.bindTexture(RenderPixie.TEXTURE_1); break;
				case 2: this.bindTexture(RenderPixie.TEXTURE_2); break;
				case 3: this.bindTexture(RenderPixie.TEXTURE_3); break;
				case 4: this.bindTexture(RenderPixie.TEXTURE_4); break;
			}

			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			RenderPixie.PIXIE_MODEL.animateInJar(te.hasProduced, te);

			GlStateManager.enableCull();

			GlStateManager.popMatrix();
		}
	}

	private static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f = yawOffset - prevYawOffset;
		int i = MathHelper.floor(f);
		f = ((((i % 360) + 540) % 360) - 180) + (f - i);
		return prevYawOffset + partialTicks * f;
	}

}
