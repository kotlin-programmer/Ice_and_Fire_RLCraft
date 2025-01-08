package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
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

public class WorldGenLightningDragonRoost extends WorldGenDragonRoost {

    protected void transformState(World world, BlockPos blockpos, IBlockState state) {
        if (state.getMaterial() == Material.GRASS && state.getBlock() == Blocks.GRASS) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledGrass.getDefaultState());
        } else if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledDirt.getDefaultState());
        } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledGravel.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getTranslationKey().contains("cobblestone"))) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledCobblestone.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && state.getBlock() != IafBlockRegistry.crackledCobblestone) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledStone.getDefaultState());
        } else if (state.getBlock() == Blocks.GRASS_PATH) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledGrassPath.getDefaultState());
        } else if (state.getMaterial() == Material.WOOD) {
            world.setBlockState(blockpos, IafBlockRegistry.ash.getDefaultState());
        } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS) {
            world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
        }
    }

    protected IBlockState getPileBlock() {
        if (CompatLoadUtil.isVariedCommoditiesLoaded()) {
            return IafBlockRegistry.diamondPile.getDefaultState();
        }
        return IafBlockRegistry.copperPile.getDefaultState();
    }

    protected IBlockState getBuildingBlock() {
        return IafBlockRegistry.crackledCobblestone.getDefaultState();
    }

    protected Block[] getDragonTransformedBlocks() {
        return new Block[] {
                IafBlockRegistry.crackledGrass,
                IafBlockRegistry.crackledDirt,
                IafBlockRegistry.crackledGravel,
                IafBlockRegistry.crackledGrassPath,
                IafBlockRegistry.crackledStone,
                IafBlockRegistry.crackledCobblestone
        };
    }

    protected ResourceLocation getLootTable() {
        return WorldGenLightningDragonCave.LIGHTNINGDRAGON_CHEST;
    }

    protected EntityDragonBase createDragon(World worldIn) {
        return new EntityLightningDragon(worldIn);
    }
}
