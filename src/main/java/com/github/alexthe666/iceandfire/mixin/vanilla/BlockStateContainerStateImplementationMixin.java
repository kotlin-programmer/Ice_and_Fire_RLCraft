package com.github.alexthe666.iceandfire.mixin.vanilla;

import com.github.alexthe666.iceandfire.entity.util.IPhasesThroughBlock;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockStateContainer.StateImplementation.class)
public abstract class BlockStateContainerStateImplementationMixin {

	@Inject(
			method = "addCollisionBoxToList",
			at = @At("HEAD"),
			cancellable = true
	)
	public void iceandfire_vanillaBlockStateContainerStateImplementation_addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185908_6_, CallbackInfo ci) {
		if(entityIn instanceof IPhasesThroughBlock && ((IPhasesThroughBlock)entityIn).canPhaseThroughBlock((IBlockState)this, worldIn, pos)) {
			ci.cancel();
		}
	}
}