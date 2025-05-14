package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDragonProofGeneric extends BlockGeneric implements IDragonProof {

	public BlockDragonProofGeneric(Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound) {
		super(materialIn, gameName, name, hardness, resistance, sound);
	}
}
