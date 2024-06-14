package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class DragonAIWatchClosest extends EntityAIWatchClosest {

	public DragonAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
		super(entitylivingIn, watchTargetClass, maxDistance);
	}

	@Override
	public boolean shouldExecute() {
		if (isAvailableToWatchClosest()) {
			return super.shouldExecute();
		}
		return false;
	}

	public boolean shouldContinueExecuting() {
		if (isAvailableToWatchClosest()) {
			return super.shouldContinueExecuting();
		}
		return false;
	}

	private boolean isAvailableToWatchClosest() {
		if (!(this.entity instanceof EntityDragonBase)) {
			return false;
		}
		EntityDragonBase dragon = (EntityDragonBase) this.entity;
		if (dragon.getControllingPassenger() != null) {
			return false;
		}
		return dragon.canMove() || dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY;
	}
}
