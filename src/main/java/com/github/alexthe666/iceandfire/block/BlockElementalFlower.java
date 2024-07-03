package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.crafting.IInfusionStabiliser;

@Optional.Interface(iface = "thaumcraft.api.crafting.IInfusionStabiliser", modid = "thaumcraft")
public class BlockElementalFlower extends BlockBush implements IInfusionStabiliser {

	private final int plantable;

	public BlockElementalFlower(String name, int plantable) {
		this.setTickRandomly(true);
		this.setCreativeTab(IceAndFire.TAB);
		this.setTranslationKey(IceAndFire.MODID + "." + name);
		setRegistryName(IceAndFire.MODID, name);
		this.setSoundType(SoundType.PLANT);
		this.plantable = plantable;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState soil = worldIn.getBlockState(pos.down());
		if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) return false;
		switch (this.plantable) {
			case 0 : return (soil.getMaterial() == Material.SAND || soil.getBlock() == Blocks.NETHERRACK);
			case 1 : return (soil.getMaterial() == Material.PACKED_ICE || soil.getMaterial() == Material.ICE);
			case 2 : return (soil.getMaterial() == Material.GROUND || soil.getMaterial() == Material.GRASS);
			default : return false;
		}
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return true;
	}

	@Override
	@Optional.Method(modid = "thaumcraft")
	public boolean canStabaliseInfusion(World world, BlockPos pos) {
		return true;
	}
}