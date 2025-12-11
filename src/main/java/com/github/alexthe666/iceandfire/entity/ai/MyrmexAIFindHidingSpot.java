package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSentinel;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class MyrmexAIFindHidingSpot extends EntityAIBase {
    private static final int RADIUS = 32;

    private final EntityMyrmexSentinel myrmex;
    private BlockPos targetBlock = BlockPos.ORIGIN;
    protected final Predicate<EntityMyrmexSentinel> targetEntitySelector;

    public MyrmexAIFindHidingSpot(EntityMyrmexSentinel myrmex) {
        this.targetEntitySelector = other -> other.isHiding() && other != myrmex;
        this.myrmex = myrmex;
    }

    @Override
    public boolean shouldExecute() {
        if(!this.myrmex.canMove() || this.myrmex.getAttackTarget() != null || this.myrmex.isOnResin() || !this.myrmex.getNavigator().noPath())
            return false;
        this.targetBlock = getTargetPosition(RADIUS);
        this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.myrmex.getNavigator().noPath();
    }

    @Override
    public void updateTask() {
        if(this.myrmex.getDistanceSqToCenter(this.targetBlock) < 2) {
            if (this.myrmex.isOnResin() || areHiddenSentinelsNear(RADIUS)) {
                this.targetBlock = getTargetPosition(RADIUS);
                this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            } else {
                this.myrmex.setHiding(true);
                this.myrmex.getNavigator().clearPath();
            }
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.myrmex.getEntityBoundingBox().grow(targetDistance, 14.0D, targetDistance);
    }

    public BlockPos getTargetPosition(int radius){
        int x = (int)myrmex.posX + myrmex.getRNG().nextInt(radius * 2) - radius;
        int z = (int)myrmex.posZ + myrmex.getRNG().nextInt(radius * 2) - radius;
        BlockPos newPos = MyrmexHive.getGroundedPos(this.myrmex.world, new BlockPos(x, this.myrmex.getPos().getY() + 5, z));
        MyrmexHive myHive = this.myrmex.getHive();
        if(myHive == null) return newPos;
        BlockPos hiveEntrance = myHive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), false);
        if(hiveEntrance.distanceSq(newPos) > 6400) return hiveEntrance; //traverse back to entrance if too far away
        return newPos;
    }

    private boolean areHiddenSentinelsNear(double distance){
        List<EntityMyrmexSentinel> hiddenSentinels = this.myrmex.world.getEntitiesWithinAABB(EntityMyrmexSentinel.class, this.getTargetableArea(distance), this.targetEntitySelector);
        return !hiddenSentinels.isEmpty();
    }

}
