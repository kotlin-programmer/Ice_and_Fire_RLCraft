package com.github.alexthe666.iceandfire.integration.jei;

import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.text.translation.I18n;

public class DragonForgeCategory implements IRecipeCategory<DragonForgeRecipeWrapper> {

    public DragonForgeDrawable drawable;
    public EnumDragonType type;

    public DragonForgeCategory(EnumDragonType type) {
        this.type = type;
        drawable = new DragonForgeDrawable(type);
    }

    @Override
    public String getUid() {
        switch (this.type){
            case FIRE: return IceAndFireJEIPlugin.FIRE_DRAGON_FORGE_ID;
            case ICE: return IceAndFireJEIPlugin.ICE_DRAGON_FORGE_ID;
            case LIGHTNING: default: return IceAndFireJEIPlugin.LIGHTNING_DRAGON_FORGE_ID;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getTitle() {
        switch (this.type){
            case FIRE: return I18n.translateToLocal("iceandfire.fire_dragon_forge");
            case ICE: return I18n.translateToLocal("iceandfire.ice_dragon_forge");
            case LIGHTNING: default: return I18n.translateToLocal("iceandfire.lightning_dragon_forge");
        }
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
    public void setRecipe(IRecipeLayout recipeLayout, DragonForgeRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 64, 29);
        guiItemStacks.init(1, true, 82, 29);
        guiItemStacks.init(2, false, 144, 30);
        guiItemStacks.set(ingredients);
    }
}
