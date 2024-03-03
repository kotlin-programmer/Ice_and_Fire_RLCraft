package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class HippogryphAIAirTarget extends EntityAIBase {
	private EntityHippogryph hippogryph;

	public HippogryphAIAirTarget(EntityHippogryph dragon) {
		this.hippogryph = dragon;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if (!hippogryph.isFlying() && !hippogryph.isHovering()) {
			return false;
		}
		if (hippogryph.isSitting()) {
			return false;
		}
		if (hippogryph.isChild()) {
			return false;
		}
		if (hippogryph.getOwner() != null && hippogryph.getPassengers().contains(hippogryph.getOwner())) {
			return false;
		}
		if (hippogryph.airTarget != null && hippogryph.getDistanceSquared(new Vec3d(hippogryph.airTarget.getX(), hippogryph.posY, hippogryph.airTarget.getZ())) > 3) {
			hippogryph.airTarget = null;
		}

		if (hippogryph.airTarget == null) {
			EntityLivingBase attackTarget = hippogryph.getAttackTarget();
			if (attackTarget != null) {
				BlockPos pos = new BlockPos(attackTarget.posX, attackTarget.posY, attackTarget.posZ);
				if (hippogryph.world.getBlockState(pos).getMaterial() == Material.AIR) {
					hippogryph.airTarget = pos;
					return true;
				}
			}

			BlockPos pos = this.getNearbyAirTarget();
			if (pos == null) {
				return false;
			}
			Vec3d vec = new Vec3d(pos);
			hippogryph.airTarget = new BlockPos(vec.x, vec.y, vec.z);
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!hippogryph.isFlying() && !hippogryph.isHovering()) {
			return false;
		}
		if (hippogryph.isSitting()) {
			return false;
		}
		if (hippogryph.isChild()) {
			return false;
		}
		if (hippogryph.isChild()) {
			return false;
		}
		IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(hippogryph);
		if (capability != null && capability.isStoned()) {
			return false;
		}
		EntityLivingBase attackTarget = hippogryph.getAttackTarget();
		if (attackTarget != null) {
			BlockPos pos = new BlockPos(attackTarget.posX, attackTarget.posY, attackTarget.posZ);
			if (hippogryph.world.getBlockState(pos).getMaterial() == Material.AIR) {
				hippogryph.airTarget = pos;
			} else {
				hippogryph.airTarget = null;
			}
		}
		return hippogryph.airTarget != null;
	}

	public BlockPos getNearbyAirTarget() {
		for (int i = 0; i < 10; i++) {
			BlockPos pos = DragonUtils.getBlockInViewHippogryph(hippogryph);
			if (pos != null && hippogryph.world.getBlockState(pos).getMaterial() == Material.AIR) {
				return pos;
			}
		}
		return null;
	}

}