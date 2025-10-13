package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
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
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(new BlockPos(this.myrmex), 300);
        if (village == null) {
            village = this.myrmex.getHive();
        }
        if (village == null) {
            return false;
        } else {
            BlockPos nextRoom = this.myrmex.getRNG().nextInt(2) == 0 ?
                    MyrmexHive.getGroundedPos(this.myrmex.world, village.getRandomRoom(this.myrmex.getRNG(), this.myrmex.getPosition())) :
                    MyrmexHive.getGroundedPos(this.myrmex.world, village.getNextRoom(this.myrmex.getPosition()));
            Path path = this.myrmex.getNavigator().getPathToPos(nextRoom);
            this.myrmex.getNavigator().setPath(path, this.movementSpeed);
            return path != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getNavigator().noPath();
    }
}