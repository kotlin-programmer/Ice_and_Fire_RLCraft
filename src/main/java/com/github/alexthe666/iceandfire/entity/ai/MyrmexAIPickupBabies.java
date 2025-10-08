package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyrmexAIPickupBabies extends EntityAITarget {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super EntityLivingBase> targetEntitySelector;
    protected EntityLivingBase targetEntity;
    public EntityMyrmexWorker myrmex;

    public MyrmexAIPickupBabies(EntityMyrmexWorker myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = other -> other != null && (other instanceof EntityMyrmexBase && ((EntityMyrmexBase) other).getGrowthStage() < 2 && !((EntityMyrmexBase) other).isInNursery() || other instanceof EntityMyrmexEgg && !((EntityMyrmexEgg) other).isInNursery());
        this.myrmex = myrmex;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || !this.myrmex.getNavigator().noPath() || this.myrmex.shouldEnterHive() || !this.myrmex.keepSearching || this.myrmex.holdingBaby()) {
            return false;
        }
        List<EntityLivingBase> listBabies = this.taskOwner.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
        if (listBabies.isEmpty()) {
            return false;
        } else {
            listBabies.sort(this.theNearestAttackableTargetSorter);
            this.targetEntity = listBabies.get(0);
            return true;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ, 1);
        super.startExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.targetEntity == null || this.targetEntity.isDead) {
            this.resetTask();
        }
        if (this.targetEntity != null && !this.targetEntity.isDead && this.taskOwner.getDistanceSq(this.targetEntity) < 2) {
            this.targetEntity.startRiding(this.myrmex);
            resetTask();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.taskOwner.getNavigator().noPath();
    }
}