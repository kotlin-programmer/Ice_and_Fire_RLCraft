package com.github.alexthe666.iceandfire.mixin.vanilla;

import com.github.alexthe666.iceandfire.block.IDreadBlock;
import com.github.alexthe666.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
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
	
	@Unique
	@Override
	public void iceAndFire$setGlintContext(boolean val) {
		this.iceAndFire$isRenderingWithGlint = val;
	}
	
	@Unique
	@Override
	public boolean iceAndFire$getGlintContext() {
		return this.iceAndFire$isRenderingWithGlint;
	}
	
	@Unique
	private boolean iceAndFire$stoned = false;
	
	@Unique
	@Override
	public void iceAndFire$setStoned(boolean val) {
		this.iceAndFire$stoned = val;
	}
	
	@Unique
	@Override
	public boolean iceAndFire$getStoned() {
		return this.iceAndFire$stoned;
	}
	
	@Unique
	private int iceAndFire$stonedData = 0;
	
	@Unique
	@Override
	public void iceAndFire$setStonedData(int val) {
		this.iceAndFire$stonedData = val;
	}
	
	@Unique
	@Override
	public int iceAndFire$getStonedData() {
		return this.iceAndFire$stonedData;
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