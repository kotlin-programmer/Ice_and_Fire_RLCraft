package com.github.alexthe666.iceandfire.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;

import java.util.*;

public class WorldGenAnimalFarm extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		Biome biome = worldIn.getBiome(position);
		Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);

		boolean sandy = types.contains(BiomeDictionary.Type.SANDY);
		Block fence = Blocks.OAK_FENCE;
		Block fence_gate = Blocks.OAK_FENCE_GATE;
		List<Integer> spawns = new ArrayList<>();

		if(types.contains(BiomeDictionary.Type.SAVANNA)) {
			fence = Blocks.ACACIA_FENCE;
			fence_gate = Blocks.ACACIA_FENCE_GATE;
			spawns.add(0);//Cow
		}
		else if(types.contains(BiomeDictionary.Type.CONIFEROUS)) {
			fence = Blocks.SPRUCE_FENCE;
			fence_gate = Blocks.SPRUCE_FENCE_GATE;
			spawns.add(1);//Sheep
		}
		else if(types.contains(BiomeDictionary.Type.JUNGLE)) {
			fence = Blocks.JUNGLE_FENCE;
			fence_gate = Blocks.JUNGLE_FENCE_GATE;
			spawns.add(2);//Chicken
		}
		else if(types.contains(BiomeDictionary.Type.FOREST) && types.contains(BiomeDictionary.Type.DENSE)) {
			fence = Blocks.DARK_OAK_FENCE;
			fence_gate = Blocks.DARK_OAK_FENCE_GATE;
		}
		else if(biome == Biomes.BIRCH_FOREST || biome == Biomes.BIRCH_FOREST_HILLS || biome == Biomes.MUTATED_BIRCH_FOREST || biome == Biomes.MUTATED_BIRCH_FOREST_HILLS) {
			fence = Blocks.BIRCH_FENCE;
			fence_gate = Blocks.BIRCH_FENCE_GATE;
		}

		if(types.contains(BiomeDictionary.Type.SANDY)) {
			spawns.add(3);//Pig
		}

		if(types.contains(BiomeDictionary.Type.PLAINS)) {
			spawns.add(2);//Chicken
		}

		if(spawns.isEmpty()) spawns.add(rand.nextInt(4));
		for(int type : spawns) {
			for(int count = 0; count < rand.nextInt(3) + 2; count++) {
				EntityAnimal animal = null;
				switch(type) {
					case 0: animal = new EntityCow(worldIn); break;
					case 1: animal = new EntitySheep(worldIn); break;
					case 2: animal = new EntityChicken(worldIn); break;
					case 3: animal = new EntityPig(worldIn); break;
				}
				if(animal == null) break;
				animal.setPositionAndRotation(position.getX() + 0.5F + (rand.nextInt(7) - 3), position.getY() + 1.5F, position.getZ() + 0.5F + (-3 + rand.nextInt(6)), rand.nextFloat() * 360, 0);
				worldIn.spawnEntity(animal);
			}
		}

		for(int x = -4; x < +5; x++) {
			for(int z = -4; z < +5; z++) {
				if(((x % 4 == 0 || z % 4 == 0) || (x % -4 == 0 || z % -4 == 0)) && Math.abs(x) != 0 && Math.abs(z) != 0) {
					worldIn.setBlockState(position.add(x, 0, z), sandy ? Blocks.SAND.getDefaultState() : Blocks.GRASS.getDefaultState(), 2);
					worldIn.setBlockState(position.add(x, 1, z), fence.getDefaultState(), 2);
				}
				else {
					worldIn.setBlockState(position.add(x, 0, z), Blocks.GRASS_PATH.getDefaultState(), 2);
					worldIn.setBlockState(position.add(x, 1, z), Blocks.AIR.getDefaultState(), 2);
					worldIn.setBlockState(position.add(x, 2, z), Blocks.AIR.getDefaultState(), 2);
				}

				if(x == 0) {
					worldIn.setBlockState(position.add(0, 1, 4), fence_gate.getDefaultState().withProperty(BlockFenceGate.FACING, EnumFacing.SOUTH), 2);
					worldIn.setBlockState(position.add(0, 1, -4), fence_gate.getDefaultState().withProperty(BlockFenceGate.FACING, EnumFacing.NORTH), 2);
					worldIn.setBlockState(position.add(0, 0, 4), Blocks.GRASS_PATH.getDefaultState(), 2);
					worldIn.setBlockState(position.add(0, 0, -4), Blocks.GRASS_PATH.getDefaultState(), 2);
				}

				if(z == 0) {
					worldIn.setBlockState(position.add(4, 1, 0), fence_gate.getDefaultState().withProperty(BlockFenceGate.FACING, EnumFacing.EAST), 2);
					worldIn.setBlockState(position.add(-4, 1, 0), fence_gate.getDefaultState().withProperty(BlockFenceGate.FACING, EnumFacing.WEST), 2);
					worldIn.setBlockState(position.add(4, 0, 0), Blocks.GRASS_PATH.getDefaultState(), 2);
					worldIn.setBlockState(position.add(-4, 0, 0), Blocks.GRASS_PATH.getDefaultState(), 2);
				}
			}
		}
		return true;
	}
}