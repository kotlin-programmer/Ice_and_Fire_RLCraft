package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEggInIce extends TileEntitySpecialRenderer<TileEntityEggInIce> {

	private static final ModelDragonEgg MODEL = new ModelDragonEgg();

	@Override
	public void render(TileEntityEggInIce egg, double x, double y, double z, float f, int f1, float alpha) {
		if (egg.type != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.5F, (float) y - 0.75F, (float) z + 0.5F);
			GL11.glPushMatrix();
			EnumDragonEgg eggType = egg.type.dragonType != EnumDragonType.ICE ? EnumDragonEgg.BLUE : egg.type;
			this.bindTexture(RenderPodium.getEggTexture(eggType));
			GL11.glPushMatrix();
			MODEL.renderFrozen();
			GL11.glPopMatrix();
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}
}