package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class GhostPathNavigator extends PathNavigateFlying {

    private boolean shouldAvoidSun;
    public EntityGhost ghost;

    public GhostPathNavigator(EntityGhost entityIn, World worldIn) {
        super(entityIn, worldIn);
        ghost = entityIn;

    }

    public void setAvoidSun(boolean avoidSun) {
        this.shouldAvoidSun = avoidSun;
    }

    protected void removeSunnyPath() {
        super.removeSunnyPath();
        if (this.shouldAvoidSun) {
            if (this.world.canSeeSky(new BlockPos(MathHelper.floor(this.entity.posX), (int)(this.entity.getEntityBoundingBox().minY + 0.5), MathHelper.floor(this.entity.posZ)))) {
                return;
            }

            for(int i = 0; i < this.currentPath.getCurrentPathLength(); ++i) {
                PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
                if (this.world.canSeeSky(new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z))) {
                    this.currentPath.setCurrentPathLength(i - 1);
                    return;
                }
            }
        }

    }

    @Override
    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        ghost.getMoveHelper().setMoveTo(entityIn.posX, entityIn.posY, entityIn.posZ, speedIn);
        return true;
    }

    @Override
    public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
        ghost.getMoveHelper().setMoveTo(x, y, z, speedIn);
        return true;
    }
}