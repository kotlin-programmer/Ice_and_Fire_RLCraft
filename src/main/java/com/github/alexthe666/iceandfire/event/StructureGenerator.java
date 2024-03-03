package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import com.github.alexthe666.iceandfire.structures.*;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.github.alexthe666.iceandfire.world.village.MapGenPixieVillage;
import com.github.alexthe666.iceandfire.world.village.MapGenSnowVillage;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.Nullable;
import java.util.*;

public class StructureGenerator implements IWorldGenerator {

	private static final MapGenSnowVillage SNOW_VILLAGE = new MapGenSnowVillage();
	private static final MapGenPixieVillage PIXIE_VILLAGE = new MapGenPixieVillage();
	private static final WorldGenMyrmexHive JUNGLE_MYRMEX_HIVE = new WorldGenMyrmexHive(true);
	private static final WorldGenMyrmexHive DESERT_MYRMEX_HIVE = new WorldGenMyrmexHive(false);
	private static final WorldGenFireDragonCave FIRE_DRAGON_CAVE = new WorldGenFireDragonCave();
	private static final WorldGenFireDragonRoost FIRE_DRAGON_ROOST = new WorldGenFireDragonRoost();
	private static final WorldGenIceDragonCave ICE_DRAGON_CAVE = new WorldGenIceDragonCave();
	private static final WorldGenIceDragonRoost ICE_DRAGON_ROOST = new WorldGenIceDragonRoost();
	private static final WorldGenLightningDragonCave LIGHTNING_DRAGON_CAVE = new WorldGenLightningDragonCave();
	private static final WorldGenLightningDragonRoost LIGHTNING_DRAGON_ROOST = new WorldGenLightningDragonRoost();
	private static final WorldGenCyclopsCave CYCLOPS_CAVE = new WorldGenCyclopsCave();
	private static final WorldGenSirenIsland SIREN_ISLAND = new WorldGenSirenIsland();
	private static final WorldGenHydraCave HYDRA_CAVE = new WorldGenHydraCave();
	private static final ResourceLocation GORGON_TEMPLE = new ResourceLocation(IceAndFire.MODID, "gorgon_temple");

	private BlockPos lastCyclopsCave = null;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(!genAllowedInDim(world.provider.getDimension())) return;
		double spawnCheck = IceAndFireConfig.WORLDGEN.dangerousWorldGenDistanceLimit * IceAndFireConfig.WORLDGEN.dangerousWorldGenDistanceLimit;
		int x = (chunkX * 16) + 8;
		int z = (chunkZ * 16) + 8;
		BlockPos height = world.getHeight(new BlockPos(x, 0, z));
		Biome biome = world.getBiome(height);
		String biomeName =  biome.getRegistryName() != null ? biome.getRegistryName().toString() : "";
		Set<Type> types = BiomeDictionary.getTypes(biome);

		//More common checks
		boolean isCold = types.contains(Type.COLD);
		boolean isSnowy = types.contains(Type.SNOWY);

