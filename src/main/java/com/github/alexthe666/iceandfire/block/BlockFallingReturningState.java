package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFallingReturningState extends BlockFalling {
	public static final PropertyBool REVERTS = PropertyBool.create("revert");
	private final IBlockState revertState;

	public BlockFallingReturningState(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, IBlockState revertState) {
		this(materialIn, gameName, name, toolUsed, toolStrength, hardness, resistance, sound, revertState, false);
	}

	public BlockFallingReturningState(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, IBlockState revertState, boolean slippery) {
		super(materialIn);
		this.setTranslationKey(name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setHarvestLevel(toolUsed, toolStrength);
		this.setSoundType(sound);
		this.setCreativeTab(IceAndFire.TAB);
		setRegistryName(IceAndFire.MODID, gameName);
		this.setDefaultState(this.blockState.getBaseState().withProperty(REVERTS, Boolean.FALSE));
		this.setTickRandomly(true);
		if (slippery) {
			this.setDefaultSlipperiness(0.98F);
		}
		this.revertState = revertState;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote && state.getValue(REVERTS) && rand.nextInt(3) == 0 && worldIn.isAreaLoaded(pos, 3)) {
			worldIn.setBlockState(pos, this.revertState);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getDustColor(IBlockState blkst) {
		return -8356741;
	}

	@Override
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