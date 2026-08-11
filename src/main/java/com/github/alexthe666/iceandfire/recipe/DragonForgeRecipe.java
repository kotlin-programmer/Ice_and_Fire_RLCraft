package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.mixin.vanilla.IItemStackAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;

public class DragonForgeRecipe {

    protected ItemStack input;
    protected ItemStack blood;
    protected ItemStack output;

    protected boolean persistMetadata;
    protected boolean isProjectile;

    public DragonForgeRecipe(ItemStack input, ItemStack blood, ItemStack output) {
        this(input, blood, output, false);
    }

    public DragonForgeRecipe(ItemStack input, ItemStack blood, ItemStack output, boolean persistMetadata) {
        this(input, blood, output, persistMetadata, false);
    }

    public DragonForgeRecipe(ItemStack input, ItemStack blood, ItemStack output, boolean persistMetadata, boolean isProjectile) {
        this.input = input;
        this.blood = blood;
        this.output = output;
        this.persistMetadata = persistMetadata;
        this.isProjectile = isProjectile;
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

    public boolean shouldPersistMetadata() {
        return persistMetadata;
    }

    public boolean isProjectile() {
        return isProjectile;
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
        if (!isProjectile() && input.getCount() < getInput().getCount()) {
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
        if (output.isEmpty()) {
            return true;
        }
        int quantityAvailable = output.getMaxStackSize() - output.getCount();
        int quantityCreated = isProjectile() ? Math.min(input.getCount(), getInput().getCount()) : getOutput().getCount();
        return quantityAvailable - quantityCreated >= 0;
    }

    public void smelt(NonNullList<ItemStack> forge) {
        ItemStack input = forge.get(0);
        ItemStack blood = forge.get(1);
        ItemStack output = forge.get(2);
        smelt(forge, input, blood, output);
    }

    public void smelt(NonNullList<ItemStack> forge, ItemStack input, ItemStack blood, ItemStack output) {
        int quantityCreated = isProjectile() ? Math.min(input.getCount(), getInput().getCount())
                : getOutput().getCount();
        int quantityConsumed = isProjectile() ? quantityCreated
                : getInput().getCount();
        if (output.isEmpty()) {
            ItemStack stack;
            if (this.shouldPersistMetadata()) {
                CapabilityDispatcher capabilities = ((IItemStackAccessor) (Object) input).iceAndFire$getCapabilities();
                stack = new ItemStack(
                        getOutput().getItem(),
                        quantityCreated,
                        input.getItemDamage(),
                        capabilities != null ? capabilities.serializeNBT() : null
                );
                stack.setStackDisplayName(input.getDisplayName());
                stack.setRepairCost(input.getRepairCost());
                stack.setTagCompound(input.getTagCompound());
            } else {
                stack = getOutput().copy();
                stack.setCount(quantityCreated);
            }
            forge.set(2, stack);
        } else {
            output.grow(quantityCreated);
        }
        input.shrink(quantityConsumed);
        blood.shrink(getBlood().getCount());
    }
}
