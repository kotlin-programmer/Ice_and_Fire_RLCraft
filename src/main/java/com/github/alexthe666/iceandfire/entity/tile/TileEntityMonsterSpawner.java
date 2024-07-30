package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.enums.EnumParticle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ITickable;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileEntityMonsterSpawner extends TileEntityMobSpawner {
    private final SpawnerBaseLogic spawnerLogic = new SpawnerBaseLogic() {
        public void broadcastEvent(int id) {
            TileEntityMonsterSpawner.this.world.addBlockEvent(TileEntityMonsterSpawner.this.pos, Blocks.MOB_SPAWNER, id, 0);
        }

        public World getSpawnerWorld() {
            return TileEntityMonsterSpawner.this.world;
        }

        public BlockPos getSpawnerPosition() {
            return TileEntityMonsterSpawner.this.pos;
        }

        @Override
        public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_) {
            super.setNextSpawnData(p_184993_1_);
            if (this.getSpawnerWorld() != null) {
                IBlockState iblockstate = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
                this.getSpawnerWorld().notifyBlockUpdate(TileEntityMonsterSpawner.this.pos, iblockstate, iblockstate, 4);
            }
        }

        @Override
        public EnumParticle getParticle() {
            return EnumParticle.FLAME;
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.spawnerLogic.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.spawnerLogic.writeToNBT(compound);
        return compound;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void update() {
        this.spawnerLogic.updateSpawner();
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
        nbttagcompound.removeTag("SpawnPotentials");
        return nbttagcompound;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return this.spawnerLogic.setDelayToMin(id) || super.receiveClientEvent(id, type);
    }

    @Override
    public boolean onlyOpsCanSetNbt() {
        return true;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.spawnerLogic.setDirty();
    }

    public MobSpawnerBaseLogic getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }
}