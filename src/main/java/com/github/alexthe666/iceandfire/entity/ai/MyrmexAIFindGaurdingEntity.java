package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSoldier;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class MyrmexAIFindGaurdingEntity extends EntityAITarget {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super EntityMyrmexBase> targetEntitySelector;
    protected EntityMyrmexBase targetEntity;
    public EntityMyrmexSoldier myrmex;

    public MyrmexAIFindGaurdingEntity(EntityMyrmexSoldier myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector =other -> other != null && !(other instanceof EntityMyrmexSoldier) && other.getGrowthStage() > 1 && !other.isBeingGuarded && other.needsGaurding() && EntityMyrmexBase.haveSameHive(MyrmexAIFindGaurdingEntity.this.myrmex, other);
        this.myrmex = myrmex;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex.getAttackTarget() != null || this.myrmex.guardingEntity != null) {
            return false;
        }
        List<EntityMyrmexBase> list = this.taskOwner.world.getEntitiesWithinAABB(EntityMyrmexBase.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
        if (list.isEmpty()) {
            return false;
        } else {
            list.sort(this.theNearestAttackableTargetSorter);
            this.myrmex.guardingEntity = list.get(0);
            return true;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}