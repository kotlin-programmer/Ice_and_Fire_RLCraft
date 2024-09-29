package com.github.alexthe666.iceandfire.integration.jei;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.integration.jei.firedragonforge.FireDragonForgeCatagory;
import com.github.alexthe666.iceandfire.integration.jei.firedragonforge.FireDragonForgeRecipeHandler;
import com.github.alexthe666.iceandfire.integration.jei.firedragonforge.FireDragonForgeRecipeWrapper;
import com.github.alexthe666.iceandfire.integration.jei.icedragonforge.IceDragonForgeCatagory;
import com.github.alexthe666.iceandfire.integration.jei.icedragonforge.IceDragonForgeRecipeHandler;
import com.github.alexthe666.iceandfire.integration.jei.icedragonforge.IceDragonForgeRecipeWrapper;
import com.github.alexthe666.iceandfire.integration.jei.lightningdragonforge.icedragonforge.LightningDragonForgeCatagory;
import com.github.alexthe666.iceandfire.integration.jei.lightningdragonforge.icedragonforge.LightningDragonForgeRecipeHandler;
import com.github.alexthe666.iceandfire.integration.jei.lightningdragonforge.icedragonforge.LightningDragonForgeRecipeWrapper;
import com.github.alexthe666.iceandfire.item.IafDragonForgeRecipeRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.core.ModRecipes;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class IceAndFireJEIPlugin implements IModPlugin {

    public static final String FIRE_DRAGON_FORGE_ID = "iceandfire.fire_dragon_forge";
    public static final String ICE_DRAGON_FORGE_ID = "iceandfire.ice_dragon_forge";
    public static final String LIGHTNING_DRAGON_FORGE_ID = "iceandfire.lightning_dragon_forge";

    private static void addDescription(IModRegistry registry, ItemStack stack) {
        registry.addIngredientInfo(stack, ItemStack.class, stack.getTranslationKey() + ".jei_desc");
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(IafDragonForgeRecipeRegistry.FIRE_FORGE_RECIPES, FIRE_DRAGON_FORGE_ID);
        registry.addRecipeHandlers(new FireDragonForgeRecipeHandler());
        registry.handleRecipes(DragonForgeRecipe.class, new FireDragonForgeFactory(), FIRE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.dragonforge_core_fire), FIRE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.dragonforge_core), FIRE_DRAGON_FORGE_ID);

        registry.addRecipes(IafDragonForgeRecipeRegistry.ICE_FORGE_RECIPES, ICE_DRAGON_FORGE_ID);
        registry.addRecipeHandlers(new IceDragonForgeRecipeHandler());
        registry.handleRecipes(DragonForgeRecipe.class, new IceDragonForgeFactory(), ICE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.dragonforge_core_ice), ICE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.dragonforge_core), ICE_DRAGON_FORGE_ID);

        registry.addRecipes(IafDragonForgeRecipeRegistry.LIGHTNING_FORGE_RECIPES, LIGHTNING_DRAGON_FORGE_ID);
        registry.addRecipeHandlers(new LightningDragonForgeRecipeHandler());
        registry.handleRecipes(DragonForgeRecipe.class, new LightningDragonForgeFactory(), LIGHTNING_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.dragonforge_core_lightning), LIGHTNING_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.dragonforge_core), LIGHTNING_DRAGON_FORGE_ID);

        addDescription(registry, new ItemStack(IafItemRegistry.fire_dragon_blood));
        addDescription(registry, new ItemStack(IafItemRegistry.ice_dragon_blood));
        addDescription(registry, new ItemStack(IafItemRegistry.lightning_dragon_blood));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_red));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_bronze));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_gray));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_green));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_blue));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_white));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_sapphire));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_silver));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_amethyst));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_copper));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_electric));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_black));
        addDescription(registry, new ItemStack(IafItemRegistry.dragon_skull));
        addDescription(registry, new ItemStack(IafItemRegistry.dragon_skull, 1, 1));
        addDescription(registry, new ItemStack(IafItemRegistry.dragon_skull, 1, 2));
        addDescription(registry, new ItemStack(IafItemRegistry.fire_stew));
        addDescription(registry, new ItemStack(IafItemRegistry.frost_stew));
        addDescription(registry, new ItemStack(IafItemRegistry.lightning_stew));

        for (EnumSkullType skull : EnumSkullType.values()) {
            addDescription(registry, new ItemStack(skull.skull_item));
        }
        for (ItemStack stack : ModRecipes.BANNER_ITEMS) {
            registry.addIngredientInfo(stack, ItemStack.class, "item.iceandfire.custom_banner.jei_desc");
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new FireDragonForgeCatagory());
        registry.addRecipeCategories(new IceDragonForgeCatagory());
        registry.addRecipeCategories(new LightningDragonForgeCatagory());
    }

    public static class FireDragonForgeFactory implements IRecipeWrapperFactory<DragonForgeRecipe> {
        @Override
        public IRecipeWrapper getRecipeWrapper(DragonForgeRecipe recipe) {
            return new FireDragonForgeRecipeWrapper(recipe);
        }
    }

    public static class IceDragonForgeFactory implements IRecipeWrapperFactory<DragonForgeRecipe> {
        @Override
        public IRecipeWrapper getRecipeWrapper(DragonForgeRecipe recipe) {
            return new IceDragonForgeRecipeWrapper(recipe);
        }
    }

    public static class LightningDragonForgeFactory implements IRecipeWrapperFactory<DragonForgeRecipe> {
        @Override
        public IRecipeWrapper getRecipeWrapper(DragonForgeRecipe recipe) {
            return new LightningDragonForgeRecipeWrapper(recipe);
        }
    }
}
