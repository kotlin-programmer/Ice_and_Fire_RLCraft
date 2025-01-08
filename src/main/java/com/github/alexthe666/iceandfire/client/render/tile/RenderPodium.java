package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderMyrmexEgg;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderPodium extends TileEntitySpecialRenderer<TileEntityPodium> {

	public static final ModelDragonEgg MODEL = new ModelDragonEgg();

	@Override
	public void render(TileEntityPodium te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = te.getStackInSlot(0);
		Item item;
		if (!stack.isEmpty() && ((item = stack.getItem()) instanceof ItemDragonEgg || item instanceof ItemMyrmexEgg)) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y + 0.475D, z + 0.5D);

			if (item instanceof ItemDragonEgg) {
				this.bindTexture(getEggTexture(((ItemDragonEgg) item).type));
			} else if (item instanceof ItemMyrmexEgg) {
				this.bindTexture(item == IafItemRegistry.myrmex_jungle_egg ? RenderMyrmexEgg.EGG_JUNGLE : RenderMyrmexEgg.EGG_DESERT);
			}

			MODEL.renderPodium();

			GlStateManager.popMatrix();
		} else {
			GlStateManager.pushMatrix();

			float ageInTicks = te.ticksExisted + partialTicks;
			float yOffset = MathHelper.sin(ageInTicks / 10.0F) * 0.1F + 0.1F;
			GlStateManager.translate(x + 0.5D, y + 1.55D + yOffset, z + 0.5D);
			float rotation = (float) Math.toDegrees(ageInTicks / 20.0F);
			GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);

			Minecraft mc = Minecraft.getMinecraft();
			mc.getItemRenderer().renderItem(mc.player, stack, TransformType.GROUND);

			GlStateManager.popMatrix();
		}
	}

	public static ResourceLocation getEggTexture(EnumDragonEgg type) {
		switch (type) {
			default: return RenderDragonEgg.EGG_RED;
			case GREEN: return RenderDragonEgg.EGG_GREEN;
			case BRONZE: return RenderDragonEgg.EGG_BRONZE;
			case GRAY: return RenderDragonEgg.EGG_GREY;
			case BLUE: return RenderDragonEgg.EGG_BLUE;
			case WHITE: return RenderDragonEgg.EGG_WHITE;
			case SAPPHIRE: return RenderDragonEgg.EGG_SAPPHIRE;
			case SILVER: return RenderDragonEgg.EGG_SILVER;
			case ELECTRIC: return RenderDragonEgg.EGG_ELECTRIC;
			case AMETHYST: return RenderDragonEgg.EGG_AMETHYST;
			case COPPER: return RenderDragonEgg.EGG_COPPER;
			case BLACK: return RenderDragonEgg.EGG_BLACK;
		}
	}

}
