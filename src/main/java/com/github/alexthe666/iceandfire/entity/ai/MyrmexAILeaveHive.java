package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class MyrmexAILeaveHive extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private BlockPos nextEntrance = BlockPos.ORIGIN;

    public MyrmexAILeaveHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if(this.myrmex.isChild()){
            return false;
        }

        if(!this.myrmex.canMove() || !this.myrmex.shouldLeaveHive() || !this.myrmex.isOnResin() || this.myrmex instanceof EntityMyrmexWorker && (((EntityMyrmexWorker)this.myrmex).holdingBaby() || !this.myrmex.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) || this.myrmex.isEnteringHive) {
            return false;
        }
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(new BlockPos(this.myrmex), 1000);
        if (village == null) {
            return false;
        } else {
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, village.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), true));
            Path path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
            this.myrmex.getNavigator().setPath(path, this.movementSpeed);
            this.myrmex.isEnteringHive = false;
            return path != null;
        }
    }

    public boolean shouldContinueExecuting() {
        if(this.myrmex.getDistanceSq(nextEntrance) <= 3 || this.myrmex.shouldEnterHive()) {
            return false;
        }
        return !this.myrmex.getNavigator().noPath() && this.myrmex.getDistanceSq(nextEntrance) > 3 && this.myrmex.shouldLeaveHive();
    }
}