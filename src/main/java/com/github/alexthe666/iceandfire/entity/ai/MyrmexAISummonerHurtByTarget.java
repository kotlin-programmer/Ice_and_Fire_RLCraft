package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class MyrmexAISummonerHurtByTarget extends EntityAITarget {
    EntityMyrmexSwarmer tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public MyrmexAISummonerHurtByTarget(EntityMyrmexSwarmer theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entityLivingBase = this.tameable.getSummoner();

        if (entityLivingBase == null) {
            return false;
        } else {
            this.attacker = entityLivingBase.getRevengeTarget();
            this.timestamp = entityLivingBase.getRevengeTimer();
            return  this.isSuitableTarget(this.attacker, false)
                    && this.tameable.shouldAttackEntity(this.attacker)
                    && isRevengeTargetStillValid();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.isSuitableTarget(this.attacker, false)
                && this.tameable.shouldAttackEntity(this.attacker)
                && isRevengeTargetStillValid()
                && super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        super.startExecuting();
    }

    private boolean isRevengeTargetStillValid() {
        EntityLivingBase entityLivingBase = this.tameable.getSummoner();
        if (entityLivingBase == null) {
            return false;
        }
        return entityLivingBase.ticksExisted - timestamp <= 100;
    }
}