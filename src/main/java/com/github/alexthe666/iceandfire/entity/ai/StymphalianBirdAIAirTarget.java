package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class StymphalianBirdAIAirTarget extends EntityAIBase {
    private EntityStymphalianBird bird;

    public StymphalianBirdAIAirTarget(EntityStymphalianBird bird) {
        this.bird = bird;
    }

    @Override
    public boolean shouldExecute() {
        if (!bird.isFlying()) {
            return false;
        }
        if (bird.isChild()) {
            return false;
        }
        if (bird.doesWantToLand()) {
            return false;
        }
        if (bird.airTarget != null && (bird.isTargetBlocked(new Vec3d(bird.airTarget)))) {
            bird.airTarget = null;
        }

        if (bird.airTarget == null) {
            EntityLivingBase attackTarget = bird.getAttackTarget();
            if (attackTarget != null) {
                bird.airTarget = new BlockPos(attackTarget.posX, attackTarget.posY + attackTarget.getEyeHeight(), attackTarget.posZ);
                return true;
            }
            BlockPos pos = getNearbyAirTarget(bird);
            if (pos == null) {
                return false;
            }
            Vec3d vec = new Vec3d(pos);
            bird.airTarget = new BlockPos(vec.x, vec.y, vec.z);
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!bird.isFlying()) {
            return false;
        }
        if (bird.isChild()) {
            return false;
        }
        IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(bird);
        if (capability != null && capability.isStoned()) {
            return false;
        }
        EntityLivingBase attackTarget = bird.getAttackTarget();
        if (attackTarget != null) {
            bird.airTarget = new BlockPos(attackTarget.posX, attackTarget.posY + attackTarget.getEyeHeight(), attackTarget.posZ);
        }
        return bird.airTarget != null;
    }

    public static BlockPos getNearbyAirTarget(EntityStymphalianBird bird) {
        if (bird.getAttackTarget() == null) {
            BlockPos pos = DragonUtils.getBlockInViewStymphalian(bird);
            if (pos != null && bird.world.getBlockState(pos).getMaterial() == Material.AIR) {
                return pos;
            }
            if (bird.flock != null && bird.flock.isLeader(bird)) {
                bird.flock.setTarget(bird.airTarget);
            }
        }
        return null;
    }
}