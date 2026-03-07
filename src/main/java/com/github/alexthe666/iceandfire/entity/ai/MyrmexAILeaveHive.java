package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.entity.util.MyrmexRoom;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;

public class MyrmexAILeaveHive extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private int delay = 0;

    public MyrmexAILeaveHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if(delay > 0) delay--;
        if(delay > 0 || !this.myrmex.canMove() || !this.myrmex.shouldLeaveHive() || !this.myrmex.isOnResin() || !this.myrmex.getNavigator().noPath() || this.myrmex.isEnteringHive) {
            return false;
        }
        MyrmexHive hive = this.myrmex.getHive();
        if (hive == null) {
            return false;
        } else {
            BlockPos nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), true));
            Path path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
            if(path == null || distanceToTargetTooBig(path.getFinalPathPoint(), nextEntrance)) {
                //fallback 1: path to bottom of entrance
                nextEntrance = hive.getClosestEntranceBottomToEntity(this.myrmex, this.myrmex.getRNG());
                path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
            }
            if(path == null || distanceToTargetTooBig(path.getFinalPathPoint(), nextEntrance)){
                //fallback 2: path to center
                MyrmexRoom currRoom = hive.getNearestRoom(this.myrmex.getPos());
                MyrmexRoom targetRoom = currRoom.getNearestRoomTowardsCenter();
                path = this.myrmex.getNavigator().getPathToPos(targetRoom.getPos());
            }
            if(path != null && !distanceToTargetTooBig(path.getFinalPathPoint(), nextEntrance)) {
                this.myrmex.getNavigator().setPath(path, this.movementSpeed);
                this.myrmex.isEnteringHive = false;
                return true;
            } else {
                delay = 50; //allow the myrmex to do other tasks, maybe it will find the way later
                return false;
            }
        }
    }

    private static boolean distanceToTargetTooBig(PathPoint endPoint, BlockPos targetPos){
        if(endPoint == null) return true;
        double distXZ = Math.sqrt(new BlockPos(endPoint.x, targetPos.getY(), endPoint.z).distanceSq(targetPos));
        double distY = Math.abs(targetPos.getY() - endPoint.y);
        return distXZ > 45 || distY > 15;
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getNavigator().noPath();
    }
}