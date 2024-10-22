package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WorldGenLightningDragonRoost extends WorldGenDragonRoost {

    protected void transformState(World world, BlockPos blockpos, IBlockState state) {
        if (state.getMaterial() == Material.GRASS && state.getBlock() == Blocks.GRASS) {
            world.setBlockState(blockpos, ModBlocks.crackledGrass.getDefaultState());
        } else if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT) {
            world.setBlockState(blockpos, ModBlocks.crackledDirt.getDefaultState());
        } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL) {
            world.setBlockState(blockpos, ModBlocks.crackledGravel.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getTranslationKey().contains("cobblestone"))) {
            world.setBlockState(blockpos, ModBlocks.crackledCobblestone.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && state.getBlock() != ModBlocks.crackledCobblestone) {
            world.setBlockState(blockpos, ModBlocks.crackledStone.getDefaultState());
        } else if (state.getBlock() == Blocks.GRASS_PATH) {
            world.setBlockState(blockpos, ModBlocks.crackledGrassPath.getDefaultState());
        } else if (state.getMaterial() == Material.WOOD) {
            world.setBlockState(blockpos, ModBlocks.ash.getDefaultState());
        } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS) {
            world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
        }
    }

    protected IBlockState getPileBlock() {
        if (CompatLoadUtil.isVariedCommoditiesLoaded()) {
            return ModBlocks.diamondPile.getDefaultState();
        }
        return ModBlocks.copperPile.getDefaultState();
    }

    protected IBlockState getBuildingBlock() {
        return ModBlocks.crackledCobblestone.getDefaultState();
    }

    protected Block[] getDragonTransformedBlocks() {
        return new Block[] {
                ModBlocks.crackledGrass,
                ModBlocks.crackledDirt,
                ModBlocks.crackledGravel,
                ModBlocks.crackledGrassPath,
                ModBlocks.crackledStone,
                ModBlocks.crackledCobblestone
        };
    }

    protected ResourceLocation getLootTable() {
        return WorldGenLightningDragonCave.LIGHTNINGDRAGON_CHEST;
    }

    protected EntityDragonBase createDragon(World worldIn) {
        return new EntityLightningDragon(worldIn);
    }
}
