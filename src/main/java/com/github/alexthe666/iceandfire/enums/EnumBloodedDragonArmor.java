package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.item.ItemBloodedArmor;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum EnumBloodedDragonArmor {

	armor_red(EnumDragonEgg.RED),
	armor_bronze(EnumDragonEgg.BRONZE),
	armor_green(EnumDragonEgg.GREEN),
	armor_gray(EnumDragonEgg.GRAY),
	armor_blue(EnumDragonEgg.BLUE),
	armor_white(EnumDragonEgg.WHITE),
	armor_sapphire(EnumDragonEgg.SAPPHIRE),
	armor_silver(EnumDragonEgg.SILVER),
	armor_electric(EnumDragonEgg.ELECTRIC),
	armor_amethyst(EnumDragonEgg.AMETHYST),
	armor_copper(EnumDragonEgg.COPPER),
	armor_black(EnumDragonEgg.BLACK);

	public ArmorMaterial material;
	public final EnumDragonEgg eggType;
	
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_helmet_blooded")
	public Item helmet;
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_chestplate_blooded")
	public Item chestplate;
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_leggings_blooded")
	public Item leggings;
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_boots_blooded")
	public Item boots;
	
	public ArmorMaterial armorMaterial;

	EnumBloodedDragonArmor(EnumDragonEgg eggType) {
		this.eggType = eggType;
	}

	public static void initArmors() {
		for(int i = 0; i < EnumBloodedDragonArmor.values().length; i++) {
			if(EnumBloodedDragonArmor.values()[i].eggType.dragonType == EnumDragonType.FIRE) {
				EnumBloodedDragonArmor.values()[i].armorMaterial = EnumHelper.addArmorMaterial("FlamedDragonScales" + (i + 1), "iceandfire:armor_dragon_scales" + (i + 1), 44, new int[]{5, 7, 9, 5}, 25, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2);
				EnumBloodedDragonArmor.values()[i].helmet = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 0, EntityEquipmentSlot.HEAD).setTranslationKey("iceandfire.dragonHelmet.flamed");
				EnumBloodedDragonArmor.values()[i].chestplate = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 1, EntityEquipmentSlot.CHEST).setTranslationKey("iceandfire.dragonChestplate.flamed");
				EnumBloodedDragonArmor.values()[i].leggings = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 2, EntityEquipmentSlot.LEGS).setTranslationKey("iceandfire.dragonLeggings.flamed");
				EnumBloodedDragonArmor.values()[i].boots = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 3, EntityEquipmentSlot.FEET).setTranslationKey("iceandfire.dragonBoots.flamed");
				EnumBloodedDragonArmor.values()[i].helmet.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_helmet_flamed");
				EnumBloodedDragonArmor.values()[i].chestplate.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_chestplate_flamed");
				EnumBloodedDragonArmor.values()[i].leggings.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_leggings_flamed");
				EnumBloodedDragonArmor.values()[i].boots.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_boots_flamed");
			}
			else if(EnumBloodedDragonArmor.values()[i].eggType.dragonType == EnumDragonType.ICE) {
				EnumBloodedDragonArmor.values()[i].armorMaterial = EnumHelper.addArmorMaterial("IcedDragonScales" + (i + 1), "iceandfire:armor_dragon_scales" + (i + 1), 44, new int[]{5, 7, 9, 5}, 25, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2);
				EnumBloodedDragonArmor.values()[i].helmet = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 0, EntityEquipmentSlot.HEAD).setTranslationKey("iceandfire.dragonHelmet.iced");
				EnumBloodedDragonArmor.values()[i].chestplate = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 1, EntityEquipmentSlot.CHEST).setTranslationKey("iceandfire.dragonChestplate.iced");
				EnumBloodedDragonArmor.values()[i].leggings = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 2, EntityEquipmentSlot.LEGS).setTranslationKey("iceandfire.dragonLeggings.iced");
				EnumBloodedDragonArmor.values()[i].boots = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 3, EntityEquipmentSlot.FEET).setTranslationKey("iceandfire.dragonBoots.iced");
				EnumBloodedDragonArmor.values()[i].helmet.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_helmet_iced");
				EnumBloodedDragonArmor.values()[i].chestplate.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_chestplate_iced");
				EnumBloodedDragonArmor.values()[i].leggings.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_leggings_iced");
				EnumBloodedDragonArmor.values()[i].boots.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_boots_iced");
			}
			else {
				EnumBloodedDragonArmor.values()[i].armorMaterial = EnumHelper.addArmorMaterial("ShockedDragonScales" + (i + 1), "iceandfire:armor_dragon_scales" + (i + 1), 44, new int[]{5, 7, 9, 5}, 25, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2);
				EnumBloodedDragonArmor.values()[i].helmet = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 0, EntityEquipmentSlot.HEAD).setTranslationKey("iceandfire.dragonHelmet.shocked");
				EnumBloodedDragonArmor.values()[i].chestplate = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 1, EntityEquipmentSlot.CHEST).setTranslationKey("iceandfire.dragonChestplate.shocked");
				EnumBloodedDragonArmor.values()[i].leggings = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 2, EntityEquipmentSlot.LEGS).setTranslationKey("iceandfire.dragonLeggings.shocked");
				EnumBloodedDragonArmor.values()[i].boots = new ItemBloodedArmor(EnumBloodedDragonArmor.values()[i].eggType, EnumBloodedDragonArmor.values()[i], EnumBloodedDragonArmor.values()[i].armorMaterial, 3, EntityEquipmentSlot.FEET).setTranslationKey("iceandfire.dragonBoots.shocked");
				EnumBloodedDragonArmor.values()[i].helmet.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_helmet_shocked");
				EnumBloodedDragonArmor.values()[i].chestplate.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_chestplate_shocked");
				EnumBloodedDragonArmor.values()[i].leggings.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_leggings_shocked");
				EnumBloodedDragonArmor.values()[i].boots.setRegistryName(IceAndFire.MODID, EnumBloodedDragonArmor.values()[i].name() + "_boots_shocked");
			}
		}
	}

	public static Item getScaleItem(EnumBloodedDragonArmor armor) {
		switch (armor) {
			case armor_bronze:
				return ModItems.dragonscales_bronze;
			case armor_green:
				return ModItems.dragonscales_green;
			case armor_gray:
				return ModItems.dragonscales_gray;
			case armor_blue:
				return ModItems.dragonscales_blue;
			case armor_white:
				return ModItems.dragonscales_white;
			case armor_sapphire:
				return ModItems.dragonscales_sapphire;
			case armor_silver:
				return ModItems.dragonscales_silver;
			case armor_electric:
				return ModItems.dragonscales_electric;
			case armor_amethyst:
				return ModItems.dragonscales_amethyst;
			case armor_copper:
				return ModItems.dragonscales_copper;
			case armor_black:
				return ModItems.dragonscales_black;
			default:
				return ModItems.dragonscales_red;
		}
	}
}