		if(isFarEnoughFromSpawn(world, height)) {
			if(IceAndFireConfig.WORLDGEN.generateGorgonTemple && random.nextInt(IceAndFireConfig.WORLDGEN.generateGorgonChance) == 0 && types.contains(Type.BEACH) && world.getBlockState(height.down()).isFullBlock() && world.isAirBlock(height.up())) {
				Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
				Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
				MinecraftServer server = world.getMinecraftServer();
				TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
				PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(mirror);
				Template template = templateManager.getTemplate(server, GORGON_TEMPLE);
				BlockPos center = height.add(template.getSize().getX() / 2, -9, template.getSize().getZ() / 2);
				BlockPos corner1 = height.down();
				BlockPos corner2 = height.add(template.getSize().getX(), -1, 0);
				BlockPos corner3 = height.add(template.getSize().getX(), -1, template.getSize().getZ());
				BlockPos corner4 = height.add(0, -1, template.getSize().getZ());
				if(world.getBlockState(center).isOpaqueCube() && world.getBlockState(corner1).isOpaqueCube() && world.getBlockState(corner2).isOpaqueCube() && world.getBlockState(corner3).isOpaqueCube() && world.getBlockState(corner4).isOpaqueCube()) {
					template.addBlocksToWorldChunk(world, center, settings);
				}
			}

			if(IceAndFireConfig.WORLDGEN.generateSirenIslands && random.nextInt(IceAndFireConfig.WORLDGEN.generateSirenChance) == 0 && types.contains(Type.OCEAN) && !isCold) {
				SIREN_ISLAND.generate(world, random, height);
			}

			if(IceAndFireConfig.WORLDGEN.generateCyclopsCaves && random.nextInt(IceAndFireConfig.WORLDGEN.generateCyclopsChance) == 0 && types.contains(Type.BEACH) && world.getBlockState(height.down()).isOpaqueCube() && (lastCyclopsCave == null || lastCyclopsCave.distanceSq(height) >= spawnCheck)) {
				CYCLOPS_CAVE.generate(world, random, height);
				lastCyclopsCave = height;
			}
			if (IceAndFireConfig.WORLDGEN.generateWanderingCyclops && isFarEnoughFromSpawn(world, height) &&  BiomeDictionary.hasType(world.getBiome(height), Type.PLAINS) && (lastCyclopsCave == null || lastCyclopsCave.distanceSq(height) >= spawnCheck)) {
				if (random.nextInt(IceAndFireConfig.WORLDGEN.generateWanderingCyclopsChance + 1) == 0) {
					EntityCyclops cyclops = new EntityCyclops(world);
					cyclops.setPosition(x, height.getY() + 1, z);
					cyclops.setVariant(random.nextInt(3));
					if (!world.isRemote) {
						world.spawnEntity(cyclops);
					}
					for(int i = 0; i < 3 + random.nextInt(3); i++){
						EntitySheep sheep = new EntitySheep(world);
						sheep.setPosition(x, height.getY() + 1, z);
						sheep.setFleeceColor(EntitySheep.getRandomSheepColor(random));
						if (!world.isRemote) {
							world.spawnEntity(sheep);
						}
					}
					lastCyclopsCave = height;
				}
			}

			if(IceAndFireConfig.WORLDGEN.generatePixieVillages && random.nextInt(IceAndFireConfig.WORLDGEN.generatePixieChance) == 0 && types.contains(Type.FOREST) && (types.contains(Type.SPOOKY) || types.contains(Type.MAGICAL))) {
				PIXIE_VILLAGE.generate(world, random, height);
			}

			if(IceAndFireConfig.WORLDGEN.generateHydraCaves && random.nextInt(IceAndFireConfig.WORLDGEN.generateHydrasChance) == 0 && types.contains(Type.SWAMP) && world.getBlockState(height.down()).isOpaqueCube()) {
				HYDRA_CAVE.generate(world, random, height);
			}

			if ((IceAndFireConfig.WORLDGEN.generateDragonRoosts || IceAndFireConfig.WORLDGEN.generateDragonDens) && isDragonGenAllowedInDim(world.provider.getDimension()) && isDragonGenAllowedInBiome(types, biomeName)) {
				EnumDragonType dragonType = getDragonGenType(types, biome, biomeName, isCold, isSnowy);
				if (dragonType != null) {
					if (IceAndFireConfig.WORLDGEN.generateDragonRoosts) {
						Integer chance = IceAndFireConfig.getDragonRoostChance().get(biomeName);
						if (chance == null) {
							boolean isHills = types.contains(Type.HILLS) || types.contains(Type.MOUNTAIN) && !isSnowy;
							chance = isHills ? IceAndFireConfig.WORLDGEN.generateDragonRoostChance : IceAndFireConfig.WORLDGEN.generateDragonRoostChance * 2;
						}
						if (random.nextInt(chance) == 0) {
							if (dragonType == EnumDragonType.FIRE) {
								FIRE_DRAGON_ROOST.generate(world, random, height);
							} else if (dragonType == EnumDragonType.ICE) {
								ICE_DRAGON_ROOST.generate(world, random, height);
							} else if (dragonType == EnumDragonType.LIGHTNING) {
								LIGHTNING_DRAGON_ROOST.generate(world, random, height);
							}
						}
					}
					if (IceAndFireConfig.WORLDGEN.generateDragonDens && !types.contains(Type.BEACH) && !types.contains(Type.OCEAN)) {
						int newY = 20 + random.nextInt(20);
						BlockPos pos = new BlockPos(x, newY, z);
						if (!world.canBlockSeeSky(pos)) {
							Integer chance = IceAndFireConfig.getDragonDenChance().get(biomeName);
							if (chance == null) {
								boolean isHills = types.contains(Type.HILLS) || types.contains(Type.MOUNTAIN);
								chance = isHills ? IceAndFireConfig.WORLDGEN.generateDragonDenChance : IceAndFireConfig.WORLDGEN.generateDragonDenChance * 2;
							}
							if (random.nextInt(chance) == 0) {
								if (dragonType == EnumDragonType.FIRE) {
									FIRE_DRAGON_CAVE.generate(world, random, pos);
								} else if (dragonType == EnumDragonType.ICE) {
									ICE_DRAGON_CAVE.generate(world, random, pos);
								} else if (dragonType == EnumDragonType.LIGHTNING) {
									LIGHTNING_DRAGON_CAVE.generate(world, random, pos);
								}
							}
						}
					}
				}
			}

			if(IceAndFireConfig.WORLDGEN.generateDragonSkeletons) {
				//Chance is not nested inside so needs to be repeated each time
				if(random.nextInt(IceAndFireConfig.WORLDGEN.generateDragonSkeletonChance) == 0 && types.contains(Type.SAVANNA)) {
					EntityLightningDragon lightningdragon = new EntityLightningDragon(world);
					lightningdragon.setPosition(x, height.getY() + 1, z);
					int dragonage = 10 + random.nextInt(IceAndFireConfig.WORLDGEN.generateDragonSkeletonMaximumStage) * 25;
					lightningdragon.growDragon(dragonage);
					lightningdragon.modelDeadProgress = 20;
					lightningdragon.setModelDead(true);
					lightningdragon.setDeathStage((dragonage / 5) / 2);
					lightningdragon.rotationYaw = random.nextInt(360);
					if(!world.isRemote) world.spawnEntity(lightningdragon);
				} else if(random.nextInt(IceAndFireConfig.WORLDGEN.generateDragonSkeletonChance) == 0 && types.contains(Type.DRY) && types.contains(Type.SANDY)) {
					EntityFireDragon firedragon = new EntityFireDragon(world);
					firedragon.setPosition(x, height.getY() + 1, z);
					int dragonage = 10 + random.nextInt(IceAndFireConfig.WORLDGEN.generateDragonSkeletonMaximumStage) * 25;
					firedragon.growDragon(dragonage);
					firedragon.modelDeadProgress = 20;
					firedragon.setModelDead(true);
					firedragon.setDeathStage((dragonage / 5) / 2);
					firedragon.rotationYaw = random.nextInt(360);
					if(!world.isRemote) world.spawnEntity(firedragon);
				} else if(random.nextInt(IceAndFireConfig.WORLDGEN.generateDragonSkeletonChance) == 0 && isCold && isSnowy) {
					EntityIceDragon icedragon = new EntityIceDragon(world);
					icedragon.setPosition(x, height.getY() + 1, z);
					int dragonage = 10 + random.nextInt(IceAndFireConfig.WORLDGEN.generateDragonSkeletonMaximumStage) * 25;
					icedragon.growDragon(dragonage);
					icedragon.modelDeadProgress = 20;
					icedragon.setModelDead(true);
					icedragon.setDeathStage((dragonage / 5) / 2);
					icedragon.rotationYaw = random.nextInt(360);
					if(!world.isRemote) world.spawnEntity(icedragon);
				}
			}

			if(IceAndFireConfig.ENTITY_SPAWNING.spawnSeaSerpents && random.nextInt(IceAndFireConfig.ENTITY_SPAWNING.seaSerpentSpawnChance) == 0 && types.contains(Type.OCEAN)) {
				BlockPos pos = new BlockPos(x + random.nextInt(10) - 5, 20 + random.nextInt(40), z + random.nextInt(10) - 5);
				if(world.getBlockState(pos).getMaterial() == Material.WATER){
					EntitySeaSerpent serpent = new EntitySeaSerpent(world);
					serpent.onWorldSpawn(random);
					serpent.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
					if(!world.isRemote) world.spawnEntity(serpent);
				}
			}

			if(IceAndFireConfig.ENTITY_SPAWNING.spawnStymphalianBirds && random.nextInt(IceAndFireConfig.ENTITY_SPAWNING.stymphalianBirdSpawnChance) == 0 && types.contains(Type.SWAMP)) {
				for(int i = 0; i < 4 + random.nextInt(4); i++){
					BlockPos pos = height.add(random.nextInt(10) - 5, 0, random.nextInt(10) - 5);
					if(world.getBlockState(pos.down()).isOpaqueCube()){
						EntityStymphalianBird bird = new EntityStymphalianBird(world);
						bird.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
						if(bird.isNotColliding() && !world.isRemote) world.spawnEntity(bird);
					}
				}
			}

			if(IceAndFireConfig.WORLDGEN.generateMyrmexColonies && isMyrmexGenAllowedInBiome(types, biomeName) && random.nextInt(IceAndFireConfig.WORLDGEN.myrmexColonyGenChance) == 0 && (types.contains(Type.JUNGLE) || types.contains(Type.HOT) && types.contains(Type.DRY) && types.contains(Type.SANDY)) && MyrmexWorldData.get(world).getNearestHive(height, 500) == null) {
				BlockPos lowestHeight = new BlockPos(height.getX(), world.getChunksLowestHorizon(height.getX(), height.getZ()), height.getZ());
				int down = Math.max(15, lowestHeight.getY() - 20 + random.nextInt(10));
				if(types.contains(Type.JUNGLE)) JUNGLE_MYRMEX_HIVE.generate(world, random, new BlockPos(lowestHeight.getX(), down, lowestHeight.getZ()));
				else DESERT_MYRMEX_HIVE.generate(world, random, new BlockPos(lowestHeight.getX(), down, lowestHeight.getZ()));
			}
		}

