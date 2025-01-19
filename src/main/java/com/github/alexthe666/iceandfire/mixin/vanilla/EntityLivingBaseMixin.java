package com.github.alexthe666.iceandfire.mixin.vanilla;

import com.github.alexthe666.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Hacky to use EntityLivingBase and not ModelBase, but some mods like MoBends dynamically replace the models
 */
@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin implements IEntityLivingBaseRenderContext {
	
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
}