package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGeneric extends Item {
	int description = 0;

	public ItemGeneric(String gameName, String name) {
		this.setCreativeTab(IceAndFire.TAB);
		this.setTranslationKey(name);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}

	public ItemGeneric(String gameName, String name, int textLength) {
		this(gameName, name);
		this.description = textLength;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (description > 0) {
			for (int i = 0; i < description; i++) {
				tooltip.add(TextFormatting.GRAY + I18n.format(this.getTranslationKey() + ".desc_" + i));
			}
		}
	}
}
