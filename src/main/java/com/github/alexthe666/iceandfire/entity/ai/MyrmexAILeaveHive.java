package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
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
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(new BlockPos(this.myrmex), 1000);
        if (village == null) {
            return false;
        } else {
            BlockPos nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, village.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), true));
            Path path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
            if(path == null) {
                //fallback 1: path to bottom of entrance
                nextEntrance = village.getClosestEntranceBottomToEntity(this.myrmex, this.myrmex.getRNG());
                path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
                if(path == null) {
                    //fallback 2: path to hive center
                    nextEntrance = village.getCenterGround();
                    path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
                }
            }
            if(path != null) {
                this.myrmex.getNavigator().setPath(path, this.movementSpeed);
                this.myrmex.isEnteringHive = false;
            } else {
                delay = 100; //allow the myrmex to do other tasks, maybe it will find the way later
            }
            return path != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getNavigator().noPath();
    }
}