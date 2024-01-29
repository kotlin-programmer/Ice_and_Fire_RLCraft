package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.entity.projectile.EntityAmphithereArrow;
import com.github.alexthe666.iceandfire.entity.projectile.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
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

public class ModRecipes {

    public static void preInit() {

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.stymphalian_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.amphithere_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.sea_serpent_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.dragonbone_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.hydra_arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHydraArrow entityarrow = new EntityHydraArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.hippogryph_egg, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntityHippogryphEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.rotten_egg, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntityCockatriceEgg(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.deathworm_egg, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntityDeathWormEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn.getMetadata() == 1);
            }
        });

        OreDictionary.registerOre("ingotCopper", ModItems.copperIngot);
        OreDictionary.registerOre("nuggetCopper", ModItems.copperNugget);
        OreDictionary.registerOre("oreCopper", ModBlocks.copperOre);
        OreDictionary.registerOre("blockCopper", ModBlocks.copperBlock);
        OreDictionary.registerOre("ingotSilver", ModItems.silverIngot);
        OreDictionary.registerOre("nuggetSilver", ModItems.silverNugget);
        OreDictionary.registerOre("oreSilver", ModBlocks.silverOre);
        OreDictionary.registerOre("blockSilver", ModBlocks.silverBlock);
        OreDictionary.registerOre("gemAmethyst", ModItems.amethystGem);
        OreDictionary.registerOre("oreAmethyst", ModBlocks.amethystOre);
        OreDictionary.registerOre("blockAmethyst", ModBlocks.amethystBlock);
        OreDictionary.registerOre("gemSapphire", ModItems.sapphireGem);
        OreDictionary.registerOre("oreSapphire", ModBlocks.sapphireOre);
        OreDictionary.registerOre("blockSapphire", ModBlocks.sapphireBlock);
        OreDictionary.registerOre("boneWither", ModItems.witherbone);
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
        OreDictionary.registerOre("boneWithered", ModItems.witherbone);
        OreDictionary.registerOre("boneDragon", ModItems.dragonbone);
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            OreDictionary.registerOre("seaSerpentScales", serpent.scale);
        }
        OreDictionary.registerOre("listAllEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));

        addBanner("firedragon", new ItemStack(ModItems.dragon_skull, 1, 0));
        addBanner("icedragon", new ItemStack(ModItems.dragon_skull, 1, 1));
        addBanner("lightningdragon", new ItemStack(ModItems.dragon_skull, 1, 2));
        addBanner("dread", new ItemStack(ModItems.dread_shard));
        GameRegistry.addSmelting(ModBlocks.copperOre, new ItemStack(ModItems.copperIngot), 1);
        GameRegistry.addSmelting(ModBlocks.silverOre, new ItemStack(ModItems.silverIngot), 1);
        GameRegistry.addSmelting(ModBlocks.amethystOre, new ItemStack(ModItems.amethystGem), 1);
        GameRegistry.addSmelting(ModBlocks.sapphireOre, new ItemStack(ModItems.sapphireGem), 1);
        GameRegistry.addSmelting(ModBlocks.myrmex_desert_resin_block, new ItemStack(ModBlocks.myrmex_desert_resin_glass), 1);
        GameRegistry.addSmelting(ModBlocks.myrmex_jungle_resin_block, new ItemStack(ModBlocks.myrmex_jungle_resin_glass), 1);
        GameRegistry.addSmelting(ModItems.stymphalian_bird_feather, new ItemStack(ModItems.copperNugget), 1);

        ModItems.blindfoldArmor.setRepairItem(new ItemStack(Items.STRING));
        ModItems.copperMetal.setRepairItem(new ItemStack(ModItems.copperIngot));
        ModItems.copperTools.setRepairItem(new ItemStack(ModItems.copperIngot));
        ModItems.silverMetal.setRepairItem(new ItemStack(ModItems.silverIngot));
        ModItems.silverTools.setRepairItem(new ItemStack(ModItems.silverIngot));
        ModItems.boneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        ModItems.fireBoneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        ModItems.iceBoneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        ModItems.lightningBoneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairItem(new ItemStack(EnumDragonArmor.getScaleItem(armor)));
        }
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairItem(new ItemStack(serpent.scale));
        }
        ModItems.sheep.setRepairItem(new ItemStack(Blocks.WOOL));
        ModItems.earplugsArmor.setRepairItem(new ItemStack(Blocks.WOODEN_BUTTON));
        ModItems.yellow_deathworm.setRepairItem(new ItemStack(ModItems.deathworm_chitin, 1, 0));
        ModItems.white_deathworm.setRepairItem(new ItemStack(ModItems.deathworm_chitin, 1, 1));
        ModItems.red_deathworm.setRepairItem(new ItemStack(ModItems.deathworm_chitin, 1, 2));
        ModItems.trollWeapon.setRepairItem(new ItemStack(Blocks.STONE));
        ModItems.troll_mountain.setRepairItem(new ItemStack(ModItems.troll_leather_mountain));
        ModItems.troll_forest.setRepairItem(new ItemStack(ModItems.troll_leather_forest));
        ModItems.troll_frost.setRepairItem(new ItemStack(ModItems.troll_leather_frost));
        ModItems.dread_sword_tools.setRepairItem(new ItemStack(ModItems.dread_shard));
        ItemStack waterBreathingPotion = new ItemStack(Items.POTIONITEM, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Potion", "water_breathing");
        waterBreathingPotion.setTagCompound(tag);
        BrewingRecipeRegistry.addRecipe(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(ModItems.shiny_scales), waterBreathingPotion);
    }

    public static BannerPattern addBanner(String name, ItemStack craftingStack) {
        Class<?>[] classes = {String.class, String.class, ItemStack.class};
        Object[] names = {name, "iceandfire." + name, craftingStack};
        return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), classes, names);
    }

    public static void handleOreRegistration(String name, ItemStack stack) {
        if ("ingotBronze".equals(name)) {
            GameRegistry.addSmelting(ModItems.bronzeAlloy, stack, 1);
        } else if ("nuggetBronze".equals(name)) {
            GameRegistry.addSmelting(ModItems.stymphalian_bird_feather, stack, 1);
        }
    }
}
