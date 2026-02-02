package com.github.alexthe666.iceandfire.integration.jei;

import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DragonForgeRecipeWrapper implements IRecipeWrapper {

    private final DragonForgeRecipe recipe;

    public DragonForgeRecipeWrapper(DragonForgeRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(recipe.getInput());
        stacks.add(recipe.getBlood());
        ingredients.setInputs(ItemStack.class, stacks);

        ItemStack output = recipe.getOutput();
        if (recipe.isProjectile()) {
            output.setCount(recipe.getInput().getCount());
        }
        ingredients.setOutput(ItemStack.class, output);
    }

    public DragonForgeRecipe getRecipe() {
        return recipe;
    }
}
