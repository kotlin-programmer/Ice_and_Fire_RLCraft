package com.github.alexthe666.iceandfire.integration.claimit;

import dev.itsmeow.claimit.api.claim.ClaimManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClaimItCompat {

    public static boolean isBlockInAnyClaim(World world, BlockPos pos) {
        return ClaimManager.getManager().isBlockInAnyClaim(world, pos);
    }
}
