package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSoldier;
import net.minecraft.entity.ai.EntityAIBase;

public class MyrmexAIEscortEntity extends EntityAIBase {
    private final EntityMyrmexSoldier myrmex;
    private final double movementSpeed;

    public MyrmexAIEscortEntity(EntityMyrmexSoldier entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        return this.myrmex.canMove() && this.myrmex.getAttackTarget() == null && this.myrmex.guardingEntity != null && !this.myrmex.isEnteringHive;
    }

    public void updateTask() {
        if(this.myrmex.guardingEntity != null && (this.myrmex.getDistance(this.myrmex.guardingEntity) > 30 || this.myrmex.getNavigator().noPath())) {
            this.myrmex.getNavigator().tryMoveToEntityLiving(this.myrmex.guardingEntity, movementSpeed);
        }
    }

    public boolean shouldContinueExecuting() {
        return this.myrmex.canMove() && this.myrmex.getAttackTarget() == null && this.myrmex.guardingEntity != null && this.myrmex.guardingEntity.needsGaurding() && this.myrmex.guardingEntity.isEntityAlive() && (this.myrmex.getDistance(this.myrmex.guardingEntity) < 15 || !this.myrmex.getNavigator().noPath());
    }

}