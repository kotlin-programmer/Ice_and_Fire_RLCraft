package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.enums.EnumBloodedDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IafDragonForgeRecipeRegistry {

    public static List<DragonForgeRecipe> FIRE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> ICE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> LIGHTNING_FORGE_RECIPES = new ArrayList<>();

    public static void preInit() {
        FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_sword), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_sword_fire), true));
        FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_bow), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_bow_fire), true));
        ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_sword), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_sword_ice), true));
        ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_bow), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_bow_ice), true));
        LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_sword), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_sword_lightning), true));
        LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_bow), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_bow_lightning), true));

        for (EnumDragonArmor input : EnumDragonArmor.values()) {
            List<DragonForgeRecipe> recipes;
            Item blood;
            switch (input.eggType.dragonType) {
                case ICE:
                    recipes = ICE_FORGE_RECIPES;
                    blood = IafItemRegistry.ice_dragon_blood;
                    break;
                case LIGHTNING:
                    recipes = LIGHTNING_FORGE_RECIPES;
                    blood = IafItemRegistry.lightning_dragon_blood;
                    break;
                default:
                    recipes = FIRE_FORGE_RECIPES;
                    blood = IafItemRegistry.fire_dragon_blood;
            }

            EnumBloodedDragonArmor result = EnumBloodedDragonArmor.valueOf(input.name());

            recipes.add(new DragonForgeRecipe(new ItemStack(input.helmet), new ItemStack(blood), new ItemStack(result.helmet), true));
            recipes.add(new DragonForgeRecipe(new ItemStack(input.chestplate), new ItemStack(blood), new ItemStack(result.chestplate), true));
            recipes.add(new DragonForgeRecipe(new ItemStack(input.leggings), new ItemStack(blood), new ItemStack(result.leggings), true));
            recipes.add(new DragonForgeRecipe(new ItemStack(input.boots), new ItemStack(blood), new ItemStack(result.boots), true));
        }
    }

    @Nullable
    public static DragonForgeRecipe getForgeRecipe(EnumDragonType type, ItemStack stack) {
        for (DragonForgeRecipe recipe : getForgeRecipes(type)) {
            if (!stack.isEmpty() && stack.isItemEqualIgnoreDurability(recipe.getInput())) {
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public static DragonForgeRecipe getForgeRecipe(ItemStack stack) {
        for (EnumDragonType type : EnumDragonType.values()) {
            DragonForgeRecipe recipe = getForgeRecipe(type, stack);
            if (recipe != null) {
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public static DragonForgeRecipe getForgeRecipeForBlood(EnumDragonType type, ItemStack stack) {
        for (DragonForgeRecipe recipe : getForgeRecipes(type)) {
            if (stack != null && stack.isItemEqual(recipe.getBlood())) {
                return recipe;
            }
        }
        return null;
    }

    public static List<DragonForgeRecipe> getForgeRecipes(EnumDragonType type) {
        if (type == null) {
            return new ArrayList<>();
        }
        switch (type) {
            case ICE: return ICE_FORGE_RECIPES;
            case LIGHTNING: return LIGHTNING_FORGE_RECIPES;
            default: return FIRE_FORGE_RECIPES;
        }
    }
}
