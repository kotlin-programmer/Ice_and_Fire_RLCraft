package com.github.alexthe666.iceandfire.client.render.tile;

import org.lwjgl.opengl.GL11;

import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderJar extends TileEntitySpecialRenderer<TileEntityJar> {

	@Override
	public void render(TileEntityJar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te.hasPixie) {
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 1.501D, z + 0.5D);
			GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, te.hasProduced ? 0.9F : 0.6F, 0.0F);
			GlStateManager.rotate(interpolateRotation(te.prevRotationYaw, te.rotationYaw, partialTicks), 0.0F, 1.0F, 0.0F);
			GL11.glScalef(0.5F, 0.5F, 0.5F);

			GL11.glDisable(GL11.GL_CULL_FACE);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

			switch (te.pixieType) {
				default: this.bindTexture(RenderPixie.TEXTURE_0); break;
				case 1: this.bindTexture(RenderPixie.TEXTURE_1); break;
				case 2: this.bindTexture(RenderPixie.TEXTURE_2); break;
				case 3: this.bindTexture(RenderPixie.TEXTURE_3); break;
				case 4: this.bindTexture(RenderPixie.TEXTURE_4); break;
				case 5: this.bindTexture(RenderPixie.TEXTURE_5); break;
			}

			GlStateManager.disableLighting();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			GlStateManager.enableLighting();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			RenderPixie.PIXIE_MODEL.animateInJar(te.hasProduced, te, 0.0F);

			GL11.glEnable(GL11.GL_CULL_FACE);

			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}

	private static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f = yawOffset - prevYawOffset;
		f = (((f % 360.0F) + 540.0F) % 360.0F) - 180.0F;
		return prevYawOffset + partialTicks * f;
	}

}
