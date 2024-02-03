package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.EnumDifficulty;

import java.util.concurrent.ThreadLocalRandom;

public class TileEntityGhostChest extends TileEntityChest {

    public void openInventory(EntityPlayer player) {
        boolean hasLoot = this.lootTable != null;
        super.openInventory(player);
        if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL && hasLoot) {
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

}
