package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public interface IDreadBlock {
    PropertyBool PLAYER_PLACED = PropertyBool.create("player_placed");

    default boolean isPlayerPlaced(IBlockState state) {
        return state.getValue(PLAYER_PLACED);
    }

    static boolean isIndestructible(IBlockState state) {
        if (state.getBlock() instanceof IDreadBlock) {
            IDreadBlock block = (IDreadBlock) state.getBlock();
            return !block.isPlayerPlaced(state);
        }
        return false;
    }

    static boolean containsIndestructibleBlock(World world, AxisAlignedBB bb) {
        int minX = MathHelper.floor(bb.minX);
        int maxX = MathHelper.ceil(bb.maxX);
        int minY = MathHelper.floor(bb.minY);
        int maxY = MathHelper.ceil(bb.maxY);
        int minZ = MathHelper.floor(bb.minZ);
        int maxZ = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos pooledMutableBlockPos = BlockPos.PooledMutableBlockPos.retain();
        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    pooledMutableBlockPos.setPos(x, y, z);
                    if (world.isBlockLoaded(pooledMutableBlockPos)) {
                        IBlockState state = world.getBlockState(pooledMutableBlockPos);
                        if (isIndestructible(state)) {
                            pooledMutableBlockPos.release();
                            return true;
                        }
                    }
                }
            }
        }
        pooledMutableBlockPos.release();
        return false;
    }

    static boolean isBlockInsideMausoleum(World world, BlockPos pos) {
        boolean pathContainsDreadBlock = false;
        for (BlockPos currentPos = pos; currentPos.getY() < pos.getY() + 20; currentPos = currentPos.up()) {
            IBlockState state = world.getBlockState(currentPos);
            if (IDreadBlock.isIndestructible(state)) {
                pathContainsDreadBlock = true;
                break;
            }
        }
        if (pathContainsDreadBlock) {
            for (BlockPos currentPos = pos; currentPos.getY() > pos.getY() - 20; currentPos = currentPos.down()) {
                IBlockState state = world.getBlockState(currentPos);
                if (IDreadBlock.isIndestructible(state)) {
                    return true;
                }
            }
        }
        return false;
    }
}