		if(IceAndFireConfig.WORLDGEN.generateSnowVillages && isVillageGenAllowedInDim(world.provider.getDimension()) && isCold && isSnowy) {
			SNOW_VILLAGE.generate(world, random, height);
		}

		if(IceAndFireConfig.ENTITY_SPAWNING.spawnHippocampus && random.nextInt(IceAndFireConfig.ENTITY_SPAWNING.hippocampusSpawnChance) == 0 && types.contains(Type.OCEAN)) {
			for(int i = 0; i < random.nextInt(5); i++){
				BlockPos pos = new BlockPos(x + random.nextInt(10) - 5, 20 + random.nextInt(40), z + random.nextInt(10) - 5);
				if(world.getBlockState(pos).getMaterial() == Material.WATER){
					EntityHippocampus campus = new EntityHippocampus(world);
					campus.setVariant(random.nextInt(5));
					campus.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
					if(campus.isNotColliding() && !world.isRemote) world.spawnEntity(campus);
				}
			}
		}

		if(IceAndFireConfig.WORLDGEN.generateCopperOre) {
			for(int copperAmount = 0; copperAmount < 2; copperAmount++) {
				int oreHeight = random.nextInt(128);
				int xOre = (chunkX * 16) + random.nextInt(16);
				int zOre = (chunkZ * 16) + random.nextInt(16);
				new WorldGenMinable(ModBlocks.copperOre.getDefaultState(), 4 + random.nextInt(4)).generate(world, random, new BlockPos(xOre, oreHeight, zOre));
			}
		}

