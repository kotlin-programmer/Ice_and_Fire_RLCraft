package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;

public class GhostAICharge extends EntityAIBase {

    private EntityGhost ghost;
    public boolean firstPhase = true;
    public Vec3d moveToPos = null;
    public Vec3d offsetOf = Vec3d.ZERO;

    public GhostAICharge(EntityGhost ghost) {
        this.setMutexBits(Constants.AiMutexBits.MOVE);
        this.ghost = ghost;
    }

    @Override
    public boolean shouldExecute() {
        return ghost.getAttackTarget() != null && !ghost.isCharging();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return ghost.getAttackTarget() != null && !ghost.getAttackTarget().isDead;
    }

    @Override
    public void startExecuting() {
        ghost.setCharging(true);
    }

    @Override
    public void resetTask() {
        firstPhase = true;
        this.moveToPos = null;
        ghost.setCharging(false);
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = ghost.getAttackTarget();
        if (target != null) {
            if (this.ghost.getAnimation() == IAnimatedEntity.NO_ANIMATION && this.ghost.getDistance(target) < 1.4D) {
                this.ghost.setAnimation(EntityGhost.ANIMATION_HIT);
            }
            if (firstPhase) {
                if (this.moveToPos == null) {
                    BlockPos moveToPos = DragonUtils.getBlockInTargetsViewGhost(ghost, target);
                    this.moveToPos = new Vec3d(moveToPos);
                } else {
                    this.ghost.getNavigator().tryMoveToXYZ(this.moveToPos.x + 0.5D, this.moveToPos.y + 0.5D,
                            this.moveToPos.z + 0.5D, 1F);
                    if (this.ghost.getDistanceSq(new BlockPos(this.moveToPos.add(0.5D, 0.5D, 0.5D))) < 9D) {
                        if (this.ghost.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                            this.ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                        }
                        this.firstPhase = false;
                        this.moveToPos = null;
                        offsetOf = target.getPositionVector().subtract(this.ghost.getPositionVector()).normalize();
                    }
                }
            } else {
                Vec3d fin = target.getPositionVector();
                this.moveToPos = new Vec3d(fin.x, target.posY + target.getEyeHeight() / 2, fin.z);
                this.ghost.getNavigator().tryMoveToEntityLiving(target, 1.2F);
                if (this.ghost.getDistanceSq(new BlockPos(this.moveToPos.add(0.5D, 0.5D, 0.5D))) < 3D) {
                    this.resetTask();
                }
            }
        }

    }
}