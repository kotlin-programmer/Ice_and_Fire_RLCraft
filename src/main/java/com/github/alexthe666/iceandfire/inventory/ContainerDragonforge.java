package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafDragonForgeRecipeRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerDragonforge extends SyncedFieldContainer {

    private final IInventory tileFurnace;

    public ContainerDragonforge(InventoryPlayer playerInventory, IInventory furnaceInventory) {
        super(furnaceInventory);
        this.tileFurnace = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 68, 34));
        this.addSlotToContainer(new Slot(furnaceInventory, 1, 86, 34) {
            public boolean isItemValid(ItemStack stack) {
                return isDragonBlood(stack.getItem());
            }
        });

        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 148, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileFurnace.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            //Output to Inventory
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                    return ItemStack.EMPTY;
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index > 2) {
                //If Dragon Forge recipe exists, only allow from inventory to left input slot
                if (IafDragonForgeRecipeRegistry.getForgeRecipe(itemstack1) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                        return ItemStack.EMPTY;
                //If blood in inventory, only allow to right input slot
                } else if (isDragonBlood(itemstack1.getItem())) {
                    if(!this.mergeItemStack(itemstack1, 1, 2, false))
                        return ItemStack.EMPTY;
                //Inventory to hotbar
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                        return ItemStack.EMPTY;
                //Hotbar to inventory
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            //Input slots to inventory
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false))
                return ItemStack.EMPTY;

            if (itemstack1.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    public static boolean isDragonBlood(Item item){
        return item == IafItemRegistry.ice_dragon_blood ||item == IafItemRegistry.fire_dragon_blood || item == IafItemRegistry.lightning_dragon_blood;
    }
}
