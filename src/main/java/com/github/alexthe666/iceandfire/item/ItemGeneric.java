package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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

}
