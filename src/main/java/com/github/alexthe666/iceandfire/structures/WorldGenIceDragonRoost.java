package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenIceDragonRoost extends WorldGenDragonRoost {

    protected void transformState(World world, BlockPos blockpos, IBlockState state) {
        if (state.getMaterial() == Material.GRASS && state.getBlock() == Blocks.GRASS) {
            world.setBlockState(blockpos, IafBlockRegistry.frozenGrass.getDefaultState());
        } else if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT) {
            world.setBlockState(blockpos, IafBlockRegistry.frozenDirt.getDefaultState());
        } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL) {
            world.setBlockState(blockpos, IafBlockRegistry.frozenGravel.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getTranslationKey().contains("cobblestone"))) {
            world.setBlockState(blockpos, IafBlockRegistry.frozenCobblestone.getDefaultState());
        } else if (state.getMaterial() == Material.ROCK && state.getBlock() != IafBlockRegistry.frozenCobblestone) {
            world.setBlockState(blockpos, IafBlockRegistry.frozenStone.getDefaultState());
        } else if (state.getBlock() == Blocks.GRASS_PATH) {
            world.setBlockState(blockpos, IafBlockRegistry.frozenGrassPath.getDefaultState());
        } else if (state.getMaterial() == Material.WOOD) {
            world.setBlockState(blockpos, IafBlockRegistry.frozenSplinters.getDefaultState());
        } else if (state.getMaterial() == Material.PACKED_ICE || state.getMaterial() == Material.CRAFTED_SNOW) {
            world.setBlockState(blockpos, IafBlockRegistry.dragon_ice.getDefaultState());
        } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS || state.getMaterial() == Material.SNOW) {
            world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
        }
    }

    protected IBlockState getPileBlock() {
        return IafBlockRegistry.silverPile.getDefaultState();
    }

    protected IBlockState getBuildingBlock() {
        return IafBlockRegistry.frozenCobblestone.getDefaultState();
    }

    protected Block[] getDragonTransformedBlocks() {
        return new Block[] {
                IafBlockRegistry.frozenGrass,
                IafBlockRegistry.frozenDirt,
                IafBlockRegistry.frozenGravel,
                IafBlockRegistry.frozenGrassPath,
                IafBlockRegistry.frozenStone,
                IafBlockRegistry.frozenCobblestone,
                IafBlockRegistry.dragon_ice
        };
    }

    protected ResourceLocation getLootTable() {
        return WorldGenIceDragonCave.ICEDRAGON_CHEST;
    }

    protected EntityDragonBase createDragon(World worldIn) {
        return new EntityIceDragon(worldIn);
    }
}

