package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class MyrmexAIForageForItems extends EntityAITarget {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super EntityItem> targetEntitySelector;
    protected EntityItem targetEntity;
    public EntityMyrmexWorker myrmex;

    public MyrmexAIForageForItems(EntityMyrmexWorker myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = item -> !item.getItem().isEmpty() && !item.isInWater();
        this.myrmex = myrmex;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || !this.myrmex.getNavigator().noPath() || !this.myrmex.keepSearching) {
            return false;
        }
        List<EntityItem> list = this.taskOwner.world.getEntitiesWithinAABB(EntityItem.class, this.getTargetableArea(25), this.targetEntitySelector);
        if (list.isEmpty()) {
            return false;
        } else {
            list.sort(this.theNearestAttackableTargetSorter);
            this.targetEntity = list.get(0);
            return true;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToEntityLiving(this.targetEntity, 1);
        super.startExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.targetEntity == null || this.targetEntity.isDead || this.targetEntity.isInWater()) {
            this.myrmex.getNavigator().clearPath();
            this.resetTask();
        }
        if (this.targetEntity != null && !this.targetEntity.isDead && this.taskOwner.getDistanceSq(this.targetEntity) < 4) {
            this.myrmex.onPickupItem(targetEntity);
            this.myrmex.setHeldItem(EnumHand.MAIN_HAND, this.targetEntity.getItem());
            this.targetEntity.setDead();
            this.myrmex.getNavigator().clearPath();
            resetTask();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.taskOwner.getNavigator().noPath() && this.myrmex.getAttackTarget() == null;
    }
}