package com.github.alexthe666.iceandfire.integration.crafttweaker;

import com.github.alexthe666.iceandfire.item.IafDragonForgeRecipeRegistry;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.iceandfire.recipes")
public class CraftTweakerCompat {

    public static void preInit() {
        CraftTweakerAPI.registerClass(CraftTweakerCompat.class);
    }

    @ZenMethod
    public static void addFireDragonForgeRecipe(IItemStack input, IItemStack bloodInput, IItemStack output, boolean persistMetadata) {
        IafDragonForgeRecipeRegistry.FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(bloodInput), CraftTweakerMC.getItemStack(output), persistMetadata));
    }

    @ZenMethod
    public static void addIceDragonForgeRecipe(IItemStack input, IItemStack bloodInput, IItemStack output, boolean persistMetadata) {
        IafDragonForgeRecipeRegistry.ICE_FORGE_RECIPES.add(new DragonForgeRecipe(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(bloodInput), CraftTweakerMC.getItemStack(output), persistMetadata));
    }

    @ZenMethod
    public static void addLightningDragonForgeRecipe(IItemStack input, IItemStack bloodInput, IItemStack output, boolean persistMetadata) {
        IafDragonForgeRecipeRegistry.LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(bloodInput), CraftTweakerMC.getItemStack(output), persistMetadata));
    }

    @ZenMethod
    public static void removeFireDragonForgeRecipe(IItemStack output) {
        ItemStack stack = CraftTweakerMC.getItemStack(output).copy();
        stack.setCount(1);
        IafDragonForgeRecipeRegistry.FIRE_FORGE_RECIPES.removeIf(recipe ->
                recipe.getOutput().copy().isItemEqualIgnoreDurability(stack)
        );
    }

    @ZenMethod
    public static void removeIceDragonForgeRecipe(IItemStack output) {
        ItemStack stack = CraftTweakerMC.getItemStack(output).copy();
        stack.setCount(1);
        IafDragonForgeRecipeRegistry.ICE_FORGE_RECIPES.removeIf(recipe ->
                recipe.getOutput().copy().isItemEqualIgnoreDurability(stack)
        );
    }

    @ZenMethod
    public static void removeLightningDragonForgeRecipe(IItemStack output) {
        ItemStack stack = CraftTweakerMC.getItemStack(output).copy();
        stack.setCount(1);
        IafDragonForgeRecipeRegistry.LIGHTNING_FORGE_RECIPES.removeIf(recipe ->
                recipe.getOutput().copy().isItemEqualIgnoreDurability(stack)
        );
    }
}
