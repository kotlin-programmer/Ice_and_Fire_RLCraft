package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.block.BlockDragonforgeInput;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityShivaxiDragon;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class TileEntityDragonforgeInput extends TileEntity implements ITickable {
    private static final int LURE_DISTANCE = 50;
    private int ticksSinceDragonFire;
    private TileEntityDragonforge core = null;

    public void onHitWithFlame(EnumDragonType type) {
        TileEntityDragonforge forge = getConnectedTileEntity();
        if (forge != null) {
            forge.transferPower(type);
        }
    }

    @Override
    public void update() {
        if (core == null) {
            core = getConnectedTileEntity();
        }
        if (ticksSinceDragonFire > 0) {
            ticksSinceDragonFire--;
        }
        lureDragons();
    }

    protected void lureDragons() {
        boolean dragonSelected = false;
        for (EntityDragonBase dragon : world.getEntitiesWithinAABB(EntityDragonBase.class, new AxisAlignedBB((double) pos.getX() - LURE_DISTANCE, (double) pos.getY() - LURE_DISTANCE, (double) pos.getZ() - LURE_DISTANCE, (double) pos.getX() + LURE_DISTANCE, (double) pos.getY() + LURE_DISTANCE, (double) pos.getZ() + LURE_DISTANCE))) {
            if (!dragonSelected
                    && dragon.isTamed()
                    && !(dragon instanceof EntityShivaxiDragon)
                    && core != null
                    && core.assembled()
                    && core.canSmelt(dragon.dragonType)
                    && canSeeInput(dragon, new Vec3d(this.getPos().getX() + 0.5F, this.getPos().getY() + 0.5F, this.getPos().getZ() + 0.5F))
            ) {
                dragon.burningTarget = this.pos;
                dragonSelected = true;
            } else if (dragon.burningTarget == this.pos) {
                dragon.burningTarget = null;
                dragon.setBreathingFire(false);
            }
        }
    }

    public void resetCore() {
        core = null;
    }

    private boolean canSeeInput(EntityDragonBase dragon, Vec3d target) {
        if (target != null) {
            RayTraceResult rayTrace = world.rayTraceBlocks(new Vec3d(dragon.getPosition().up((int) dragon.height)), target, false);
            if (rayTrace != null && rayTrace.hitVec != null) {
                BlockPos sidePos = rayTrace.getBlockPos();
                BlockPos pos = new BlockPos(rayTrace.hitVec);
                return world.getBlockState(pos).getBlock() instanceof BlockDragonforgeInput || world.getBlockState(sidePos).getBlock() instanceof BlockDragonforgeInput;
            }
        }
        return false;
    }

    private TileEntityDragonforge getConnectedTileEntity() {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (world.getTileEntity(pos.offset(facing)) != null && world.getTileEntity(pos.offset(facing)) instanceof TileEntityDragonforge) {
                return (TileEntityDragonforge) world.getTileEntity(pos.offset(facing));
            }
        }
        return null;
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing) {
        return getConnectedTileEntity() != null && getConnectedTileEntity().hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
        if (getConnectedTileEntity() != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return getConnectedTileEntity().getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }
}
