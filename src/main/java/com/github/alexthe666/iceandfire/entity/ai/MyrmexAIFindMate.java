package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class MyrmexAIFindMate extends EntityAITarget {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<EntityMyrmexRoyal> targetEntitySelector;
    protected EntityMyrmexBase targetEntity;
    public EntityMyrmexRoyal myrmex;

    public MyrmexAIFindMate(EntityMyrmexRoyal myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector =other -> other != myrmex && other.getGrowthStage() >= 2;
        this.myrmex = myrmex;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex.getAttackTarget() != null || this.myrmex.releaseTicks < 400 || this.myrmex.mate != null) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(new BlockPos(this.myrmex), 100);
        }
        if (village != null) {
            return false;
        }
        List<EntityMyrmexRoyal> list = this.taskOwner.world.getEntitiesWithinAABB(EntityMyrmexRoyal.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
        if (list.isEmpty()) {
            return false;
        } else {
            list.sort(this.theNearestAttackableTargetSorter);
            for(EntityMyrmexRoyal royal : list){
                if(this.myrmex.canMateWith(royal)){
                    this.myrmex.mate = royal;
                    this.myrmex.world.setEntityState(this.myrmex, (byte) 76);
                    return true;
                }
            }
        return false;
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