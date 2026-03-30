package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.entity.util.MyrmexRoom;
import com.github.alexthe666.iceandfire.structures.WorldGenMyrmexHive;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
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
            if(village.getCenter().distanceSq(this.myrmex.getPosition()) > 40000) return false;

            MyrmexRoom thisRoom = village.getNearestRoom(this.myrmex.getPos());
            MyrmexRoom nextRoom = thisRoom.getConnectedRoomOfType(WorldGenMyrmexHive.RoomType.NURSERY);
            if(nextRoom == null || nextRoom.getPos().distanceSq(this.myrmex.getPos()) > 1600) return false; // try again later

            //random position in nursery
            this.nextRoom = nextRoom.getPos().add(3 - this.myrmex.getRNG().nextInt(7), 4, 3 - this.myrmex.getRNG().nextInt(7));
            this.nextRoom = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), this.nextRoom);

            Path path = this.myrmex.getNavigator().getPathToPos(this.nextRoom);
            ((EntityLiving) this.myrmex.getHeldEntity()).getNavigator().setPath(path, this.movementSpeed); //living passengers steer the living vehicle with fixed speed of 1.5 (EntityLiving::updateEntityActionState, why...)
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