package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.*;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	public static ArmorMaterial copperMetal = EnumHelper.addArmorMaterial("Copper", "iceandfire:armor_copper_metal", 10, new int[]{1, 3, 4, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0);
	public static ArmorMaterial silverMetal = EnumHelper.addArmorMaterial("Silver", "iceandfire:armor_silver_metal", 15, new int[]{1, 4, 5, 2}, 20, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0);
	public static ArmorMaterial blindfoldArmor = EnumHelper.addArmorMaterial("Blindfold", "iceandfire:blindfold", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ArmorMaterial sheep = EnumHelper.addArmorMaterial("Sheep", "iceandfire:sheep_disguise", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ArmorMaterial myrmexDesert = EnumHelper.addArmorMaterial("MyrmexDesert", "iceandfire:myrmex_desert", 20, new int[]{3, 5, 8, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ArmorMaterial myrmexJungle = EnumHelper.addArmorMaterial("MyrmexJungle", "iceandfire:myrmex_jungle", 20, new int[]{3, 5, 8, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ArmorMaterial earplugsArmor = EnumHelper.addArmorMaterial("Earplugs", "iceandfire:earplugs", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ArmorMaterial yellow_deathworm = EnumHelper.addArmorMaterial("Yellow Deathworm", "iceandfire:yellow_deathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.5F);
	public static ArmorMaterial white_deathworm = EnumHelper.addArmorMaterial("White Deathworm", "iceandfire:white_deathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.5F);
	public static ArmorMaterial red_deathworm = EnumHelper.addArmorMaterial("Red Deathworm", "iceandfire:red_deathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.5F);
	public static ArmorMaterial troll_mountain = EnumHelper.addArmorMaterial("Mountain Troll", "iceandfire:troll_mountain", 20, new int[]{4, 5, 7, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
	public static ArmorMaterial troll_forest = EnumHelper.addArmorMaterial("Forest Troll", "iceandfire:troll_forest", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
	public static ArmorMaterial troll_frost = EnumHelper.addArmorMaterial("Frost Troll", "iceandfire:troll_frost", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
	public static ToolMaterial copperTools = EnumHelper.addToolMaterial("Copper", 2, 190, 5F, 1.5F, 10);
	public static ToolMaterial silverTools = EnumHelper.addToolMaterial("Silver", 2, 460, 11.0F, 1.0F, 18);
	public static ToolMaterial boneTools = EnumHelper.addToolMaterial("Dragonbone", 4, 1660, 10.0F, 4.0F, 22);
	public static ToolMaterial fireBoneTools = EnumHelper.addToolMaterial("FireDragonbone", 4, 2000, 10.0F, 5.5F, 22);
	public static ToolMaterial iceBoneTools = EnumHelper.addToolMaterial("IceDragonbone", 4, 2000, 10.0F, 5.5F, 22);
	public static ToolMaterial lightningBoneTools = EnumHelper.addToolMaterial("LightningDragonbone", 4, 2000, 10.0F, 5.5F, 22);
	public static ToolMaterial trollWeapon = EnumHelper.addToolMaterial("TrollWeapon", 2, 300, 11.0F, 1.0F, 1);
	public static ToolMaterial myrmexChitin = EnumHelper.addToolMaterial("MyrmexChitin", 3, 600, 6.0F, 1.0F, 8);
	public static ToolMaterial ghost_sword_tool_material = EnumHelper.addToolMaterial("GhostSword", 2, 3000, 5, 10.0F, 25);

	public static ToolMaterial dread_sword_tools = EnumHelper.addToolMaterial("DreadSword", 0, 100, 0.0F, 1F, 0);
	public static ToolMaterial dread_knight_sword_tools = EnumHelper.addToolMaterial("DreadKnightSword", 0, 1200, 0.0F, 13F, 10);

	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":bestiary")
	public static Item bestiary = new ItemBestiary();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":manuscript")
	public static Item manuscript = new ItemGeneric("manuscript", "iceandfire.manuscript");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":amethyst_gem")
	public static Item amethystGem = new ItemGeneric("amethyst_gem", "iceandfire.amethystGem");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sapphire_gem")
	public static Item sapphireGem = new ItemGeneric("sapphire_gem", "iceandfire.sapphireGem");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":bronze_alloy")
	public static Item bronzeAlloy = new ItemGeneric("bronze_alloy", "iceandfire.bronzeAlloy");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_ingot")
	public static Item copperIngot = new ItemGeneric("copper_ingot", "iceandfire.copperIngot");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_nugget")
	public static Item copperNugget = new ItemGeneric("copper_nugget", "iceandfire.copperNugget");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_helmet")
	public static Item copper_helmet = new ItemCopperArmor(copperMetal, 0, EntityEquipmentSlot.HEAD);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_chestplate")
	public static Item copper_chestplate = new ItemCopperArmor(copperMetal, 1, EntityEquipmentSlot.CHEST);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_leggings")
	public static Item copper_leggings = new ItemCopperArmor(copperMetal, 2, EntityEquipmentSlot.LEGS);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_boots")
	public static Item copper_boots = new ItemCopperArmor(copperMetal, 3, EntityEquipmentSlot.FEET);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_sword")
	public static Item copper_sword = new ItemModSword(copperTools, "copper_sword", "iceandfire.copper_sword");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_shovel")
	public static Item copper_shovel = new ItemModShovel(copperTools, "copper_shovel", "iceandfire.copper_shovel");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_pickaxe")
	public static Item copper_pickaxe = new ItemModPickaxe(copperTools, "copper_pickaxe", "iceandfire.copper_pickaxe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_axe")
	public static Item copper_axe = new ItemModAxe(copperTools, "copper_axe", "iceandfire.copper_axe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":copper_hoe")
	public static Item copper_hoe = new ItemModHoe(copperTools, "copper_hoe", "iceandfire.copper_hoe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_ingot")
	public static Item silverIngot = new ItemGeneric("silver_ingot", "iceandfire.silverIngot");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_nugget")
	public static Item silverNugget = new ItemGeneric("silver_nugget", "iceandfire.silverNugget");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_helmet")
	public static Item silver_helmet = new ItemSilverArmor(silverMetal, 0, EntityEquipmentSlot.HEAD);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_chestplate")
	public static Item silver_chestplate = new ItemSilverArmor(silverMetal, 1, EntityEquipmentSlot.CHEST);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_leggings")
	public static Item silver_leggings = new ItemSilverArmor(silverMetal, 2, EntityEquipmentSlot.LEGS);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_boots")
	public static Item silver_boots = new ItemSilverArmor(silverMetal, 3, EntityEquipmentSlot.FEET);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_sword")
	public static Item silver_sword = new ItemModSword(silverTools, "silver_sword", "iceandfire.silver_sword");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_shovel")
	public static Item silver_shovel = new ItemModShovel(silverTools, "silver_shovel", "iceandfire.silver_shovel");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_pickaxe")
	public static Item silver_pickaxe = new ItemModPickaxe(silverTools, "silver_pickaxe", "iceandfire.silver_pickaxe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_axe")
	public static Item silver_axe = new ItemModAxe(silverTools, "silver_axe", "iceandfire.silver_axe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_hoe")
	public static Item silver_hoe = new ItemModHoe(silverTools, "silver_hoe", "iceandfire.silver_hoe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_stew")
	public static Item fire_stew = new ItemGeneric("fire_stew", "iceandfire.fire_stew");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frost_stew")
	public static Item frost_stew = new ItemGeneric("frost_stew", "iceandfire.frost_stew");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":lightning_stew")
	public static Item lightning_stew = new ItemGeneric("lightning_stew", "iceandfire.lightning_stew");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_red")
	public static Item dragonegg_red = new ItemDragonEgg("dragonegg_red", EnumDragonEgg.RED);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_green")
	public static Item dragonegg_green = new ItemDragonEgg("dragonegg_green", EnumDragonEgg.GREEN);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_bronze")
	public static Item dragonegg_bronze = new ItemDragonEgg("dragonegg_bronze", EnumDragonEgg.BRONZE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_gray")
	public static Item dragonegg_gray = new ItemDragonEgg("dragonegg_gray", EnumDragonEgg.GRAY);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_blue")
	public static Item dragonegg_blue = new ItemDragonEgg("dragonegg_blue", EnumDragonEgg.BLUE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_white")
	public static Item dragonegg_white = new ItemDragonEgg("dragonegg_white", EnumDragonEgg.WHITE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_sapphire")
	public static Item dragonegg_sapphire = new ItemDragonEgg("dragonegg_sapphire", EnumDragonEgg.SAPPHIRE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_silver")
	public static Item dragonegg_silver = new ItemDragonEgg("dragonegg_silver", EnumDragonEgg.SILVER);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_electric")
	public static Item dragonegg_electric = new ItemDragonEgg("dragonegg_electric", EnumDragonEgg.ELECTRIC);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_amethyst")
	public static Item dragonegg_amethyst = new ItemDragonEgg("dragonegg_amethyst", EnumDragonEgg.AMETHYST);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_copper")
	public static Item dragonegg_copper = new ItemDragonEgg("dragonegg_copper", EnumDragonEgg.COPPER);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_black")
	public static Item dragonegg_black = new ItemDragonEgg("dragonegg_black", EnumDragonEgg.BLACK);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_red")
	public static Item dragonscales_red = new ItemDragonScales("dragonscales_red", EnumDragonEgg.RED);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_green")
	public static Item dragonscales_green = new ItemDragonScales("dragonscales_green", EnumDragonEgg.GREEN);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_bronze")
	public static Item dragonscales_bronze = new ItemDragonScales("dragonscales_bronze", EnumDragonEgg.BRONZE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_gray")
	public static Item dragonscales_gray = new ItemDragonScales("dragonscales_gray", EnumDragonEgg.GRAY);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_blue")
	public static Item dragonscales_blue = new ItemDragonScales("dragonscales_blue", EnumDragonEgg.BLUE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_white")
	public static Item dragonscales_white = new ItemDragonScales("dragonscales_white", EnumDragonEgg.WHITE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_sapphire")
	public static Item dragonscales_sapphire = new ItemDragonScales("dragonscales_sapphire", EnumDragonEgg.SAPPHIRE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_silver")
	public static Item dragonscales_silver = new ItemDragonScales("dragonscales_silver", EnumDragonEgg.SILVER);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_electric")
	public static Item dragonscales_electric = new ItemDragonScales("dragonscales_electric", EnumDragonEgg.ELECTRIC);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_amethyst")
	public static Item dragonscales_amethyst = new ItemDragonScales("dragonscales_amethyst", EnumDragonEgg.AMETHYST);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_copper")
	public static Item dragonscales_copper = new ItemDragonScales("dragonscales_copper", EnumDragonEgg.COPPER);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_black")
	public static Item dragonscales_black = new ItemDragonScales("dragonscales_black", EnumDragonEgg.BLACK);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone")
	public static Item dragonbone = new ItemDragonBone();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":witherbone")
	public static Item witherbone = new ItemGeneric("witherbone", "iceandfire.witherbone");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fishing_spear")
	public static Item fishing_spear = new ItemFishingSpear();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":wither_shard")
	public static Item wither_shard = new ItemGeneric("wither_shard", "iceandfire.wither_shard");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_sword")
	public static Item dragonbone_sword = new ItemModSword(boneTools, "dragonbone_sword", "iceandfire.dragonbone_sword");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_shovel")
	public static Item dragonbone_shovel = new ItemModShovel(boneTools, "dragonbone_shovel", "iceandfire.dragonbone_shovel");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_pickaxe")
	public static Item dragonbone_pickaxe = new ItemModPickaxe(boneTools, "dragonbone_pickaxe", "iceandfire.dragonbone_pickaxe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_axe")
	public static Item dragonbone_axe = new ItemModAxe(boneTools, "dragonbone_axe", "iceandfire.dragonbone_axe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_hoe")
	public static Item dragonbone_hoe = new ItemModHoe(boneTools, "dragonbone_hoe", "iceandfire.dragonbone_hoe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_sword_fire")
	public static Item dragonbone_sword_fire = new ItemAlchemySword(fireBoneTools, "dragonbone_sword_fire", "iceandfire.dragonbone_sword_fire");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_sword_ice")
	public static Item dragonbone_sword_ice = new ItemAlchemySword(iceBoneTools, "dragonbone_sword_ice", "iceandfire.dragonbone_sword_ice");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_sword_lightning")
	public static Item dragonbone_sword_lightning = new ItemAlchemySword(lightningBoneTools, "dragonbone_sword_lightning", "iceandfire.dragonbone_sword_lightning");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_arrow")
	public static Item dragonbone_arrow = new ItemGeneric("dragonbone_arrow", "iceandfire.dragonbone_arrow");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_bow")
	public static Item dragonbone_bow = new ItemDragonBow();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_skull")
	public static Item dragon_skull = new ItemDragonSkull();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonarmor_iron")
	public static Item dragon_armor_iron = new ItemDragonArmor(ItemDragonArmor.DragonArmorType.IRON, "dragonarmor_iron");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonarmor_gold")
	public static Item dragon_armor_gold = new ItemDragonArmor(ItemDragonArmor.DragonArmorType.GOLD, "dragonarmor_gold");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonarmor_diamond")
	public static Item dragon_armor_diamond = new ItemDragonArmor(ItemDragonArmor.DragonArmorType.DIAMOND, "dragonarmor_diamond");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonarmor_silver")
	public static Item dragon_armor_silver = new ItemDragonArmor(ItemDragonArmor.DragonArmorType.SILVER, "dragonarmor_silver");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonarmor_copper")
	public static Item dragon_armor_copper = new ItemDragonArmor(ItemDragonArmor.DragonArmorType.COPPER, "dragonarmor_copper");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_meal")
	public static Item dragon_meal = new ItemGeneric("dragon_meal", "iceandfire.dragon_meal");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sickly_dragon_meal")
	public static Item sickly_dragon_meal = new ItemGenericDesc("sickly_dragon_meal", "iceandfire.sickly_dragon_meal");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_dragon_flesh")
	public static Item fire_dragon_flesh = new ItemDragonFlesh(0);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ice_dragon_flesh")
	public static Item ice_dragon_flesh = new ItemDragonFlesh(1);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":lightning_dragon_flesh")
	public static Item lightning_dragon_flesh = new ItemDragonFlesh(2);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_dragon_heart")
	public static Item fire_dragon_heart = new ItemGeneric("fire_dragon_heart", "iceandfire.fire_dragon_heart");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ice_dragon_heart")
	public static Item ice_dragon_heart = new ItemGeneric("ice_dragon_heart", "iceandfire.ice_dragon_heart");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":lightning_dragon_heart")
	public static Item lightning_dragon_heart = new ItemGeneric("lightning_dragon_heart", "iceandfire.lightning_dragon_heart");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_dragon_blood")
	public static Item fire_dragon_blood = new ItemGeneric("fire_dragon_blood", "iceandfire.fire_dragon_blood");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ice_dragon_blood")
	public static Item ice_dragon_blood = new ItemGeneric("ice_dragon_blood", "iceandfire.ice_dragon_blood");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":lightning_dragon_blood")
	public static Item lightning_dragon_blood = new ItemGeneric("lightning_dragon_blood", "iceandfire.lightning_dragon_blood");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_stick")
	public static Item dragon_stick = new ItemDragonStaff();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_horn")
	public static Item dragon_horn = new ItemDragonHornStatic();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_horn_fire")
	public static Item dragon_horn_fire = new ItemDragonHornActive("dragon_horn_fire");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_horn_ice")
	public static Item dragon_horn_ice = new ItemDragonHornActive("dragon_horn_ice");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_horn_lightning")
	public static Item dragon_horn_lightning = new ItemDragonHornActive("dragon_horn_lightning");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_flute")
	public static Item dragon_flute = new ItemDragonFlute();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":hippogryph_egg")
	public static Item hippogryph_egg = new ItemHippogryphEgg();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":iron_hippogryph_armor")
	public static Item iron_hippogryph_armor = new ItemGeneric("iron_hippogryph_armor", "iceandfire.iron_hippogryph_armor").setMaxStackSize(1);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":gold_hippogryph_armor")
	public static Item gold_hippogryph_armor = new ItemGeneric("gold_hippogryph_armor", "iceandfire.gold_hippogryph_armor").setMaxStackSize(1);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":diamond_hippogryph_armor")
	public static Item diamond_hippogryph_armor = new ItemGeneric("diamond_hippogryph_armor", "iceandfire.diamond_hippogryph_armor").setMaxStackSize(1);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":gorgon_head")
	public static Item gorgon_head = new ItemGorgonHead();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":stone_statue")
	public static Item stone_statue = new ItemStoneStatue();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":blindfold")
	public static Item blindfold = new ItemBlindfold();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":pixie_dust")
	public static Item pixie_dust = new ItemPixieDust();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ambrosia")
	public static Item ambrosia = new ItemAmbrosia();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sheep_helmet")
	public static Item sheep_helmet = new ItemModArmor(sheep, 0, EntityEquipmentSlot.HEAD, "sheep_helmet", "iceandfire.sheep_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sheep_chestplate")
	public static Item sheep_chestplate = new ItemModArmor(sheep, 1, EntityEquipmentSlot.CHEST, "sheep_chestplate", "iceandfire.sheep_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sheep_leggings")
	public static Item sheep_leggings = new ItemModArmor(sheep, 2, EntityEquipmentSlot.LEGS, "sheep_leggings", "iceandfire.sheep_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sheep_boots")
	public static Item sheep_boots = new ItemModArmor(sheep, 3, EntityEquipmentSlot.FEET, "sheep_boots", "iceandfire.sheep_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":shiny_scales")
	public static Item shiny_scales = new ItemGeneric("shiny_scales", "iceandfire.shiny_scales");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":earplugs")
	public static Item earplugs = new ItemModArmor(earplugsArmor, 0, EntityEquipmentSlot.HEAD, "earplugs", "iceandfire.earplugs");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":troll_leather_forest")
	public static Item troll_leather_forest = new ItemTrollLeather(EnumTroll.FOREST, "troll_leather_forest", "iceandfire.troll_leather_forest");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":troll_leather_frost")
	public static Item troll_leather_frost = new ItemTrollLeather(EnumTroll.FROST, "troll_leather_frost", "iceandfire.troll_leather_frost");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":troll_leather_mountain")
	public static Item troll_leather_mountain = new ItemTrollLeather(EnumTroll.MOUNTAIN, "troll_leather_mountain", "iceandfire.troll_leather_mountain");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_chitin")
	public static Item deathworm_chitin = new ItemDeathWormChitin();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_yellow_helmet")
	public static Item deathworm_yellow_helmet = new ItemDeathwormArmor(yellow_deathworm, 0, EntityEquipmentSlot.HEAD, "deathworm_yellow_helmet", "iceandfire.deathworm_yellow_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_yellow_chestplate")
	public static Item deathworm_yellow_chestplate = new ItemDeathwormArmor(yellow_deathworm, 1, EntityEquipmentSlot.CHEST, "deathworm_yellow_chestplate", "iceandfire.deathworm_yellow_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_yellow_leggings")
	public static Item deathworm_yellow_leggings = new ItemDeathwormArmor(yellow_deathworm, 2, EntityEquipmentSlot.LEGS, "deathworm_yellow_leggings", "iceandfire.deathworm_yellow_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_yellow_boots")
	public static Item deathworm_yellow_boots = new ItemDeathwormArmor(yellow_deathworm, 3, EntityEquipmentSlot.FEET, "deathworm_yellow_boots", "iceandfire.deathworm_yellow_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_white_helmet")
	public static Item deathworm_white_helmet = new ItemDeathwormArmor(white_deathworm, 0, EntityEquipmentSlot.HEAD, "deathworm_white_helmet", "iceandfire.deathworm_white_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_white_chestplate")
	public static Item deathworm_white_chestplate = new ItemDeathwormArmor(white_deathworm, 1, EntityEquipmentSlot.CHEST, "deathworm_white_chestplate", "iceandfire.deathworm_white_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_white_leggings")
	public static Item deathworm_white_leggings = new ItemDeathwormArmor(white_deathworm, 2, EntityEquipmentSlot.LEGS, "deathworm_white_leggings", "iceandfire.deathworm_white_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_white_boots")
	public static Item deathworm_white_boots = new ItemDeathwormArmor(white_deathworm, 3, EntityEquipmentSlot.FEET, "deathworm_white_boots", "iceandfire.deathworm_white_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_red_helmet")
	public static Item deathworm_red_helmet = new ItemDeathwormArmor(red_deathworm, 0, EntityEquipmentSlot.HEAD, "deathworm_red_helmet", "iceandfire.deathworm_red_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_red_chworm_whhestplate")
	public static Item deathworm_red_chestplate = new ItemDeathwormArmor(red_deathworm, 1, EntityEquipmentSlot.CHEST, "deathworm_red_chestplate", "iceandfire.deathworm_red_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_red_leggings")
	public static Item deathworm_red_leggings = new ItemDeathwormArmor(red_deathworm, 2, EntityEquipmentSlot.LEGS, "deathworm_red_leggings", "iceandfire.deathworm_red_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_red_boots")
	public static Item deathworm_red_boots = new ItemDeathwormArmor(red_deathworm, 3, EntityEquipmentSlot.FEET, "deathworm_red_boots", "iceandfire.deathworm_red_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_egg")
	public static Item deathworm_egg = new ItemDeathwormEgg();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":rotten_egg")
	public static Item rotten_egg = new ItemRottenEgg();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":stymphalian_bird_feather")
	public static Item stymphalian_bird_feather = new ItemGeneric("stymphalian_bird_feather", "iceandfire.stymphalian_bird_feather");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":stymphalian_arrow")
	public static Item stymphalian_arrow = new ItemStymphalianArrow();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":troll_tusk")
	public static Item troll_tusk = new ItemGeneric("troll_tusk", "iceandfire.troll_tusk");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_egg")
	public static Item myrmex_desert_egg = new ItemMyrmexEgg(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_egg")
	public static Item myrmex_jungle_egg = new ItemMyrmexEgg(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_resin")
	public static Item myrmex_desert_resin = new ItemGeneric("myrmex_desert_resin", "iceandfire.myrmex_desert_resin");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_resin")
	public static Item myrmex_jungle_resin = new ItemGeneric("myrmex_jungle_resin", "iceandfire.myrmex_jungle_resin");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_chitin")
	public static Item myrmex_desert_chitin = new ItemGeneric("myrmex_desert_chitin", "iceandfire.myrmex_desert_chitin");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_chitin")
	public static Item myrmex_jungle_chitin = new ItemGeneric("myrmex_jungle_chitin", "iceandfire.myrmex_jungle_chitin");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_stinger")
	public static Item myrmex_stinger = new ItemGeneric("myrmex_stinger", "iceandfire.myrmex_stinger");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_sword")
	public static Item myrmex_desert_sword = new ItemModSword(myrmexChitin, "myrmex_desert_sword", "iceandfire.myrmex_desert_sword");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_sword_venom")
	public static Item myrmex_desert_sword_venom = new ItemModSword(myrmexChitin, "myrmex_desert_sword_venom", "iceandfire.myrmex_desert_sword_venom");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_shovel")
	public static Item myrmex_desert_shovel = new ItemModShovel(myrmexChitin, "myrmex_desert_shovel", "iceandfire.myrmex_desert_shovel");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_pickaxe")
	public static Item myrmex_desert_pickaxe = new ItemModPickaxe(myrmexChitin, "myrmex_desert_pickaxe", "iceandfire.myrmex_desert_pickaxe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_axe")
	public static Item myrmex_desert_axe = new ItemModAxe(myrmexChitin, "myrmex_desert_axe", "iceandfire.myrmex_desert_axe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_hoe")
	public static Item myrmex_desert_hoe = new ItemModHoe(myrmexChitin, "myrmex_desert_hoe", "iceandfire.myrmex_desert_hoe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_sword")
	public static Item myrmex_jungle_sword = new ItemModSword(myrmexChitin, "myrmex_jungle_sword", "iceandfire.myrmex_jungle_sword");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_sword_venom")
	public static Item myrmex_jungle_sword_venom = new ItemModSword(myrmexChitin, "myrmex_jungle_sword_venom", "iceandfire.myrmex_jungle_sword_venom");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_shovel")
	public static Item myrmex_jungle_shovel = new ItemModShovel(myrmexChitin, "myrmex_jungle_shovel", "iceandfire.myrmex_jungle_shovel");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_pickaxe")
	public static Item myrmex_jungle_pickaxe = new ItemModPickaxe(myrmexChitin, "myrmex_jungle_pickaxe", "iceandfire.myrmex_jungle_pickaxe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_axe")
	public static Item myrmex_jungle_axe = new ItemModAxe(myrmexChitin, "myrmex_jungle_axe", "iceandfire.myrmex_jungle_axe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_hoe")
	public static Item myrmex_jungle_hoe = new ItemModHoe(myrmexChitin, "myrmex_jungle_hoe", "iceandfire.myrmex_jungle_hoe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_staff")
	public static Item myrmex_desert_staff = new ItemMyrmexStaff(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_staff")
	public static Item myrmex_jungle_staff = new ItemMyrmexStaff(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_helmet")
	public static Item myrmex_desert_helmet = new ItemModArmor(myrmexDesert, 0, EntityEquipmentSlot.HEAD, "myrmex_desert_helmet", "iceandfire.myrmex_desert_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_chestplate")
	public static Item myrmex_desert_chestplate = new ItemModArmor(myrmexDesert, 1, EntityEquipmentSlot.CHEST, "myrmex_desert_chestplate", "iceandfire.myrmex_desert_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_leggings")
	public static Item myrmex_desert_leggings = new ItemModArmor(myrmexDesert, 2, EntityEquipmentSlot.LEGS, "myrmex_desert_leggings", "iceandfire.myrmex_desert_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_boots")
	public static Item myrmex_desert_boots = new ItemModArmor(myrmexDesert, 3, EntityEquipmentSlot.FEET, "myrmex_desert_boots", "iceandfire.myrmex_desert_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_helmet")
	public static Item myrmex_jungle_helmet = new ItemModArmor(myrmexJungle, 0, EntityEquipmentSlot.HEAD, "myrmex_jungle_helmet", "iceandfire.myrmex_jungle_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_chestplate")
	public static Item myrmex_jungle_chestplate = new ItemModArmor(myrmexJungle, 1, EntityEquipmentSlot.CHEST, "myrmex_jungle_chestplate", "iceandfire.myrmex_jungle_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_leggings")
	public static Item myrmex_jungle_leggings = new ItemModArmor(myrmexJungle, 2, EntityEquipmentSlot.LEGS, "myrmex_jungle_leggings", "iceandfire.myrmex_jungle_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_boots")
	public static Item myrmex_jungle_boots = new ItemModArmor(myrmexJungle, 3, EntityEquipmentSlot.FEET, "myrmex_jungle_boots", "iceandfire.myrmex_jungle_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":amphithere_feather")
	public static Item amphithere_feather = new ItemGeneric("amphithere_feather", "iceandfire.amphithere_feather");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":amphithere_arrow")
	public static Item amphithere_arrow = new ItemAmphithereArrow();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sea_serpent_fang")
	public static Item sea_serpent_fang = new ItemGeneric("sea_serpent_fang", "iceandfire.sea_serpent_fang");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sea_serpent_arrow")
	public static Item sea_serpent_arrow = new ItemSeaSerpentArrow();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":hydra_fang")
	public static Item hydra_fang = new ItemGeneric("hydra_fang", "iceandfire.hydra_fang");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":hydra_heart")
	public static Item hydra_heart = new ItemHydraHeart();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":hydra_arrow")
	public static Item hydra_arrow = new ItemHydraArrow();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":tide_trident")
	public static Item tide_trident = new ItemTideTrident();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":lich_staff")
	public static Item lich_staff = new ItemLichStaff();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dread_shard")
	public static Item dread_shard = new ItemGeneric("dread_shard", "iceandfire.dread_shard", 0);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dread_sword")
	public static Item dread_sword = new ItemModSword(dread_sword_tools, "dread_sword", "iceandfire.dread_sword");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dread_knight_sword")
	public static Item dread_knight_sword = new ItemModSword(dread_knight_sword_tools, "dread_knight_sword", "iceandfire.dread_knight_sword");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ghost_ingot")
	public static Item ghost_ingot = new ItemGeneric("ghost_ingot", "iceandfire.ghost_ingot");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ghost_sword")
	public static Item ghost_sword = new ItemGhostSword();

	static {
		EnumDragonArmor.initArmors();
		EnumSeaSerpent.initArmors();
	}
}