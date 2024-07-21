package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.BlockFallingReturningState;
import com.github.alexthe666.iceandfire.block.BlockPath;
import com.github.alexthe666.iceandfire.block.BlockReturningState;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenIceDragonRoost extends WorldGenDragonRoost {

    protected void transformState(World world, BlockPos blockpos, IBlockState state) {
        if (state.getMaterial() == Material.GRASS && state.getBlock() == Blocks.GRASS) {
            world.setBlockState(blockpos, ModBlocks.frozenGrass.getDefaultState().withProperty(BlockReturningState.REVERTS, Boolean.FALSE));
        } else if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT) {
            world.setBlockState(blockpos, ModBlocks.frozenDirt.getDefaultState().withProperty(BlockReturningState.REVERTS, Boolean.FALSE));
        } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL) {
            world.setBlockState(blockpos, ModBlocks.frozenGravel.getDefaultState().withProperty(BlockFallingReturningState.REVERTS, Boolean.FALSE));
        } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getTranslationKey().contains("cobblestone"))) {
            world.setBlockState(blockpos, ModBlocks.frozenCobblestone.getDefaultState().withProperty(BlockReturningState.REVERTS, Boolean.FALSE));
        } else if (state.getMaterial() == Material.ROCK && state.getBlock() != ModBlocks.frozenCobblestone) {
            world.setBlockState(blockpos, ModBlocks.frozenStone.getDefaultState().withProperty(BlockReturningState.REVERTS, Boolean.FALSE));
        } else if (state.getBlock() == Blocks.GRASS_PATH) {
            world.setBlockState(blockpos, ModBlocks.frozenGrassPath.getDefaultState().withProperty(BlockPath.REVERTS, Boolean.FALSE));
        } else if (state.getMaterial() == Material.WOOD) {
            world.setBlockState(blockpos, ModBlocks.frozenSplinters.getDefaultState());
        } else if (state.getMaterial() == Material.PACKED_ICE || state.getMaterial() == Material.CRAFTED_SNOW) {
            world.setBlockState(blockpos, ModBlocks.dragon_ice.getDefaultState());
        } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS || state.getMaterial() == Material.SNOW) {
            world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
        }
    }

    protected IBlockState getPileBlock(Random rand) {
        return ModBlocks.silverPile.getDefaultState();
    }

    protected IBlockState getBuildingBlock() {
        return ModBlocks.frozenCobblestone.getDefaultState().withProperty(BlockReturningState.REVERTS, Boolean.FALSE);
    }

    protected Block[] getDragonTransformedBlocks() {
        return new Block[] {
                ModBlocks.frozenGrass,
                ModBlocks.frozenDirt,
                ModBlocks.frozenGravel,
                ModBlocks.frozenGrassPath,
                ModBlocks.frozenStone,
                ModBlocks.frozenCobblestone,
                ModBlocks.dragon_ice
        };
    }

    protected ResourceLocation getLootTable() {
        return WorldGenIceDragonCave.ICEDRAGON_CHEST;
    }

    protected EntityDragonBase createDragon(World worldIn) {
        return new EntityIceDragon(worldIn);
    }
}

