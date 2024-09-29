package com.github.alexthe666.iceandfire.integration.jei.lightningdragonforge.icedragonforge;

import com.github.alexthe666.iceandfire.integration.jei.IceAndFireJEIPlugin;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class LightningDragonForgeRecipeHandler implements IRecipeHandler<LightningDragonForgeRecipeWrapper> {

    @Override
    public Class getRecipeClass() {
        return DragonForgeRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(LightningDragonForgeRecipeWrapper recipe) {
        return IceAndFireJEIPlugin.LIGHTNING_DRAGON_FORGE_ID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(LightningDragonForgeRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(LightningDragonForgeRecipeWrapper recipe) {
        return true;
    }
}
