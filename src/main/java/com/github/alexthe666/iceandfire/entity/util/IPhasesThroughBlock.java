package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPhasesThroughBlock {

    boolean canPhaseThroughBlock(IBlockState state, World world, BlockPos pos);

}
