package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrassPath;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockPath extends BlockGrassPath {

    public enum Type {
        CHARED,
        FROZEN,
        CRACKLED;

        public String getRegistrationKey() {
            switch (this) {
                case FROZEN:
                    return "frozen_grass_path";
                case CRACKLED:
                    return "crackled_grass_path";
                default:
                    return "chared_grass_path";
            }
        }

        public String getTranslationKey() {
            switch (this) {
                case FROZEN:
                    return "iceandfire.frozenGrassPath";
                case CRACKLED:
                    return "iceandfire.crackledGrassPath";
                default:
                    return "iceandfire.charedGrassPath";
            }
        }

        public Block getBaseBlock() {
            switch (this) {
                case FROZEN:
                    return IafBlockRegistry.frozenDirt;
                case CRACKLED:
                    return IafBlockRegistry.crackledDirt;
                default:
                    return IafBlockRegistry.charedDirt;
            }
        }
    }

    public static final PropertyBool REVERTS = PropertyBool.create("revert");
    private final Type type;

    public BlockPath(Type type) {
        super();
        this.type = type;
        this.setTranslationKey(this.type.getTranslationKey());
        this.setHarvestLevel("shovel", 0);
        this.setHardness(0.6F);
        this.setSoundType(type == Type.FROZEN ? SoundType.GLASS : SoundType.GROUND);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        if (this.type == Type.FROZEN) {
            this.setDefaultSlipperiness(0.98F);
        }
        this.setLightOpacity(0);
        setRegistryName(IceAndFire.MODID, this.type.getRegistrationKey());
        this.setDefaultState(this.blockState.getBaseState().withProperty(REVERTS, Boolean.FALSE));
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState());
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        switch (side) {
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
                Block block = iblockstate.getBlock();
                return !iblockstate.isOpaqueCube() && block != Blocks.FARMLAND && block != Blocks.GRASS_PATH && block != IafBlockRegistry.charedGrassPath && block != IafBlockRegistry.frozenGrassPath && block != IafBlockRegistry.crackledGrassPath;
            default:
                return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this.type.getBaseBlock().getItemDropped(this.type.getBaseBlock().getDefaultState(), rand, fortune);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.updateBlockState(worldIn, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.updateBlockState(worldIn, pos);
    }

    private void updateBlockState(World worldIn, BlockPos pos) {
        if (!worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
            return;
        }
        Block block = this.type.getBaseBlock();
        IBlockState state = block.getDefaultState();
        worldIn.setBlockState(pos, state);
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