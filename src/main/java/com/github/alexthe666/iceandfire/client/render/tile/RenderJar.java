package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import net.ilexiconn.llibrary.client.util.ItemTESRContext;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderJar extends TileEntitySpecialRenderer<TileEntityJar> {

	@Override
	public void render(TileEntityJar entity, double x, double y, double z, float f, int f1, float alpha) {
		int meta = 0;
		boolean hasPixie = false;
		if (entity != null && entity.getWorld() != null) {
			meta = entity.pixieType;
			hasPixie = entity.hasPixie;
		} else if (ItemTESRContext.INSTANCE.getCurrentStack() != null) {
			hasPixie = ItemTESRContext.INSTANCE.getCurrentStack().getItemDamage() != 0;
			meta = ItemTESRContext.INSTANCE.getCurrentStack().getItemDamage() - 1;
		}
		if (hasPixie) {
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.5F, (float) y + 1.501F, (float) z + 0.5F);
			GL11.glRotatef(180, 1, 0, 0);
			GL11.glPushMatrix();
			switch (meta) {
				default: this.bindTexture(RenderPixie.TEXTURE_0); break;
				case 1: this.bindTexture(RenderPixie.TEXTURE_1); break;
				case 2: this.bindTexture(RenderPixie.TEXTURE_2); break;
				case 3: this.bindTexture(RenderPixie.TEXTURE_3); break;
				case 4: this.bindTexture(RenderPixie.TEXTURE_4); break;
				case 5: this.bindTexture(RenderPixie.TEXTURE_5); break;
			}
			if (entity != null && entity.getWorld() != null) {
				if (entity.hasProduced) {
					GL11.glTranslatef(0F, 0.90F, 0F);
				} else {
					GL11.glTranslatef(0F, 0.60F, 0F);
				}
				GL11.glDisable(GL11.GL_CULL_FACE);
				GlStateManager.rotate(this.interpolateRotation(entity.prevRotationYaw, entity.rotationYaw, f), 0.0F, 1.0F, 0.0F);
				GL11.glScalef(0.50F, 0.50F, 0.50F);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
				GlStateManager.disableLighting();
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
				GlStateManager.enableLighting();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				RenderPixie.PIXIE_MODEL.animateInJar(entity.hasProduced, entity, 0);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}

	protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f = yawOffset - prevYawOffset;
		f = (((f % 360.0F) + 540.0F) % 360.0F) - 180.0F;
		return prevYawOffset + partialTicks * f;
	}
}