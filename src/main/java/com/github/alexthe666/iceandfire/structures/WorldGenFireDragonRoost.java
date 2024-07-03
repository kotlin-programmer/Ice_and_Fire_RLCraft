package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenFireDragonRoost extends WorldGenDragonRoost {

    protected void transformState(World world, BlockPos blockpos, IBlockState state) {
        if (state.getBlock() instanceof BlockContainer) {
            return;
        }
        if (state.getMaterial() == Material.GRASS && state.getBlock() == Blocks.GRASS) {
            world.setBlockState(blockpos, ModBlocks.charedGrass.getDefaultState());
        } else if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT) {
            world.setBlockState(blockpos, ModBlocks.charedDirt.getDefaultState());
        } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL) {
            world.setBlockState(blockpos, ModBlocks.charedGravel.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getTranslationKey().contains("cobblestone"))) {
            world.setBlockState(blockpos, ModBlocks.charedCobblestone.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && state.getBlock() != ModBlocks.charedCobblestone) {
            world.setBlockState(blockpos, ModBlocks.charedStone.getDefaultState());
        } else if (state.getBlock() == Blocks.GRASS_PATH) {
            world.setBlockState(blockpos, ModBlocks.charedGrassPath.getDefaultState());
        } else if (state.getMaterial() == Material.WOOD) {
            world.setBlockState(blockpos, ModBlocks.ash.getDefaultState());
        } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS) {
            world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
        }
    }

    protected IBlockState getPileBlock(Random rand) {
        return ModBlocks.goldPile.getDefaultState();
    }

    protected IBlockState getBuildingBlock() {
        return ModBlocks.charedCobblestone.getDefaultState();
    }

    protected String getTranslationKeyword() {
        return "chared";
    }

    protected ResourceLocation getLootTable() {
        return WorldGenFireDragonCave.FIREDRAGON_CHEST;
    }

    protected EntityDragonBase createDragon(World worldIn) {
        return new EntityFireDragon(worldIn);
    }
}
