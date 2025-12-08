package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDeathWormGauntlet;
import com.github.alexthe666.iceandfire.client.model.ModelTrollWeapon;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDeathWorm;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDeathwormGauntlet;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;

import net.ilexiconn.llibrary.client.util.ItemTESRContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class IceAndFireTEISR extends TileEntityItemStackRenderer {

	private static final ModelBase TROLL_WEAPON = new ModelTrollWeapon();
	private static final ModelDeathWormGauntlet DEATHWORM_GAUNTLET = new ModelDeathWormGauntlet();

	@Override
	public void renderByItem(ItemStack itemStackIn) {
		if (itemStackIn.getItem() instanceof ItemTrollWeapon) {
			ItemTrollWeapon weaponItem = (ItemTrollWeapon) itemStackIn.getItem();
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5F, -0.75F, 0.5F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(weaponItem.weapon.TEXTURE);
			TROLL_WEAPON.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GlStateManager.popMatrix();
		}
		if (itemStackIn.getItem() instanceof ItemDeathwormGauntlet) {
			ResourceLocation texture;
			if (itemStackIn.getItem() == IafItemRegistry.deathworm_gauntlet_red) {
				texture = RenderDeathWorm.TEXTURE_RED;
			} else if (itemStackIn.getItem() == IafItemRegistry.deathworm_gauntlet_white) {
				texture = RenderDeathWorm.TEXTURE_WHITE;
			} else {
				texture = RenderDeathWorm.TEXTURE_YELLOW;
			}
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glPushMatrix();
			Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
			GL11.glPushMatrix();
			if (ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND ||
					ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND) {
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				DEATHWORM_GAUNTLET.animate(itemStackIn, Minecraft.getMinecraft().getRenderPartialTicks());
			} else if (ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND ||
					ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
				DEATHWORM_GAUNTLET.animate(itemStackIn, Minecraft.getMinecraft().getRenderPartialTicks());
			} else {
				DEATHWORM_GAUNTLET.resetToDefaultPose();
			}
			DEATHWORM_GAUNTLET.render(null, 0, 0, 0, 0, 0, 0.0625F);
			GL11.glPopMatrix();
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}

}