		if(IceAndFireConfig.WORLDGEN.generateSilverOre) {
			for(int silverAmount = 0; silverAmount < 2; silverAmount++) {
				int oreHeight = random.nextInt(32);
				int xOre = (chunkX * 16) + random.nextInt(16);
				int zOre = (chunkZ * 16) + random.nextInt(16);
				new WorldGenMinable(ModBlocks.silverOre.getDefaultState(), 4 + random.nextInt(4)).generate(world, random, new BlockPos(xOre, oreHeight, zOre));
			}
		}

		if(IceAndFireConfig.WORLDGEN.generateAmethystOre) {
			if(types.contains(Type.SAVANNA)) {
				int count = 3 + random.nextInt(6);
				for(int amethystAmount = 0; amethystAmount < count; amethystAmount++) {
					int oreHeight = random.nextInt(28) + 4;
					int xOre = (chunkX * 16) + random.nextInt(16);
					int zOre = (chunkZ * 16) + random.nextInt(16);
					BlockPos pos = new BlockPos(xOre, oreHeight, zOre);
					IBlockState state = world.getBlockState(pos);
					if(state.getBlock().isReplaceableOreGen(state, world, pos, BlockMatcher.forBlock(Blocks.STONE))) {
						world.setBlockState(pos, ModBlocks.amethystOre.getDefaultState());
					}
				}
			}
		}

