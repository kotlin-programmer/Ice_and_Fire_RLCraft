package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenFireDragonRoost extends WorldGenDragonRoost {

    protected void transformState(World world, BlockPos blockpos, IBlockState state) {
        if (state.getBlock() instanceof BlockContainer) {
            return;
        }
        if (state.getMaterial() == Material.GRASS && state.getBlock() == Blocks.GRASS) {
            world.setBlockState(blockpos, IafBlockRegistry.charedGrass.getDefaultState());
        } else if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT) {
            world.setBlockState(blockpos, IafBlockRegistry.charedDirt.getDefaultState());
        } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL) {
            world.setBlockState(blockpos, IafBlockRegistry.charedGravel.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getTranslationKey().contains("cobblestone"))) {
            world.setBlockState(blockpos, IafBlockRegistry.charedCobblestone.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && state.getBlock() != IafBlockRegistry.charedCobblestone) {
            world.setBlockState(blockpos, IafBlockRegistry.charedStone.getDefaultState());
        } else if (state.getBlock() == Blocks.GRASS_PATH) {
            world.setBlockState(blockpos, IafBlockRegistry.charedGrassPath.getDefaultState());
        } else if (state.getMaterial() == Material.WOOD) {
            world.setBlockState(blockpos, IafBlockRegistry.ash.getDefaultState());
        } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS) {
            world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
        }
    }

    protected IBlockState getPileBlock() {
        return IafBlockRegistry.goldPile.getDefaultState();
    }

    protected IBlockState getBuildingBlock() {
        return IafBlockRegistry.charedCobblestone.getDefaultState();
    }

    protected Block[] getDragonTransformedBlocks() {
        return new Block[] {
                IafBlockRegistry.charedGrass,
                IafBlockRegistry.charedDirt,
                IafBlockRegistry.charedGravel,
                IafBlockRegistry.charedGrassPath,
                IafBlockRegistry.charedStone,
                IafBlockRegistry.charedCobblestone
        };
    }

    protected ResourceLocation getLootTable() {
        return WorldGenFireDragonCave.FIREDRAGON_CHEST;
    }

    protected EntityDragonBase createDragon(World worldIn) {
        return new EntityFireDragon(worldIn);
    }
}
