package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDragonforgeInput extends BlockContainer implements IDragonProof {
    public static final PropertyInteger ACTIVE = PropertyInteger.create("active", 0, 3);

    public BlockDragonforgeInput() {
        super(Material.IRON);
        this.setLightOpacity(2);
        this.setHardness(40F);
        this.setResistance(500F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.dragonforge_input");
        this.setRegistryName(IceAndFire.MODID, "dragonforge_input");
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, 0));
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.getConnectedTileEntity(worldIn, pos) != null) {
            TileEntityDragonforge forge = this.getConnectedTileEntity(worldIn, pos);
            if (forge != null) {
                worldIn.scheduleUpdate(forge.getPos(), this, this.tickRate(worldIn));
                return forge.getBlockType().onBlockActivated(worldIn, forge.getPos(), worldIn.getBlockState(forge.getPos()), playerIn, hand, facing, hitX, hitY, hitZ);
            }
        }
        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            this.checkInput(worldIn, pos);
        }
    }

    private void checkInput(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntityDragonforge forge = getConnectedTileEntity(worldIn, pos);
        int meta = 0;
        if (forge != null) {
            meta = getMetaFromType(forge.getType());
        }
        worldIn.setBlockState(pos, state.withProperty(ACTIVE, meta));
    }

    private TileEntityDragonforge getConnectedTileEntity(World worldIn, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (worldIn.getTileEntity(pos.offset(facing)) != null && worldIn.getTileEntity(pos.offset(facing)) instanceof TileEntityDragonforge) {
                TileEntityDragonforge forge = (TileEntityDragonforge) worldIn.getTileEntity(pos.offset(facing));
                if (forge != null && forge.assembled()) {
                    return forge;
                }
            }
        }
        return null;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, meta);
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE);
    }

    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityDragonforgeInput) {
            ((TileEntityDragonforgeInput) tileEntity).resetCore();
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDragonforgeInput();
    }

    public static int getMetaFromType(EnumDragonType type) {
        return BlockDragonforgeVent.getMetaFromType(type);
    }
}
