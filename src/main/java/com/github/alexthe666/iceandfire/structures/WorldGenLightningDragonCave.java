package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.block.BlockReturningState;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WorldGenLightningDragonCave extends WorldGenDragonCave {
	public static final ResourceLocation LIGHTNINGDRAGON_CHEST_1 = LootTableList.register(new ResourceLocation("iceandfire", "lightning_dragon_female_cave_1"));
	public static final ResourceLocation LIGHTNINGDRAGON_MALE_CHEST_1 = LootTableList.register(new ResourceLocation("iceandfire", "lightning_dragon_male_cave_1"));
	public static final ResourceLocation LIGHTNINGDRAGON_CHEST_2 = LootTableList.register(new ResourceLocation("iceandfire", "lightning_dragon_female_cave_2"));
	public static final ResourceLocation LIGHTNINGDRAGON_MALE_CHEST_2 = LootTableList.register(new ResourceLocation("iceandfire", "lightning_dragon_male_cave_2"));
	protected IBlockState getStone() {
		return ModBlocks.crackledStone.getDefaultState();
	}

	protected IBlockState getCobblestone() {
		return ModBlocks.crackledCobblestone.getDefaultState();
	}

	protected IBlockState getPile() {
		if (CompatLoadUtil.isVariedCommoditiesLoaded()) {
			return ModBlocks.diamondPile.getDefaultState();
		}
		return ModBlocks.copperPile.getDefaultState();
	}

	protected IBlockState getGemstone() {
		return IceAndFireConfig.WORLDGEN.generateAmethystOre ? ModBlocks.amethystOre.getDefaultState() : Blocks.EMERALD_ORE.getDefaultState();
	}

	protected ResourceLocation getLootTable() {
		if (CompatLoadUtil.isVariedCommoditiesLoaded()) {
			if (isMale) {
				return LIGHTNINGDRAGON_MALE_CHEST_1;
			} else return LIGHTNINGDRAGON_CHEST_1;
		}
		if (isMale) {
			return LIGHTNINGDRAGON_MALE_CHEST_2;
		} else return LIGHTNINGDRAGON_CHEST_2;
	}

	protected EntityDragonBase createDragon(World worldIn) {
		return new EntityLightningDragon(worldIn);
	}
}