		if(IceAndFireConfig.WORLDGEN.generateSapphireOre) {
			if(isSnowy) {
				int count = 3 + random.nextInt(6);
				for(int sapphireAmount = 0; sapphireAmount < count; sapphireAmount++) {
					int oreHeight = random.nextInt(28) + 4;
					int xOre = (chunkX * 16) + random.nextInt(16);
					int zOre = (chunkZ * 16) + random.nextInt(16);
					BlockPos pos = new BlockPos(xOre, oreHeight, zOre);
					IBlockState state = world.getBlockState(pos);
					if(state.getBlock().isReplaceableOreGen(state, world, pos, BlockMatcher.forBlock(Blocks.STONE))) {
						world.setBlockState(pos, ModBlocks.sapphireOre.getDefaultState());
					}
				}
			}
		}

		if(random.nextInt(5) == 0) {
			if(types.contains(Type.SAVANNA)) {
				if(ModBlocks.lightning_lily.canPlaceBlockAt(world, height)) {
					world.setBlockState(height, ModBlocks.lightning_lily.getDefaultState());
				}
			} else if(isCold && isSnowy) {
				if (ModBlocks.frost_lily.canPlaceBlockAt(world, height)) {
					world.setBlockState(height, ModBlocks.frost_lily.getDefaultState());
				}
			} else if(types.contains(Type.HOT) && (types.contains(Type.SANDY))){
				if (ModBlocks.fire_lily.canPlaceBlockAt(world, height)) {
					world.setBlockState(height, ModBlocks.fire_lily.getDefaultState());
				}
			}
		}

