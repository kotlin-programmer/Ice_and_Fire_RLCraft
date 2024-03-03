package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityGhostChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;

import java.util.Random;

public class BlockGhostChest extends BlockChest {
    public BlockGhostChest() {
        super(Type.TRAP);
        this.setHardness(2F);
        this.setResistance(1F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB);
        this.setTranslationKey("iceandfire.ghost_chest");
        this.setRegistryName(IceAndFire.MODID, "ghost_chest");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGhostChest();
    }

    public boolean canProvidePower(IBlockState state) {
        return this.chestType == BlockChest.Type.TRAP;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (!blockState.canProvidePower()) {
            return 0;
        } else {
            int i = 0;
            TileEntity tileentity = blockAccess.getTileEntity(pos);
            if (tileentity instanceof TileEntityChest) {
                i = ((TileEntityChest)tileentity).numPlayersUsing;
            }

            return MathHelper.clamp(i, 0, 15);
        }
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }
}
