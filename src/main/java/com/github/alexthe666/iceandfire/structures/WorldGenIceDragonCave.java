package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class WorldGenIceDragonCave extends WorldGenDragonCave {
	public static final ResourceLocation ICEDRAGON_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "ice_dragon_female_cave"));
	public static final ResourceLocation ICEDRAGON_MALE_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "ice_dragon_male_cave"));

	protected IBlockState getStone() {
		return IafBlockRegistry.frozenStone.getDefaultState();
	}

	protected IBlockState getCobblestone() {
		return IafBlockRegistry.frozenCobblestone.getDefaultState();
	}

	protected IBlockState getPile() {
		return IafBlockRegistry.silverPile.getDefaultState();
	}

	protected IBlockState getGemstone() {
		return IceAndFireConfig.WORLDGEN.generateSapphireOre ? IafBlockRegistry.sapphireOre.getDefaultState() : Blocks.EMERALD_ORE.getDefaultState();
	}

	protected ResourceLocation getLootTable() {
		if (isMale) {
			return ICEDRAGON_MALE_CHEST;
		}
		else return ICEDRAGON_CHEST;
	}

	protected EntityDragonBase createDragon(World worldIn) {
		return new EntityIceDragon(worldIn);
	}
}
