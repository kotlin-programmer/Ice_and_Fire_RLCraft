package com.github.alexthe666.iceandfire.mixin.vanilla;

import net.minecraft.entity.monster.EntityGuardian;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityGuardian.class)
public interface IEntityGuardianAccessor {
	
	@Accessor("clientSideSpikesAnimation")
	void setClientSideSpikesAnimation(float val);
	
	@Accessor("clientSideSpikesAnimationO")
	void setClientSideSpikesAnimation0(float val);
	
	@Accessor("clientSideTailAnimation")
	void setClientSideTailAnimation(float val);
	
	@Accessor("clientSideTailAnimationO")
	void setClientSideTailAnimation0(float val);
}