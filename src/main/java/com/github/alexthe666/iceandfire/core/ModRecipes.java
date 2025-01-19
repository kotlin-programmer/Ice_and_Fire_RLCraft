package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.projectile.EntityAmphithereArrow;
import com.github.alexthe666.iceandfire.entity.projectile.*;
import com.github.alexthe666.iceandfire.enums.EnumBloodedDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.world.World;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ModRecipes {

    public static List<ItemStack> BANNER_ITEMS = new ArrayList<>();

    public static void preInit() {

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.stymphalian_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.amphithere_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.sea_serpent_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.dragonbone_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.hydra_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHydraArrow entityarrow = new EntityHydraArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.hippogryph_egg, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntityHippogryphEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.rotten_egg, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntityCockatriceEgg(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.deathworm_egg, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntityDeathWormEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn.getMetadata() == 1);
            }
        });

        OreDictionary.registerOre("ingotCopper", IafItemRegistry.copperIngot);
        OreDictionary.registerOre("nuggetCopper", IafItemRegistry.copperNugget);
        OreDictionary.registerOre("oreCopper", IafBlockRegistry.copperOre);
        OreDictionary.registerOre("blockCopper", IafBlockRegistry.copperBlock);
        OreDictionary.registerOre("ingotSilver", IafItemRegistry.silverIngot);
        OreDictionary.registerOre("nuggetSilver", IafItemRegistry.silverNugget);
        OreDictionary.registerOre("oreSilver", IafBlockRegistry.silverOre);
        OreDictionary.registerOre("blockSilver", IafBlockRegistry.silverBlock);
        OreDictionary.registerOre("gemAmethyst", IafItemRegistry.amethystGem);
        OreDictionary.registerOre("oreAmethyst", IafBlockRegistry.amethystOre);
        OreDictionary.registerOre("blockAmethyst", IafBlockRegistry.amethystBlock);
        OreDictionary.registerOre("gemSapphire", IafItemRegistry.sapphireGem);
        OreDictionary.registerOre("oreSapphire", IafBlockRegistry.sapphireOre);
        OreDictionary.registerOre("blockSapphire", IafBlockRegistry.sapphireBlock);
        OreDictionary.registerOre("boneWither", IafItemRegistry.witherbone);
        OreDictionary.registerOre("woolBlock", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodMeat", Items.CHICKEN);
        OreDictionary.registerOre("foodMeat", Items.COOKED_CHICKEN);
        OreDictionary.registerOre("foodMeat", Items.BEEF);
        OreDictionary.registerOre("foodMeat", Items.COOKED_BEEF);
        OreDictionary.registerOre("foodMeat", Items.PORKCHOP);
        OreDictionary.registerOre("foodMeat", Items.COOKED_PORKCHOP);
        OreDictionary.registerOre("foodMeat", Items.MUTTON);
        OreDictionary.registerOre("foodMeat", Items.COOKED_MUTTON);
        OreDictionary.registerOre("foodMeat", Items.RABBIT);
        OreDictionary.registerOre("foodMeat", Items.COOKED_RABBIT);
        OreDictionary.registerOre("boneWithered", IafItemRegistry.witherbone);
        OreDictionary.registerOre("boneDragon", IafItemRegistry.dragonbone);
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            OreDictionary.registerOre("seaSerpentScales", serpent.scale);
        }
        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("dragonSkull",  new ItemStack(IafItemRegistry.dragon_skull, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("mythicalSkull",  new ItemStack(IafItemRegistry.dragon_skull, 1, OreDictionary.WILDCARD_VALUE));
        for (EnumSkullType skullType : EnumSkullType.values()) {
            OreDictionary.registerOre("mythicalSkull", skullType.skull_item);
        }

        addBanner("firedragon", new ItemStack(IafItemRegistry.fire_dragon_heart));
        addBanner("icedragon", new ItemStack(IafItemRegistry.ice_dragon_heart));
        addBanner("lightningdragon", new ItemStack(IafItemRegistry.lightning_dragon_heart));
        addBanner("firedragon_head", new ItemStack(IafItemRegistry.dragon_skull, 1, 0));
        addBanner("icedragon_head", new ItemStack(IafItemRegistry.dragon_skull, 1, 1));
        addBanner("lightningdragon_head", new ItemStack(IafItemRegistry.dragon_skull, 1, 2));
        addBanner("amphithere", new ItemStack(IafItemRegistry.amphithere_feather));
        addBanner("sea_serpent", new ItemStack(IafItemRegistry.sea_serpent_fang));
        addBanner("stymphalian_bird", new ItemStack(IafItemRegistry.stymphalian_bird_feather));
        addBanner("hippocampus", new ItemStack(IafItemRegistry.hippocampus_fin));
        addBanner("hippogryph", new ItemStack(EnumSkullType.HIPPOGRYPH.skull_item));
        addBanner("troll", new ItemStack(IafItemRegistry.troll_tusk));
        addBanner("gorgon", new ItemStack(IafItemRegistry.gorgon_head));
        addBanner("feather", new ItemStack(Items.FEATHER));
        addBanner("dread", new ItemStack(IafItemRegistry.dread_shard));
        GameRegistry.addSmelting(IafBlockRegistry.copperOre, new ItemStack(IafItemRegistry.copperIngot), 1);
        GameRegistry.addSmelting(IafBlockRegistry.silverOre, new ItemStack(IafItemRegistry.silverIngot), 1);
        GameRegistry.addSmelting(IafBlockRegistry.amethystOre, new ItemStack(IafItemRegistry.amethystGem), 1);
        GameRegistry.addSmelting(IafBlockRegistry.sapphireOre, new ItemStack(IafItemRegistry.sapphireGem), 1);
        GameRegistry.addSmelting(IafBlockRegistry.myrmex_desert_resin_block, new ItemStack(IafBlockRegistry.myrmex_desert_resin_glass), 1);
        GameRegistry.addSmelting(IafBlockRegistry.myrmex_jungle_resin_block, new ItemStack(IafBlockRegistry.myrmex_jungle_resin_glass), 1);

        if (IceAndFireConfig.WORLDGEN.generateCopperOre) {
            GameRegistry.addSmelting(IafItemRegistry.stymphalian_bird_feather, new ItemStack(IafItemRegistry.copperNugget), 1);
        }

        GameRegistry.addSmelting(IafItemRegistry.silver_helmet, new ItemStack(IafItemRegistry.silverIngot, 2), 1);
        GameRegistry.addSmelting(IafItemRegistry.silver_chestplate, new ItemStack(IafItemRegistry.silverIngot, 3), 1);
        GameRegistry.addSmelting(IafItemRegistry.silver_leggings, new ItemStack(IafItemRegistry.silverIngot, 3), 1);
        GameRegistry.addSmelting(IafItemRegistry.silver_boots, new ItemStack(IafItemRegistry.silverIngot, 1), 1);

        GameRegistry.addSmelting(IafItemRegistry.silver_pickaxe, new ItemStack(IafItemRegistry.silverIngot), 1);
        GameRegistry.addSmelting(IafItemRegistry.silver_axe, new ItemStack(IafItemRegistry.silverIngot), 1);
        GameRegistry.addSmelting(IafItemRegistry.silver_sword, new ItemStack(IafItemRegistry.silverNugget, 4), 1);
        GameRegistry.addSmelting(IafItemRegistry.silver_hoe, new ItemStack(IafItemRegistry.silverNugget, 4), 1);
        GameRegistry.addSmelting(IafItemRegistry.silver_shovel, new ItemStack(IafItemRegistry.silverNugget, 1), 1);

        GameRegistry.addSmelting(IafItemRegistry.copper_helmet, new ItemStack(IafItemRegistry.copperIngot, 2), 1);
        GameRegistry.addSmelting(IafItemRegistry.copper_chestplate, new ItemStack(IafItemRegistry.copperIngot, 3), 1);
        GameRegistry.addSmelting(IafItemRegistry.copper_leggings, new ItemStack(IafItemRegistry.copperIngot, 3), 1);
        GameRegistry.addSmelting(IafItemRegistry.copper_boots, new ItemStack(IafItemRegistry.copperIngot, 1), 1);

        GameRegistry.addSmelting(IafItemRegistry.copper_pickaxe, new ItemStack(IafItemRegistry.copperIngot), 1);
        GameRegistry.addSmelting(IafItemRegistry.copper_axe, new ItemStack(IafItemRegistry.copperIngot), 1);
        GameRegistry.addSmelting(IafItemRegistry.copper_sword, new ItemStack(IafItemRegistry.copperNugget, 4), 1);
        GameRegistry.addSmelting(IafItemRegistry.copper_hoe, new ItemStack(IafItemRegistry.copperNugget, 4), 1);
        GameRegistry.addSmelting(IafItemRegistry.copper_shovel, new ItemStack(IafItemRegistry.copperNugget, 1), 1);

        IafItemRegistry.blindfoldArmor.setRepairItem(new ItemStack(Items.STRING));
        IafItemRegistry.copperMetal.setRepairItem(new ItemStack(IafItemRegistry.copperIngot));
        IafItemRegistry.copperTools.setRepairItem(new ItemStack(IafItemRegistry.copperIngot));
        IafItemRegistry.silverMetal.setRepairItem(new ItemStack(IafItemRegistry.silverIngot));
        IafItemRegistry.silverTools.setRepairItem(new ItemStack(IafItemRegistry.silverIngot));
        IafItemRegistry.boneTools.setRepairItem(new ItemStack(IafItemRegistry.dragonbone));
        IafItemRegistry.fireBoneTools.setRepairItem(new ItemStack(IafItemRegistry.dragonbone));
        IafItemRegistry.iceBoneTools.setRepairItem(new ItemStack(IafItemRegistry.dragonbone));
        IafItemRegistry.lightningBoneTools.setRepairItem(new ItemStack(IafItemRegistry.dragonbone));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairItem(new ItemStack(EnumDragonArmor.getScaleItem(armor)));
        }
        for (EnumBloodedDragonArmor armor : EnumBloodedDragonArmor.values()) {
            armor.armorMaterial.setRepairItem(new ItemStack(EnumBloodedDragonArmor.getScaleItem(armor)));
        }
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairItem(new ItemStack(serpent.scale));
        }
        IafItemRegistry.sheep.setRepairItem(new ItemStack(Blocks.WOOL));
        IafItemRegistry.earplugsArmor.setRepairItem(new ItemStack(Blocks.WOODEN_BUTTON));
        IafItemRegistry.yellow_deathworm.setRepairItem(new ItemStack(IafItemRegistry.deathworm_chitin, 1, 0));
        IafItemRegistry.white_deathworm.setRepairItem(new ItemStack(IafItemRegistry.deathworm_chitin, 1, 1));
        IafItemRegistry.red_deathworm.setRepairItem(new ItemStack(IafItemRegistry.deathworm_chitin, 1, 2));
        IafItemRegistry.trollWeapon.setRepairItem(new ItemStack(Blocks.STONE));
        IafItemRegistry.troll_mountain.setRepairItem(new ItemStack(IafItemRegistry.troll_leather_mountain));
        IafItemRegistry.troll_forest.setRepairItem(new ItemStack(IafItemRegistry.troll_leather_forest));
        IafItemRegistry.troll_frost.setRepairItem(new ItemStack(IafItemRegistry.troll_leather_frost));
        IafItemRegistry.dread_sword_tools.setRepairItem(new ItemStack(IafItemRegistry.dread_shard));
        IafItemRegistry.hippocampus_sword_tools.setRepairItem(new ItemStack(IafItemRegistry.hippocampus_fin));
        ItemStack waterBreathingPotion = new ItemStack(Items.POTIONITEM, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Potion", "water_breathing");
        waterBreathingPotion.setTagCompound(tag);
        BrewingRecipeRegistry.addRecipe(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(IafItemRegistry.shiny_scales), waterBreathingPotion);
    }

    public static BannerPattern addBanner(String name, ItemStack craftingStack) {
        Class<?>[] classes = {String.class, String.class, ItemStack.class};
        Object[] names = {name, "iceandfire." + name, craftingStack};
        BANNER_ITEMS.add(craftingStack);
        return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), classes, names);
    }

    public static void handleOreRegistration(String name, ItemStack stack) {
        if ("ingotBronze".equals(name)) {
            GameRegistry.addSmelting(IafItemRegistry.bronzeAlloy, stack, 1);
        } else if ("nuggetBronze".equals(name)) {
            GameRegistry.addSmelting(IafItemRegistry.stymphalian_bird_feather, stack, 1);
        }
    }
}
