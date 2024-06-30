package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
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
	public static final ResourceLocation LIGHTNINGDRAGON_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "lightning_dragon_female_cave"));
	public static final ResourceLocation LIGHTNINGDRAGON_MALE_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "lightning_dragon_male_cave"));

	protected IBlockState getStone() {
		return ModBlocks.crackledStone.getDefaultState();
	}

	protected IBlockState getCobblestone() {
		return ModBlocks.crackledCobblestone.getDefaultState();
	}

	protected IBlockState getPile(Random rand) {
		List<IBlockState> piles = new LinkedList<>();
		if (IceAndFireConfig.WORLDGEN.generateCopperOre) {
			piles.add(ModBlocks.copperPile.getDefaultState());
		}
		if (IceAndFireConfig.WORLDGEN.generateSilverOre) {
			piles.add(ModBlocks.silverPile.getDefaultState());
		}
		if (CompatLoadUtil.isVariedCommoditiesLoaded()) {
			piles.add(ModBlocks.diamondPile.getDefaultState());
		}
		piles.add(ModBlocks.goldPile.getDefaultState());
		int index = rand.nextInt(piles.size());
		return piles.get(index);
	}

	protected IBlockState getGemstone() {
		return IceAndFireConfig.WORLDGEN.generateAmethystOre ? ModBlocks.amethystOre.getDefaultState() : Blocks.EMERALD_ORE.getDefaultState();
	}

	protected ResourceLocation getLootTable() {
		if (isMale) {
			return LIGHTNINGDRAGON_MALE_CHEST;
		}
		else return LIGHTNINGDRAGON_CHEST;
	}

	protected EntityDragonBase createDragon(World worldIn) {
		return new EntityLightningDragon(worldIn);
	}
}
