package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDragonFlesh extends ItemFood {

	int dragonType;

	static String getRegistryName(int dragonType) {
		switch (dragonType) {
			case 1:
				return "ice_dragon_flesh";
			case 2:
				return "lightning_dragon_flesh";
			default:
				return "fire_dragon_flesh";
		}
	}

	static String getTranslationKey(int dragonType) {
		switch (dragonType) {
			case 1:
				return "iceandfire.ice_dragon_flesh";
			case 2:
				return "iceandfire.lightning_dragon_flesh";
			default:
				return "iceandfire.fire_dragon_flesh";
		}
	}

	public ItemDragonFlesh(int dragonType) {
		super(8, 0.8F, true);
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey(getTranslationKey(dragonType));
		this.setRegistryName(IceAndFire.MODID, getRegistryName(dragonType));
		this.dragonType = dragonType;
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		if (!worldIn.isRemote) {
			if (dragonType == 1) {
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
			} else if (dragonType == 2) {
				EntityLightningBolt lightningBolt = new EntityLightningBolt(worldIn, player.posX, player.posY, player.posZ, false);
				worldIn.spawnEntity(lightningBolt);
			} else {
				player.setFire(5);
			}
		}
	}
}
