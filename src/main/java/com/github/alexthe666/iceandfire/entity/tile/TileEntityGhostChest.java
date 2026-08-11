package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.EnumDifficulty;

import java.util.concurrent.ThreadLocalRandom;

public class TileEntityGhostChest extends TileEntityChest {
    public boolean spawnChecked = false;
    public int spawnChance = 100;

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("SpawnChecked", 99)) {
            this.spawnChecked = compound.getBoolean("SpawnChecked");
        }
        if (compound.hasKey("SpawnChance", 99)) {
            this.spawnChance = compound.getInteger("SpawnChance");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("SpawnChecked", this.spawnChecked);
        compound.setInteger("SpawnChance", this.spawnChance);
        return compound;
    }

    public void checkSpawn(EntityPlayer player) {
        if (!this.world.isRemote && this.world.getDifficulty() != EnumDifficulty.PEACEFUL && !this.spawnChecked) {
            if (this.world.rand.nextInt(100) < this.spawnChance) {
                EntityGhost ghost = new EntityGhost(this.world);
                ghost.moveToBlockPosAndAngles(this.pos.add(0.5, 0.5, 0.5),
                        ThreadLocalRandom.current().nextFloat() * 360F, 0);
                ghost.onInitialSpawn(this.world.getDifficultyForLocation(this.pos), null);
                if (!player.isCreative()) {
                    ghost.setAttackTarget(player);
                }
                ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                ghost.setHomePosAndDistance(this.pos, 4);
                ghost.setFromChest(true);
                this.world.spawnEntity(ghost);
            }
            this.spawnChecked = true;
        }
    }
}
