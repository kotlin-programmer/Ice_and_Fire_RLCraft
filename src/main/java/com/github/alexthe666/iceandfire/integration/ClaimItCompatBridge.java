package com.github.alexthe666.iceandfire.integration;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClaimItCompatBridge {

	public static boolean isBlockInAnyClaim(World world, BlockPos pos) {
		if (CompatLoadUtil.isClaimItLoaded()) {
			return ClaimItCompat.isBlockInAnyClaim(world, pos);
		}
		return false;
	}
}