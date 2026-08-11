package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class MyrmexAISummonerHurtTarget extends EntityAITarget {
    EntityMyrmexSwarmer tameable;
    EntityLivingBase attackedEntity;
    private int timestamp;

    public MyrmexAISummonerHurtTarget(EntityMyrmexSwarmer theEntityMyrmexSwarmerIn) {
        super(theEntityMyrmexSwarmerIn, false);
        this.tameable = theEntityMyrmexSwarmerIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.tameable.getSummoner();

        if (entitylivingbase == null) {
            return false;
        } else {
            this.attackedEntity = entitylivingbase.getLastAttackedEntity();
            this.timestamp = entitylivingbase.getLastAttackedEntityTime();
            return this.isSuitableTarget(this.attackedEntity, false)
                    && this.tameable.shouldAttackEntity(this.attackedEntity)
                    && isAttackTargetStillValid();
        }
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attackedEntity);
        super.startExecuting();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.isSuitableTarget(this.attackedEntity, false)
                && this.tameable.shouldAttackEntity(this.attackedEntity)
                && isAttackTargetStillValid()
                && super.shouldContinueExecuting();
    }

    private boolean isAttackTargetStillValid() {
        EntityLivingBase entityLivingBase = this.tameable.getSummoner();
        if (entityLivingBase == null) {
            return false;
        }
        return entityLivingBase.ticksExisted - timestamp <= 100;
    }
}