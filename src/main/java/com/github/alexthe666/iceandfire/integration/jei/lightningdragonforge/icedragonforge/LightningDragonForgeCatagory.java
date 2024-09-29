package com.github.alexthe666.iceandfire.integration.jei.lightningdragonforge.icedragonforge;

import com.github.alexthe666.iceandfire.integration.jei.IceAndFireJEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.text.translation.I18n;

public class LightningDragonForgeCatagory implements IRecipeCategory<LightningDragonForgeRecipeWrapper> {

    public LightningDragonForgeDrawable drawable;

    public LightningDragonForgeCatagory() {
        drawable = new LightningDragonForgeDrawable();
    }

    @Override
    public String getUid() {
        return IceAndFireJEIPlugin.LIGHTNING_DRAGON_FORGE_ID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getTitle() {
        return I18n.translateToLocal("iceandfire.lightning_dragon_forge");
    }

    @Override
    public String getModName() {
        return "iceandfire";
    }

    @Override
    public IDrawable getBackground() {
        return drawable;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, LightningDragonForgeRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 64, 29);
        guiItemStacks.init(1, true, 82, 29);
        guiItemStacks.init(2, false, 144, 30);
        guiItemStacks.set(ingredients);
    }
}
