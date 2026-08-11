package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockDreadStairs extends BlockStairs implements IDragonProof {

    public BlockDreadStairs(IBlockState modelState, String name) {
        super(modelState);
        this.setHarvestLevel("pickaxe", 3);
        this.setHardness(20F);
        this.setResistance(10000F);
        this.setLightOpacity(0);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire." + name);
        this.setRegistryName(name);
    }
}
