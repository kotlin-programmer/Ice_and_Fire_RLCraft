package com.github.alexthe666.iceandfire.mixin.vanilla;

import net.minecraft.entity.monster.EntityGuardian;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityGuardian.class)
public interface IEntityGuardianAccessor {
	
	@Accessor("clientSideSpikesAnimation")
	void iceAndFire$setClientSideSpikesAnimation(float val);
	
	@Accessor("clientSideSpikesAnimationO")
	void iceAndFire$setClientSideSpikesAnimation0(float val);
	
	@Accessor("clientSideTailAnimation")
	void iceAndFire$setClientSideTailAnimation(float val);
	
	@Accessor("clientSideTailAnimationO")
	void iceAndFire$setClientSideTailAnimation0(float val);
}