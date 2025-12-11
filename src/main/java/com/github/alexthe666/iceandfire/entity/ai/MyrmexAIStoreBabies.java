package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.structures.WorldGenMyrmexHive;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIStoreBabies extends EntityAIBase {
    private final EntityMyrmexWorker myrmex;
    private final double movementSpeed;
    private BlockPos nextRoom = BlockPos.ORIGIN;

    public MyrmexAIStoreBabies(EntityMyrmexWorker entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || !this.myrmex.holdingBaby() || !this.myrmex.isInHive()) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            return false;
        } else {
            nextRoom = MyrmexHive.getGroundedPos(this.myrmex.world, village.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.myrmex.getRNG(), this.myrmex.getPosition()).add(this.myrmex.getRNG().nextInt(11)-5, 1, this.myrmex.getRNG().nextInt(11)-5));
            this.myrmex.getNavigator().tryMoveToXYZ(this.nextRoom.getX(), this.nextRoom.getY(), this.nextRoom.getZ(), this.movementSpeed);
            return true;
        }
    }

    public boolean shouldContinueExecuting() {
        return this.myrmex.holdingBaby() && !this.myrmex.getNavigator().noPath();
    }

    @Override
    public void updateTask() {
        if (this.myrmex.getDistanceSq(nextRoom) < 4 && !this.myrmex.getPassengers().isEmpty()){
            for(Entity entity : this.myrmex.getPassengers()){
                entity.dismountRidingEntity();
                this.myrmex.getNavigator().clearPath();
                entity.copyLocationAndAnglesFrom(this.myrmex);
            }
        }
    }
}