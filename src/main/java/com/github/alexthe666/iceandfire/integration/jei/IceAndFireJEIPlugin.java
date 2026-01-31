package com.github.alexthe666.iceandfire.integration.jei;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import com.github.alexthe666.iceandfire.item.IafDragonForgeRecipeRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
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
        registry.handleRecipes(DragonForgeRecipe.class, DragonForgeRecipeWrapper::new, FIRE_DRAGON_FORGE_ID);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.dragonforge_core_fire), FIRE_DRAGON_FORGE_ID);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.dragonforge_core), FIRE_DRAGON_FORGE_ID);

        registry.addRecipes(IafDragonForgeRecipeRegistry.ICE_FORGE_RECIPES, ICE_DRAGON_FORGE_ID);
        registry.handleRecipes(DragonForgeRecipe.class, DragonForgeRecipeWrapper::new, ICE_DRAGON_FORGE_ID);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.dragonforge_core_ice), ICE_DRAGON_FORGE_ID);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.dragonforge_core), ICE_DRAGON_FORGE_ID);

        registry.addRecipes(IafDragonForgeRecipeRegistry.LIGHTNING_FORGE_RECIPES, LIGHTNING_DRAGON_FORGE_ID);
        registry.handleRecipes(DragonForgeRecipe.class, DragonForgeRecipeWrapper::new, LIGHTNING_DRAGON_FORGE_ID);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.dragonforge_core_lightning), LIGHTNING_DRAGON_FORGE_ID);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.dragonforge_core), LIGHTNING_DRAGON_FORGE_ID);

        addDescription(registry, new ItemStack(IafItemRegistry.fire_dragon_blood));
        addDescription(registry, new ItemStack(IafItemRegistry.ice_dragon_blood));
        addDescription(registry, new ItemStack(IafItemRegistry.lightning_dragon_blood));
        addDescription(registry, new ItemStack(IafItemRegistry.dragon_skull));
        addDescription(registry, new ItemStack(IafItemRegistry.dragon_skull, 1, 1));
        addDescription(registry, new ItemStack(IafItemRegistry.dragon_skull, 1, 2));
        addDescription(registry, new ItemStack(IafItemRegistry.fire_stew));
        addDescription(registry, new ItemStack(IafItemRegistry.frost_stew));
        addDescription(registry, new ItemStack(IafItemRegistry.lightning_stew));
        for (EnumDragonEgg egg : EnumDragonEgg.values()) {
            addDescription(registry, new ItemStack(egg.egg));
        }
        for (EnumSkullType skull : EnumSkullType.values()) {
            addDescription(registry, new ItemStack(skull.skull_item));
        }
        for (ItemStack stack : IafRecipeRegistry.BANNER_ITEMS) {
            registry.addIngredientInfo(stack, ItemStack.class, "item.iceandfire.custom_banner.jei_desc");
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new DragonForgeCategory(EnumDragonType.FIRE));
        registry.addRecipeCategories(new DragonForgeCategory(EnumDragonType.ICE));
        registry.addRecipeCategories(new DragonForgeCategory(EnumDragonType.LIGHTNING));
    }
}
