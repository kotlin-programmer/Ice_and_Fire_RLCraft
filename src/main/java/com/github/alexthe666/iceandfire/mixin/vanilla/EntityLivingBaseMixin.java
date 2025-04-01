package com.github.alexthe666.iceandfire.mixin.vanilla;

import com.github.alexthe666.iceandfire.block.IDreadBlock;
import com.github.alexthe666.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hacky to use EntityLivingBase and not ModelBase, but some mods like MoBends dynamically replace the models
 */
@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity implements IEntityLivingBaseRenderContext {

	public EntityLivingBaseMixin(World worldIn) {
		super(worldIn);
	}

	@Unique
	private boolean iceAndFire$isRenderingWithGlint = false;
	
	@Override
	public void iceAndFire$setGlintContext(boolean val) {
		this.iceAndFire$isRenderingWithGlint = val;
	}
	
	@Override
	public boolean iceAndFire$getGlintContext() {
		return this.iceAndFire$isRenderingWithGlint;
	}

	@Inject(
			method = "attemptTeleport",
			at = @At("HEAD"),
			cancellable = true
	)
	public void overrideAttemptTeleport(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		BlockPos pos = new BlockPos(x, y, z);
		if (IDreadBlock.isBlockInsideMausoleum(world, pos)) {
			cir.setReturnValue(false);
		}
	}
}