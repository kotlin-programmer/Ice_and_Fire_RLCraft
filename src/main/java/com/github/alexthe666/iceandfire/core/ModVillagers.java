package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.Maps;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.Map;
import java.util.Random;

public class ModVillagers {
	public static final ModVillagers INSTANCE = new ModVillagers();

	public VillagerRegistry.VillagerProfession fisherman;
	public VillagerRegistry.VillagerProfession craftsman;
	public VillagerRegistry.VillagerProfession shaman;
	public VillagerRegistry.VillagerProfession desertMyrmexWorker;
	public VillagerRegistry.VillagerProfession jungleMyrmexWorker;
	public VillagerRegistry.VillagerProfession desertMyrmexSoldier;
	public VillagerRegistry.VillagerProfession jungleMyrmexSoldier;
	public VillagerRegistry.VillagerProfession desertMyrmexSentinel;
	public VillagerRegistry.VillagerProfession jungleMyrmexSentinel;
	public VillagerRegistry.VillagerProfession desertMyrmexRoyal;
	public VillagerRegistry.VillagerProfession jungleMyrmexRoyal;
	public VillagerRegistry.VillagerProfession desertMyrmexQueen;
	public VillagerRegistry.VillagerProfession jungleMyrmexQueen;
	public Map<Integer, VillagerRegistry.VillagerProfession> professions = Maps.newHashMap();

	public void init() {
		fisherman = ModVillagers.INSTANCE.fisherman;
		craftsman = ModVillagers.INSTANCE.craftsman;
		shaman = ModVillagers.INSTANCE.shaman;
		desertMyrmexWorker = ModVillagers.INSTANCE.desertMyrmexWorker;
		jungleMyrmexWorker = ModVillagers.INSTANCE.jungleMyrmexWorker;
		desertMyrmexSoldier = IafVillagerRegistry.INSTANCE.desertMyrmexSoldier;
		jungleMyrmexSoldier = IafVillagerRegistry.INSTANCE.jungleMyrmexSoldier;
		desertMyrmexSentinel = IafVillagerRegistry.INSTANCE.desertMyrmexSentinel;
		jungleMyrmexSentinel = IafVillagerRegistry.INSTANCE.jungleMyrmexSentinel;
		desertMyrmexRoyal = IafVillagerRegistry.INSTANCE.desertMyrmexRoyal;
		jungleMyrmexRoyal = IafVillagerRegistry.INSTANCE.jungleMyrmexRoyal;
		desertMyrmexQueen = IafVillagerRegistry.INSTANCE.desertMyrmexQueen;
		jungleMyrmexQueen = IafVillagerRegistry.INSTANCE.jungleMyrmexQueen;
		professions = IafVillagerRegistry.INSTANCE.professions;
	}

	public static class SapphireForItems extends IafVillagerRegistry.SapphireForItems {

		public SapphireForItems(Item itemIn, EntityVillager.PriceInfo priceIn) {
			super(itemIn, priceIn);
		}
	}

	public static class ListItemForSapphires extends IafVillagerRegistry.ListItemForSapphires {
		public ListItemForSapphires(Item par1Item, EntityVillager.PriceInfo priceInfo) {
			super(par1Item, priceInfo);
		}

		public ListItemForSapphires(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
			super(stack, priceInfo);
		}
	}
}