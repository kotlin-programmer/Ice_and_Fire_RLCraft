package com.github.alexthe666.iceandfire.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class DragonForgeRecipe {

    protected ItemStack input;
    protected ItemStack blood;
    protected ItemStack output;
    boolean persistMetadata;

    public DragonForgeRecipe(ItemStack input, ItemStack blood, ItemStack output, boolean persistMetadata) {
        this.input = input;
        this.blood = blood;
        this.output = output;
        this.persistMetadata = persistMetadata;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getBlood() {
        return blood;
    }

    public ItemStack getOutput() {
        return output;
    }

    public boolean canSmelt(NonNullList<ItemStack> forge) {
        ItemStack input = forge.get(0);
        ItemStack blood = forge.get(1);
        ItemStack output = forge.get(2);
        return canSmelt(input, blood, output);
    }

    public boolean canSmelt(ItemStack input, ItemStack blood, ItemStack output) {
        if (input.isEmpty() || !input.isItemEqualIgnoreDurability(getInput())) {
            return false;
        }
        if (input.getCount() < getInput().getCount()) {
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
        int calculatedOutputCount = output.getCount() + getOutput().getCount();
        return calculatedOutputCount <= 64 && calculatedOutputCount <= output.getMaxStackSize();
    }

    public void smelt(NonNullList<ItemStack> forge) {
        ItemStack input = forge.get(0);
        ItemStack blood = forge.get(1);
        ItemStack output = forge.get(2);
        smelt(forge, input, blood, output);
    }

    public void smelt(NonNullList<ItemStack> forge, ItemStack input, ItemStack blood, ItemStack output) {
        if (output.isEmpty()) {
            ItemStack stack = getOutput().copy();
            if (this.persistMetadata) {
                stack.setStackDisplayName(input.getDisplayName());
                stack.setItemDamage(input.getItemDamage());
                stack.setRepairCost(input.getRepairCost());
                stack.setTagCompound(input.getTagCompound());
            }
            forge.set(2, stack);
        } else {
            output.grow(getOutput().getCount());
        }
        input.shrink(getInput().getCount());
        blood.shrink(getBlood().getCount());
    }
}
