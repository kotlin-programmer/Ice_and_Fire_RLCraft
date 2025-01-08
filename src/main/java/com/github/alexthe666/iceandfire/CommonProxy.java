package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.block.*;
import com.github.alexthe666.iceandfire.client.particle.lightning.ParticleLightningVector;
import com.github.alexthe666.iceandfire.core.*;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.projectile.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumParticle;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.block.ItemBlockMyrmexResin;
import com.github.alexthe666.iceandfire.item.block.ItemBlockPodium;
import com.github.alexthe666.iceandfire.world.BiomeGlacier;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class CommonProxy {

    @SubscribeEvent
    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
        try {
            for (Field f : ModSounds.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof SoundEvent) {
                    event.getRegistry().register((SoundEvent) obj);
                } else if (obj instanceof SoundEvent[]) {
                    for (SoundEvent soundEvent : (SoundEvent[]) obj) {
                        event.getRegistry().register(soundEvent);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<PotionType> event) {
        ModPotions.register();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        try {
            for (Field f : ModBlocks.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    event.getRegistry().register((Block) obj);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        event.getRegistry().register(block);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        registerUnspawnable(EntityEntryBuilder.<EntityDragonEgg>create(), event, EntityDragonEgg.class, "dragonegg", 1, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonArrow>create(), event, EntityDragonArrow.class, "dragonarrow", 2, 64);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonSkull>create(), event, EntityDragonSkull.class, "dragonskull", 3, 80);
        registerSpawnable(EntityEntryBuilder.<EntityFireDragon>create(), event, EntityFireDragon.class, "firedragon", 4, 0X340000, 0XA52929, 128);
        registerSpawnable(EntityEntryBuilder.<EntityIceDragon>create(), event, EntityIceDragon.class, "icedragon", 5, 0XB5DDFB, 0X7EBAF0, 128);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonFire>create(), event, EntityDragonFire.class, "dragonfire", 6, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonIce>create(), event, EntityDragonIce.class, "dragonice", 7, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonLightning>create(), event, EntityDragonLightning.class, "dragonlightning", 8, 80);
        registerSpawnable(EntityEntryBuilder.<EntityLightningDragon>create(), event, EntityLightningDragon.class, "lightningdragon", 9, 0X422367, 0X725691, 128);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonFireCharge>create(), event, EntityDragonFireCharge.class, "dragonfirecharge", 10, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonIceCharge>create(), event, EntityDragonIceCharge.class, "dragonicecharge", 11, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonLightningCharge>create(), event, EntityDragonLightningCharge.class, "dragonlightningcharge", 12, 80);
        registerSpawnable(EntityEntryBuilder.<EntitySnowVillager>create(), event, EntitySnowVillager.class, "snowvillager", 13, 0X3C2A23, 0X70B1CF, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityHippogryphEgg>create(), event, EntityHippogryphEgg.class, "hippogryphegg", 14, 64);
        registerSpawnable(EntityEntryBuilder.<EntityHippogryph>create(), event, EntityHippogryph.class, "hippogryph", 15, 0XD8D8D8, 0XD1B55D, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityStoneStatue>create(), event, EntityStoneStatue.class, "stonestatue", 16, 80);
        registerSpawnable(EntityEntryBuilder.<EntityGorgon>create(), event, EntityGorgon.class, "gorgon", 17, 0XD0D99F, 0X684530, 80);
        registerSpawnable(EntityEntryBuilder.<EntityPixie>create(), event, EntityPixie.class, "if_pixie", 18, 0XFF7F89, 0XE2CCE2, 80);
        registerSpawnable(EntityEntryBuilder.<EntityCyclops>create(), event, EntityCyclops.class, "cyclops", 19, 0XB0826E, 0X3A1F0F, 120);
        registerSpawnable(EntityEntryBuilder.<EntitySiren>create(), event, EntitySiren.class, "siren", 20, 0X8EE6CA, 0XF2DFC8, 120);
        registerSpawnable(EntityEntryBuilder.<EntityHippocampus>create(), event, EntityHippocampus.class, "hippocampus", 21, 0X4491C7, 0X4FC56B, 80);
        registerSpawnable(EntityEntryBuilder.<EntityDeathWorm>create(), event, EntityDeathWorm.class, "deathworm", 22, 0XD1CDA3, 0X423A3A, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityDeathWormEgg>create(), event, EntityDeathWormEgg.class, "deathwormegg", 23, 64);
        registerSpawnable(EntityEntryBuilder.<EntityCockatrice>create(), event, EntityCockatrice.class, "if_cockatrice", 24, 0X8F5005, 0X4F5A23, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityCockatriceEgg>create(), event, EntityCockatriceEgg.class, "if_cockatriceegg", 25, 64);
        registerSpawnable(EntityEntryBuilder.<EntityStymphalianBird>create(), event, EntityStymphalianBird.class, "stymphalianbird", 26, 0X744F37, 0X9E6C4B, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityStymphalianFeather>create(), event, EntityStymphalianFeather.class, "stymphalianfeather", 27, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityStymphalianArrow>create(), event, EntityStymphalianArrow.class, "stymphalianarrow", 28, 64);
        registerSpawnable(EntityEntryBuilder.<EntityTroll>create(), event, EntityTroll.class, "if_troll", 29, 0X3D413D, 0X58433A, 80);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexWorker>create(), event,EntityMyrmexWorker.class, "myrmex_worker", 30, 0XA16026, 0X594520, 80);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexSoldier>create(), event,EntityMyrmexSoldier.class, "myrmex_soldier", 31, 0XA16026, 0X7D622D, 80);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexSentinel>create(), event,EntityMyrmexSentinel.class, "myrmex_sentinel", 32, 0XA16026, 0XA27F3A, 80);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexRoyal>create(), event,EntityMyrmexRoyal.class, "myrmex_royal", 33, 0XA16026, 0XC79B48, 80);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexQueen>create(), event,EntityMyrmexQueen.class, "myrmex_queen", 34, 0XA16026, 0XECB855, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityMyrmexEgg>create(), event,EntityMyrmexEgg.class, "myrmex_egg", 35, 80);
        registerSpawnable(EntityEntryBuilder.<EntityAmphithere>create(), event, EntityAmphithere.class, "amphithere", 36, 0X597535, 0X00AA98, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityAmphithereArrow>create(), event, EntityAmphithereArrow.class, "amphitherearrow", 37, 64);
        registerSpawnable(EntityEntryBuilder.<EntitySeaSerpent>create(), event, EntitySeaSerpent.class, "seaserpent", 38, 0X008299, 0XC5E6E7, 128);
        registerUnspawnable(EntityEntryBuilder.<EntitySeaSerpentBubbles>create(), event, EntitySeaSerpentBubbles.class, "seaserpentbubble", 39, 80);
        registerUnspawnable(EntityEntryBuilder.<EntitySeaSerpentArrow>create(), event, EntitySeaSerpentArrow.class, "seaserpentarrow", 40, 64);
        registerUnspawnable(EntityEntryBuilder.<EntityHydraArrow>create(), event, EntityHydraArrow.class, "hydra_arrow", 41, 64);
        registerSpawnable(EntityEntryBuilder.<EntityHydra>create(), event, EntityHydra.class, "if_hydra", 42, 0X8B8B78, 0X2E372B, 128);
        registerUnspawnable(EntityEntryBuilder.<EntityHydraBreath>create(), event, EntityHydraBreath.class, "hydra_breath", 43, 80);
        registerUnspawnable(EntityEntryBuilder.<EntityTideTrident>create(), event, EntityTideTrident.class, "tide_trident", 44, 64);
        registerSpawnable(EntityEntryBuilder.<EntityDreadThrall>create(), event, EntityDreadThrall.class, "dread_thrall", 45, 0XE0E6E6, 0X00FFFF, 64);
        registerSpawnable(EntityEntryBuilder.<EntityDreadGhoul>create(), event, EntityDreadGhoul.class, "dread_ghoul", 46, 0XE0E6E6, 0X7B838A, 64);
        registerSpawnable(EntityEntryBuilder.<EntityDreadBeast>create(), event, EntityDreadBeast.class, "dread_beast", 47, 0XE0E6E6, 0X38373C, 64);
        registerSpawnable(EntityEntryBuilder.<EntityDreadScuttler>create(), event, EntityDreadScuttler.class, "dread_scuttler", 48, 0XE0E6E6, 0X4D5667, 64);
        registerSpawnable(EntityEntryBuilder.<EntityDreadLich>create(), event, EntityDreadLich.class, "dread_lich", 49, 0XE0E6E6, 0X274860, 64);
        registerUnspawnable(EntityEntryBuilder.<EntityDreadLichSkull>create(), event, EntityDreadLichSkull.class, "dread_lich_skull", 50, 64);
        registerSpawnable(EntityEntryBuilder.<EntityDreadKnight>create(), event, EntityDreadKnight.class, "dread_knight", 51, 0XE0E6E6, 0X4A6C6E, 64);
        registerSpawnable(EntityEntryBuilder.<EntityDreadHorse>create(), event, EntityDreadHorse.class, "dread_horse", 52, 0XE0E6E6, 0XACACAC, 64);
        registerSpawnable(EntityEntryBuilder.create(), event, EntityGhost.class, "ghost", 53, 0XB9EDB8, 0X73B276, 64);
        registerUnspawnable(EntityEntryBuilder.create(), event, EntityGhostSword.class, "ghost_sword", 54,  64);
        registerUnspawnable(EntityEntryBuilder.<EntityMobSkull>create(), event, EntityMobSkull.class, "if_mob_skull", 55, 64);
    }

    public static void registerSpawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id, int mainColor, int subColor, int range) {
        id += 900;
        builder.entity(entityClass);
        builder.id(new ResourceLocation(IceAndFire.MODID, name), id);
        builder.name(name);
        builder.egg(mainColor, subColor);
        builder.tracker(range, 1, true);
        event.getRegistry().register(builder.build());
    }

    public static void registerUnspawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id, int range) {
        id += 900;
        builder.entity(entityClass);
        builder.id(new ResourceLocation(IceAndFire.MODID, name), id);
        builder.name(name);
        builder.tracker(range, 1, true);
        event.getRegistry().register(builder.build());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        // ItemBlocks
        try {
            for (Field f : ModBlocks.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    ItemBlock itemBlock;
                    if (obj == ModBlocks.jar_pixie) {
                        itemBlock = new BlockJar.ItemBlockJar((Block) obj);
                    } else if (obj instanceof BlockPixieHouse) {
                        itemBlock = new BlockPixieHouse.ItemBlockPixieHouse((Block) obj);
                    } else if (obj instanceof BlockPodium) {
                        itemBlock = new ItemBlockPodium((Block) obj);
                    } else if (obj instanceof BlockMyrmexResin) {
                        itemBlock = new ItemBlockMyrmexResin((Block) obj);
                    } else if (obj instanceof BlockGenericSlab) {
                        itemBlock = ((BlockGenericSlab) obj).getItemBlock();
                    } else {
                        itemBlock = new ItemBlock((Block) obj);
                    }
                    itemBlock.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(itemBlock);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        ItemBlock itemBlock = new ItemBlock(block);
                        itemBlock.setRegistryName(block.getRegistryName());
                        event.getRegistry().register(itemBlock);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Items
        try {
            for (Field f : ModItems.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item) {
                    event.getRegistry().register((Item) obj);
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        event.getRegistry().register(item);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            event.getRegistry().register(armor.helmet);
            event.getRegistry().register(armor.chestplate);
            event.getRegistry().register(armor.leggings);
            event.getRegistry().register(armor.boots);
        }
        for (EnumSeaSerpent armor : EnumSeaSerpent.values()) {
            event.getRegistry().register(armor.scale);
            event.getRegistry().register(armor.helmet);
            event.getRegistry().register(armor.chestplate);
            event.getRegistry().register(armor.leggings);
            event.getRegistry().register(armor.boots);
        }
        for (EnumTroll.Weapon weapon : EnumTroll.Weapon.values()) {
            event.getRegistry().register(weapon.item);
        }
        for (EnumTroll troll : EnumTroll.values()) {
            event.getRegistry().register(troll.helmet);
            event.getRegistry().register(troll.chestplate);
            event.getRegistry().register(troll.leggings);
            event.getRegistry().register(troll.boots);
        }
        for (EnumSkullType skull : EnumSkullType.values()) {
            event.getRegistry().register(skull.skull_item);
        }
        ModRecipes.preInit();
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        IceAndFire.GLACIER = new BiomeGlacier().setRegistryName(IceAndFire.MODID, "Glacier");
        event.getRegistry().register(IceAndFire.GLACIER);
        BiomeDictionary.addTypes(IceAndFire.GLACIER, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.WASTELAND);
        if (IceAndFireConfig.WORLDGEN.spawnGlaciers) {
            BiomeManager.addSpawnBiome(IceAndFire.GLACIER);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(IceAndFire.GLACIER, IceAndFireConfig.WORLDGEN.glacierSpawnChance));

        }
    }

    @SubscribeEvent
    public static void handleOreRegistration(OreDictionary.OreRegisterEvent event) {
        ModRecipes.handleOreRegistration(event.getName(), event.getOre());
    }

    public void preRender() {

    }

    public void render() {
    }

    public void postRender() {
    }

    public void spawnParticle(EnumParticle type, World world, double x, double y, double z, double motX, double motY, double motZ) {
    }

    public void spawnLightningEffect(World world, ParticleLightningVector sourceVec, ParticleLightningVector targetVec, boolean isProjectile) {
    }

    public void openBestiaryGui(ItemStack book) {
    }

    public void openMyrmexStaffGui(ItemStack staff) {
    }

    public Object getArmorModel(int armorId) {
        return null;
    }

    public Object getFontRenderer() {
        return null;
    }

    public int getDragon3rdPersonView() {
        return 0;
    }

    public void setDragon3rdPersonView(int view) {
    }

    public void updateDragonArmorRender(String clear){
    }

    public void openMyrmexAddRoomGui(ItemStack staff, BlockPos pos, EnumFacing facing) {
    }
}
