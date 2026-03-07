package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.entity.util.MyrmexRoom;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIWanderHiveCenter extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;

    public MyrmexAIWanderHiveCenter(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if(!this.myrmex.canMove() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigator().noPath() || !this.myrmex.isInHive()){
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            return false;
        } else {
            if(village.getCenter().distanceSq(this.myrmex.getPosition()) > 40000) return false;

            MyrmexRoom thisRoom = village.getNearestRoom(this.myrmex.getPos());
            MyrmexRoom nextRoom = thisRoom.getNearestRoomTowardsCenter();

            BlockPos target = MyrmexHive.getGroundedPos(this.myrmex.world, nextRoom.getPos());

            this.path = this.myrmex.getNavigator().getPathToPos(target);
            return this.path != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getNavigator().noPath() && this.myrmex.shouldEnterHive();
    }

    public void startExecuting() {
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
    }
}