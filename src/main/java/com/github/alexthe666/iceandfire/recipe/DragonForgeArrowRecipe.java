package com.github.alexthe666.iceandfire.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class DragonForgeArrowRecipe extends DragonForgeRecipe {

    public DragonForgeArrowRecipe(ItemStack input, ItemStack blood, ItemStack output) {
        super(input, blood, output);
    }

    @Override
    public boolean canSmelt(ItemStack input, ItemStack blood, ItemStack output) {
        if (input.isEmpty() || !input.isItemEqualIgnoreDurability(getInput())) {
            return false;
        }
        if (blood.isEmpty() || !blood.isItemEqual(getBlood())) {
            return false;
        }
        if (blood.getCount() < getBlood().getCount()) {
            return false;
        }
        if (getOutput().isEmpty()) {
            return false;
        }
        if (!output.isEmpty() && !output.isItemEqual(getOutput())) {
            return false;
        }
        int calculatedOutputCount = output.getCount() + Math.min(input.getCount(), getOutput().getCount());
        return calculatedOutputCount <= 64 && calculatedOutputCount <= output.getMaxStackSize();
    }

    public void smelt(NonNullList<ItemStack> forge) {
        ItemStack input = forge.get(0);
        ItemStack blood = forge.get(1);
        ItemStack output = forge.get(2);
        smelt(forge, input, blood, output);
    }

    public void smelt(NonNullList<ItemStack> forge, ItemStack input, ItemStack blood, ItemStack output) {
        int count = Math.min(input.getCount(), getOutput().getCount());
        if (output.isEmpty()) {
            ItemStack stack = getOutput().copy();
            stack.setCount(count);
            forge.set(2, stack);
        } else {
            output.grow(count);
        }
        input.shrink(count);
        blood.shrink(getBlood().getCount());
    }
}
