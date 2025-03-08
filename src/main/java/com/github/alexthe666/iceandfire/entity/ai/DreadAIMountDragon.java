package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityBlackFrostDragon;
import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.List;

public class DreadAIMountDragon extends EntityAIBase {
    private final EntityDreadQueen queen;
    private EntityBlackFrostDragon dragon;

    public DreadAIMountDragon(EntityDreadQueen queen) {
        this.queen = queen;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.queen.isRiding()) {
            return false;
        } else {
            List<EntityBlackFrostDragon> list = this.queen.world.getEntitiesWithinAABB(EntityBlackFrostDragon.class, this.queen.getEntityBoundingBox().grow(32.0D, 7.0D, 32.0D));

            if (list.isEmpty()) {
                return false;
            } else {
                for (EntityBlackFrostDragon dragon : list) {
                   if (!dragon.isBeingRidden()) {
                       this.dragon = dragon;
                       break;
                   }
                }

                return this.dragon != null;
            }
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.queen.isRiding() && this.dragon != null && !this.dragon.isBeingRidden();
    }

    public void startExecuting() {
        this.dragon.getNavigator().clearPath();
    }

    public void resetTask() {
        this.dragon = null;
        this.queen.getNavigator().clearPath();
    }

    public void updateTask() {
        this.queen.getLookHelper().setLookPositionWithEntity(this.dragon, 30.0F, 30.0F);

        this.queen.getNavigator().tryMoveToEntityLiving(this.dragon, 1.2D);
        if (this.queen.getDistanceSq(this.dragon) < this.dragon.getRenderSize()) {
            this.queen.getNavigator().clearPath();
            this.dragon.setCommanderId(this.queen.getUniqueID());
            this.queen.startRiding(dragon);
        }
    }
}