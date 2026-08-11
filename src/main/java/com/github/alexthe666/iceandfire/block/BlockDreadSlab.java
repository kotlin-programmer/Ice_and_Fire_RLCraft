package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public abstract class BlockDreadSlab extends BlockSlab implements IDreadBlock, IDragonProof {

    public BlockDreadSlab(String name, float hardness, float resistance, SoundType soundType, Material material) {
		super(material);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PLAYER_PLACED, Boolean.FALSE));
		this.setHarvestLevel("pickaxe", 3);
		this.setLightOpacity(0);
		this.useNeighborBrightness = true;
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(soundType);
		if (this.isDouble()) {
			setTranslationKey("iceandfire." + name + "_double");
			this.setRegistryName(name + "_double");
		} else {
			setTranslationKey("iceandfire." + name);
			this.setRegistryName(name);
			setCreativeTab(IceAndFire.TAB_BLOCKS);
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return getSlabItem();
	}

	protected abstract Item getSlabItem();

	public abstract ItemBlock getItemBlock();

	@SuppressWarnings("deprecation")
	@Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(getSlabItem());
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		return blockState.getValue(PLAYER_PLACED) ? super.getBlockHardness(blockState, worldIn, pos) : -1;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(PLAYER_PLACED, true);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();
		if (this.isDouble()) {
			return iblockstate.withProperty(PLAYER_PLACED, meta > 0);
		}
		return iblockstate.withProperty(HALF, (meta % 2) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP)
				.withProperty(PLAYER_PLACED, (meta & 2) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if (this.isDouble()) {
			return state.getValue(PLAYER_PLACED) ? 0 : 1;
		}
		int i = state.getValue(PLAYER_PLACED) ? 0 : 2;
		if (state.getValue(HALF) == EnumBlockHalf.TOP) {
			return i + 1;
		}
		return i;
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		IBlockState state = world.getBlockState(pos);
		if (IDreadBlock.isIndestructible(state)) {
			return player.capabilities.isCreativeMode;
		}
		return super.canHarvestBlock(world, pos, player);
	}

	@Override
    protected BlockStateContainer createBlockState() {
		return this.isDouble() ? new BlockStateContainer(this, PLAYER_PLACED) : new BlockStateContainer(this, HALF, PLAYER_PLACED);
	}

	@Override
	public String getTranslationKey(int meta) {
		return super.getTranslationKey();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return null;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return Variant.DEFAULT;
	}

	public enum Variant implements IStringSerializable {
		DEFAULT;

		@Override
        public String getName() {
			return "default";
		}
	}

	public abstract static class Double extends BlockDreadSlab {
		public Double(String name, float hardness, float resistance, SoundType soundType, Material material) {
			super(name, hardness, resistance, soundType, material);
		}

		@Override
        public boolean isDouble() {
			return true;
		}
	}

	public abstract static class Half extends BlockDreadSlab {
		public Half(String name, float hardness, float resistance, SoundType soundType, Material material) {
			super(name, hardness, resistance, soundType, material);
		}

		@Override
        public boolean isDouble() {
			return false;
		}

	}

	static class DreadSlabBlockItem extends ItemBlock {
		private final BlockSlab singleSlab;
		private final BlockSlab doubleSlab;

		public DreadSlabBlockItem(Block block, BlockSlab singleSlab, BlockSlab doubleSlab) {
			super(block);
			this.singleSlab = singleSlab;
			this.doubleSlab = doubleSlab;
			this.setMaxDamage(0);
			this.setHasSubtypes(true);
		}

		@Override
        public int getMetadata(int damage) {
			return damage;
		}

		@Override
        public String getTranslationKey(ItemStack stack) {
			return this.singleSlab.getTranslationKey(stack.getMetadata());
		}

		@Override
        public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			ItemStack stack = playerIn.getHeldItem(hand);
			if(stack.getItem() == Item.getItemFromBlock(doubleSlab)){
				return EnumActionResult.SUCCESS;
			}
			if (stack.getCount() != 0 && playerIn.canPlayerEdit(pos.offset(facing), facing, stack)) {
				IBlockState iblockstate = worldIn.getBlockState(pos);
				if (iblockstate.getBlock() == this.singleSlab && (iblockstate.getValue(PLAYER_PLACED) || playerIn.capabilities.isCreativeMode)) {
					EnumBlockHalf blockslab$enumblockhalf = iblockstate.getValue(BlockSlab.HALF);
					if ((facing == EnumFacing.UP && blockslab$enumblockhalf == EnumBlockHalf.BOTTOM || facing == EnumFacing.DOWN && blockslab$enumblockhalf == EnumBlockHalf.TOP)) {
						IBlockState iblockstate1 = this.doubleSlab.getDefaultState().withProperty(PLAYER_PLACED, iblockstate.getValue(PLAYER_PLACED));
						AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, pos);
						if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate1, 11)) {
							SoundType soundtype = this.doubleSlab.getSoundType(iblockstate1, worldIn, pos, playerIn);
							worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
							stack.shrink(1);
						}
						return EnumActionResult.SUCCESS;
					}
				}
				return this.tryPlace(playerIn, stack, worldIn, pos.offset(facing)) ? EnumActionResult.SUCCESS : super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			} else {
				return EnumActionResult.FAIL;
			}
		}

		@Override
        @SideOnly(Side.CLIENT)
		public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
			BlockPos blockpos = pos;
			IBlockState iblockstate = worldIn.getBlockState(pos);

			if (iblockstate.getBlock() == this.singleSlab && (iblockstate.getValue(PLAYER_PLACED) || player.capabilities.isCreativeMode)) {
				boolean flag = iblockstate.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP;
				if ((side == EnumFacing.UP && !flag || side == EnumFacing.DOWN && flag)) {
					return true;
				}
			}
			pos = pos.offset(side);
			IBlockState iblockstate1 = worldIn.getBlockState(pos);
			return iblockstate1.getBlock() == this.singleSlab && (iblockstate1.getValue(PLAYER_PLACED) || player.capabilities.isCreativeMode) || super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
		}

		private boolean tryPlace(EntityPlayer player, ItemStack stack, World worldIn, BlockPos pos) {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			if (iblockstate.getBlock() == this.singleSlab && (iblockstate.getValue(PLAYER_PLACED) || player.capabilities.isCreativeMode)) {
				IBlockState iblockstate1 = this.doubleSlab.getDefaultState().withProperty(PLAYER_PLACED, iblockstate.getValue(PLAYER_PLACED));
				AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, pos);
				if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate1, 11)) {
					SoundType soundtype = this.doubleSlab.getSoundType(iblockstate1, worldIn, pos, player);
					worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					stack.shrink(1);
				}
				return true;
			}

			return false;
		}
	}
}
