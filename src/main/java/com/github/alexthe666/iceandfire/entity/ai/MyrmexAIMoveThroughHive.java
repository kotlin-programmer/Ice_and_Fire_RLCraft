package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.entity.util.MyrmexRoom;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIMoveThroughHive extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;

    public MyrmexAIMoveThroughHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if(!this.myrmex.canMove() || !this.myrmex.getNavigator().noPath() || this.myrmex.canSeeSky()){
            return false;
        }
        MyrmexHive hive = this.myrmex.getHive();
        if (hive == null) {
            return false;
        }
        if(hive.getCenter().distanceSq(this.myrmex.getPosition()) > 40000) return false;

        MyrmexRoom thisRoom = hive.getNearestRoom(this.myrmex.getPos());
        MyrmexRoom nextRoom = this.myrmex.getRNG().nextInt(4) == 0 ? thisRoom.getNearestRoomTowardsCenter() : thisRoom.getRandomConnectedRoom(this.myrmex.getRNG());

        Path path = this.myrmex.getNavigator().getPathToPos(nextRoom.getPos());
        this.myrmex.getNavigator().setPath(path, this.movementSpeed);
        return path != null;

    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getNavigator().noPath();
    }
}