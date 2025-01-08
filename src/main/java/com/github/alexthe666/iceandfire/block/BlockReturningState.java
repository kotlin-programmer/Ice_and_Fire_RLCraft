package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockReturningState extends Block {
    private final IBlockState returnState;
    public static final PropertyBool REVERTS = PropertyBool.create("revert");

    public BlockReturningState(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, IBlockState returnToState) {
        this(materialIn, gameName, name, toolUsed, toolStrength, hardness, resistance, sound, false, returnToState);
    }

    public BlockReturningState(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery, IBlockState returnToState) {
        super(materialIn);
        this.setTranslationKey(name);
        this.setHarvestLevel(toolUsed, toolStrength);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB);
        if (slippery) {
            this.setDefaultSlipperiness(0.98F);
        }
        this.setRegistryName(IceAndFire.MODID, gameName);
        this.returnState = returnToState;
        this.setDefaultState(this.blockState.getBaseState().withProperty(REVERTS, Boolean.FALSE));
        this.setTickRandomly(IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (IceAndFireConfig.DRAGON_SETTINGS.dragonAffectedBlocksRevert && !worldIn.isRemote) {
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0 && worldIn.isAreaLoaded(pos, 3)) {
                worldIn.setBlockState(pos, returnState);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(REVERTS, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(REVERTS) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, REVERTS);
    }
}