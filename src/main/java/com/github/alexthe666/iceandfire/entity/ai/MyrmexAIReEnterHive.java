package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIReEnterHive extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos nextEntrance = BlockPos.ORIGIN;
    private boolean traversingToTopPart = true;
    private MyrmexHive hive;

    public MyrmexAIReEnterHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if(!this.myrmex.canMove() || this.myrmex.shouldLeaveHive() || (this.myrmex.isOnResin() && !this.myrmex.canSeeSky()) || !traversingToTopPart){
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(new BlockPos(this.myrmex), 500);
        }
        if (village == null) {
            return false;
        } else {
            this.hive = village;
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), false));
            this.myrmex.getNavigator().clearPath();
            this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
            traversingToTopPart = true;
            this.myrmex.isEnteringHive = true;
            this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
            return this.path != null;
        }
    }

    public void updateTask(){
        if(traversingToTopPart && this.myrmex.getDistanceSq(nextEntrance) < 9){
            nextEntrance = hive.getClosestEntranceBottomToEntity(this.myrmex, this.myrmex.getRNG());
            traversingToTopPart = false;
            this.myrmex.getNavigator().clearPath();
            this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
            this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
        }
    }

    public boolean shouldContinueExecuting() {
        if(!traversingToTopPart && this.myrmex.getDistanceSq(nextEntrance) < 15){
            return false;
        }
        return !this.myrmex.getNavigator().noPath() && this.myrmex.shouldEnterHive();
    }

    public void resetTask() {
        this.myrmex.isEnteringHive = false;
        traversingToTopPart = true;
    }
}