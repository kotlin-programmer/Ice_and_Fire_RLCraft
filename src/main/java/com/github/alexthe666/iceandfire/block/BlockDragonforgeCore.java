package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDragonforgeCore extends BlockContainer implements IDragonProof {
    private static boolean keepInventory;

    EnumDragonType type;

    public BlockDragonforgeCore() {
        this(null);
    }

    public BlockDragonforgeCore(EnumDragonType type) {
        super(Material.IRON);
        this.setHardness(40F);
        this.setResistance(500F);
        this.setSoundType(SoundType.METAL);
        if (type == null) {
            this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        }
        this.setTranslationKey("iceandfire.dragonforge_core");
        StringBuilder sb = new StringBuilder("dragonforge_core");
        if (type != null) {
            sb.append("_");
            sb.append(type.name().toLowerCase());
        }
        this.setRegistryName(IceAndFire.MODID, sb.toString());
        if (type != null) {
            this.setLightLevel(1.0F);
        }
        this.type = type;
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    public static Block setState(EnumDragonType type, World worldIn, BlockPos pos, TileEntityDragonforge core) {
        keepInventory = true;

        Block block;
        if (type == EnumDragonType.FIRE) {
            block = IafBlockRegistry.dragonforge_core_fire;
        } else if (type == EnumDragonType.ICE) {
            block = IafBlockRegistry.dragonforge_core_ice;
        } else if (type == EnumDragonType.LIGHTNING) {
            block = IafBlockRegistry.dragonforge_core_lightning;
        } else {
            block = IafBlockRegistry.dragonforge_core;
        }

        worldIn.setBlockState(pos, block.getDefaultState(), 3);
        worldIn.setBlockState(pos, block.getDefaultState(), 3);

        keepInventory = false;

        if (core != null) {
            core.validate();
            worldIn.setTileEntity(pos, core);
        }

        return block;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            return false;
        } else {
            playerIn.openGui(IceAndFire.INSTANCE, 7, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(IafBlockRegistry.dragonforge_core);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(IafBlockRegistry.dragonforge_core));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return this.type == EnumDragonType.FIRE;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDragonforge();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!keepInventory) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityDragonforge) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityDragonforge) tileEntity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }
}
