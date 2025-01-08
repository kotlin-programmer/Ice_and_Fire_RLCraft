package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class WorldGenFireDragonCave extends WorldGenDragonCave {
	public static final ResourceLocation FIREDRAGON_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "fire_dragon_female_cave"));
	public static final ResourceLocation FIREDRAGON_MALE_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "fire_dragon_male_cave"));

	protected IBlockState getStone() {
		return IafBlockRegistry.charedStone.getDefaultState();
	}

	protected IBlockState getCobblestone() {
		return IafBlockRegistry.charedCobblestone.getDefaultState();
	}

	protected IBlockState getPile() {
		return IafBlockRegistry.goldPile.getDefaultState();
	}

	protected IBlockState getGemstone() {
		return Blocks.EMERALD_ORE.getDefaultState();
	}

	protected ResourceLocation getLootTable() {
		if (isMale) {
			return FIREDRAGON_MALE_CHEST;
		}
		else return FIREDRAGON_CHEST;
	}

	protected EntityDragonBase createDragon(World worldIn) {
		return new EntityFireDragon(worldIn);
	}
}
