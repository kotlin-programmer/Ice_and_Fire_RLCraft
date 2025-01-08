package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemScaleArmor;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum EnumDragonArmor {

	armor_red(12, EnumDragonEgg.RED),
	armor_bronze(13, EnumDragonEgg.BRONZE),
	armor_green(14, EnumDragonEgg.GREEN),
	armor_gray(15, EnumDragonEgg.GRAY),
	armor_blue(12, EnumDragonEgg.BLUE),
	armor_white(13, EnumDragonEgg.WHITE),
	armor_sapphire(14, EnumDragonEgg.SAPPHIRE),
	armor_silver(15, EnumDragonEgg.SILVER),
	armor_electric(16, EnumDragonEgg.ELECTRIC),
	armor_amethyst(17, EnumDragonEgg.AMETHYST),
	armor_copper(18, EnumDragonEgg.COPPER),
	armor_black(19, EnumDragonEgg.BLACK);

	public ArmorMaterial material;
	public int armorId;
	public EnumDragonEgg eggType;
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_helmet")
	public Item helmet;
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_chestplate")
	public Item chestplate;
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_leggings")
	public Item leggings;
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_boots")
	public Item boots;
	public ArmorMaterial armorMaterial;

	EnumDragonArmor(int armorId, EnumDragonEgg eggType) {
		this.armorId = armorId;
		this.eggType = eggType;
	}

	public static void initArmors() {
		for (int i = 0; i < EnumDragonArmor.values().length; i++) {
			EnumDragonArmor.values()[i].armorMaterial = EnumHelper.addArmorMaterial("DragonScales" + (i + 1), "iceandfire:armor_dragon_scales" + (i + 1), 36, new int[]{5, 7, 9, 5}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2);
			EnumDragonArmor.values()[i].helmet = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].armorMaterial, 0, EntityEquipmentSlot.HEAD).setTranslationKey("iceandfire.dragonHelmet");
			EnumDragonArmor.values()[i].chestplate = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].armorMaterial, 1, EntityEquipmentSlot.CHEST).setTranslationKey("iceandfire.dragonChestplate");
			EnumDragonArmor.values()[i].leggings = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].armorMaterial, 2, EntityEquipmentSlot.LEGS).setTranslationKey("iceandfire.dragonLeggings");
			EnumDragonArmor.values()[i].boots = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].armorMaterial, 3, EntityEquipmentSlot.FEET).setTranslationKey("iceandfire.dragonBoots");
			EnumDragonArmor.values()[i].helmet.setRegistryName(IceAndFire.MODID, EnumDragonArmor.values()[i].name() + "_helmet");
			EnumDragonArmor.values()[i].chestplate.setRegistryName(IceAndFire.MODID, EnumDragonArmor.values()[i].name() + "_chestplate");
			EnumDragonArmor.values()[i].leggings.setRegistryName(IceAndFire.MODID, EnumDragonArmor.values()[i].name() + "_leggings");
			EnumDragonArmor.values()[i].boots.setRegistryName(IceAndFire.MODID, EnumDragonArmor.values()[i].name() + "_boots");
		}
	}

	public static Item getScaleItem(EnumDragonArmor armor) {
		switch (armor) {
			case armor_bronze:
				return IafItemRegistry.dragonscales_bronze;
			case armor_green:
				return IafItemRegistry.dragonscales_green;
			case armor_gray:
				return IafItemRegistry.dragonscales_gray;
			case armor_blue:
				return IafItemRegistry.dragonscales_blue;
			case armor_white:
				return IafItemRegistry.dragonscales_white;
			case armor_sapphire:
				return IafItemRegistry.dragonscales_sapphire;
			case armor_silver:
				return IafItemRegistry.dragonscales_silver;
			case armor_electric:
				return IafItemRegistry.dragonscales_electric;
			case armor_amethyst:
				return IafItemRegistry.dragonscales_amethyst;
			case armor_copper:
				return IafItemRegistry.dragonscales_copper;
			case armor_black:
				return IafItemRegistry.dragonscales_black;
			default:
				return IafItemRegistry.dragonscales_red;
		}
	}
}
