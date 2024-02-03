package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Set;

public class ModEntities {

	public static void init() {
		for(Biome biome : Biome.REGISTRY) {
			if(biome != null) {
				Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);
				List<Biome.SpawnListEntry> creatureList = biome.getSpawnableList(EnumCreatureType.CREATURE);

				if(IceAndFireConfig.ENTITY_SPAWNING.spawnHippogryphs) {
					for(EnumHippogryphTypes variant : EnumHippogryphTypes.values()) {
						if(!variant.developer) {
							if(types.contains(BiomeDictionary.Type.HILLS)) {
								creatureList.add(new Biome.SpawnListEntry(EntityHippogryph.class, IceAndFireConfig.ENTITY_SPAWNING.hippogryphSpawnRate, 1, 1));
							}
						}
					}
				}
				if(IceAndFireConfig.ENTITY_SPAWNING.spawnDeathWorm) {
					if(types.contains(BiomeDictionary.Type.SANDY) && types.contains(BiomeDictionary.Type.DRY) && !types.contains(BiomeDictionary.Type.BEACH) && !types.contains(BiomeDictionary.Type.MESA)) {
						creatureList.add(new Biome.SpawnListEntry(EntityDeathWorm.class, IceAndFireConfig.ENTITY_SPAWNING.deathWormSpawnRate, 1, 3));
					}
				}
				if(IceAndFireConfig.ENTITY_SPAWNING.spawnTrolls) {
					List<Biome.SpawnListEntry> monsterList = biome.getSpawnableList(EnumCreatureType.MONSTER);
					for(EnumTroll type : EnumTroll.values()) {
						if(types.contains(type.spawnBiome)) {
							monsterList.add(new Biome.SpawnListEntry(EntityTroll.class, IceAndFireConfig.ENTITY_SPAWNING.trollSpawnRate, 1, 1));
						}
					}
				}
				if (IceAndFireConfig.ENTITY_SPAWNING.spawnLiches) {
					if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
						List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);
						spawnList.add(new Biome.SpawnListEntry(EntityDreadLich.class, IceAndFireConfig.ENTITY_SPAWNING.lichSpawnRate, 1, 1));
					}
				}
				if(IceAndFireConfig.ENTITY_SPAWNING.spawnCockatrices) {
					if(types.contains(BiomeDictionary.Type.SAVANNA) && types.contains(BiomeDictionary.Type.SPARSE)) {
						creatureList.add(new Biome.SpawnListEntry(EntityCockatrice.class, IceAndFireConfig.ENTITY_SPAWNING.cockatriceSpawnRate, 1, 2));
					}
				}
				if(IceAndFireConfig.ENTITY_SPAWNING.spawnAmphitheres) {
					if(types.contains(BiomeDictionary.Type.JUNGLE)) {
						creatureList.add(new Biome.SpawnListEntry(EntityAmphithere.class, IceAndFireConfig.ENTITY_SPAWNING.amphithereSpawnRate, 1, 3));
					}
				}
			}
		}
	}
}