		if(random.nextInt(5) == 0) {
			if(types.contains(Type.NETHER)){
				BlockPos surface = getNetherHeight(world, new BlockPos(x, 0, z));
				if(surface != null){
					world.setBlockState(surface.up(), ModBlocks.fire_lily.getDefaultState());
				}
			}
		}
	}

	public static void generateMyrmexHiveForQueen(EntityMyrmexQueen queen, Random rand, BlockPos position) {
		if (queen.isJungle()) {
			JUNGLE_MYRMEX_HIVE.generateForQueen(queen, rand, position);
		} else {
			DESERT_MYRMEX_HIVE.generateForQueen(queen, rand, position);
		}
	}

	private static boolean isFarEnoughFromSpawn(World world, BlockPos pos){
		if(IceAndFireConfig.WORLDGEN.dangerousWorldGenDistanceLimit == 0) return true;
		BlockPos spawnRelative = new BlockPos(world.getSpawnPoint().getX(), pos.getY(), world.getSpawnPoint().getZ());
		return spawnRelative.distanceSq(pos) > IceAndFireConfig.WORLDGEN.dangerousWorldGenDistanceLimit * IceAndFireConfig.WORLDGEN.dangerousWorldGenDistanceLimit;
	}

	private static boolean genAllowedInDim(int id) {
		for(int i : IceAndFireConfig.WORLDGEN.chunkGenBlacklist) {
			if(i == id) return IceAndFireConfig.WORLDGEN.chunkGenWhitelist;
		}
		return !IceAndFireConfig.WORLDGEN.chunkGenWhitelist;
	}

	private static boolean isDragonGenAllowedInDim(int id) {
		for(int i : IceAndFireConfig.WORLDGEN.dragonDimensionBlacklistedDimensions) {
			if(i == id) return IceAndFireConfig.WORLDGEN.dragonDimensionWhitelist;
		}
		return !IceAndFireConfig.WORLDGEN.dragonDimensionWhitelist;
	}

	private static boolean isVillageGenAllowedInDim(int id) {
		for(int i : IceAndFireConfig.WORLDGEN.snowVillageBlacklistedDimensions) {
			if(i == id) return IceAndFireConfig.WORLDGEN.snowVillageWhitelist;
		}
		return !IceAndFireConfig.WORLDGEN.snowVillageWhitelist;
	}

	private static boolean isMyrmexGenAllowedInBiome(Set<BiomeDictionary.Type> dictSet, String biomeName) {
		if(IceAndFireConfig.getMyrmexDisabledNames().contains(biomeName)) return false;
		for(Type type : IceAndFireConfig.getMyrmexDisabledTypes()) {
			if(dictSet.contains(type)) return false;
		}
		return true;
	}

	private static boolean isDragonGenAllowedInBiome(Set<BiomeDictionary.Type> dictSet, String biomeName) {
		if(IceAndFireConfig.getDragonDisabledNames().contains(biomeName)) return false;
		if(IceAndFireConfig.getFireDragonEnabledNames().contains(biomeName)
				|| IceAndFireConfig.getIceDragonEnabledNames().contains(biomeName)
				|| IceAndFireConfig.getLightningDragonEnabledNames().contains(biomeName)) return true;
		for(Type type : IceAndFireConfig.getDragonDisabledTypes()) {
			if(dictSet.contains(type)) return false;
		}
		return true;
	}

	@Nullable
	private EnumDragonType getDragonGenType(Set<BiomeDictionary.Type> dictSet, Biome biome, String biomeName, boolean isCold, boolean isSnowy) {
		if (IceAndFireConfig.getFireDragonEnabledNames().contains(biomeName)) {
			return EnumDragonType.FIRE;
		} else if (IceAndFireConfig.getIceDragonEnabledNames().contains(biomeName)) {
			return EnumDragonType.ICE;
		} else if (IceAndFireConfig.getLightningDragonEnabledNames().contains(biomeName) || dictSet.contains(Type.JUNGLE) || dictSet.contains(Type.SAVANNA) || dictSet.contains(Type.MESA)) {
			return EnumDragonType.LIGHTNING;
		} else if (!biome.getEnableSnow() && biome.getDefaultTemperature() > -0.5 && !isCold && !isSnowy && !dictSet.contains(Type.WET) && !dictSet.contains(Type.OCEAN) && !dictSet.contains(Type.RIVER)) {
			return EnumDragonType.FIRE;
		} else if (isCold && isSnowy) {
			return EnumDragonType.ICE;
		}
		return null;
	}

	//TODO: Is there a better method for this?
	private BlockPos getNetherHeight(World world, BlockPos pos){
		for(int i = 0; i < 255; i++){
			BlockPos ground = pos.up(i);
			if(world.getBlockState(ground).getBlock() == Blocks.NETHERRACK && world.isAirBlock(ground.up())){
				return ground;
			}
		}
		return null;
	}
}