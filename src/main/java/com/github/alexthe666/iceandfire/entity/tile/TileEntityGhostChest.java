package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.EnumDifficulty;

import java.util.concurrent.ThreadLocalRandom;

public class TileEntityGhostChest extends TileEntityChest {
    public int spawnChance = 100;

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("SpawnChance", 99)) {
            this.spawnChance = compound.getInteger("SpawnChance");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("SpawnChance", this.spawnChance);
        return compound;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer player) {
        if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.lootTable != null) {
            if (this.world.rand.nextInt(100) < spawnChance) {
                EntityGhost ghost = new EntityGhost(world);
                ghost.moveToBlockPosAndAngles(this.pos.add(0.5, 0.5, 0.5),
                        ThreadLocalRandom.current().nextFloat() * 360F, 0);
                if (!this.world.isRemote) {
                    ghost.onInitialSpawn(world.getDifficultyForLocation(this.pos), null);
                    if (!player.isCreative()) {
                        ghost.setAttackTarget(player);
                    }
                    world.spawnEntity(ghost);
                }
                ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                ghost.setHomePosAndDistance(this.pos, 4);
                ghost.setFromChest(true);
            }
        }
        return super.createContainer(playerInventory, player);
    }
}
