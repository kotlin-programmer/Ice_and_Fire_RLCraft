package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.block.BlockDragonforgeBricks;
import com.github.alexthe666.iceandfire.block.BlockDragonforgeCore;
import com.github.alexthe666.iceandfire.block.BlockDragonforgeInput;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.core.ModRecipes;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import com.github.alexthe666.iceandfire.item.IafDragonForgeRecipeRegistry;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TileEntityDragonforge extends TileEntity implements ITickable, ISidedInventory {
    private static final int[] SLOTS_TOP = new int[]{0, 1};
    private static final int[] SLOTS_BOTTOM = new int[]{2};
    private static final int[] SLOTS_SIDES = new int[]{0, 1};
    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);
    private NonNullList<ItemStack> forgeItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
    public int cookTime = 0;
    public int lastFlameTimer = 0;

    public TileEntityDragonforge() {
    }

    @Override
    public int getSizeInventory() {
        return this.forgeItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.forgeItemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private void updateBlocks(boolean grill) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos blockPos = this.getPos().offset(facing);
            Block block = world.getBlockState(blockPos).getBlock();
            if (block instanceof BlockDragonforgeBricks) {
                IBlockState grillState = IafBlockRegistry.dragonforge_bricks.getDefaultState().withProperty(BlockDragonforgeBricks.GRILL, grill ? BlockDragonforgeBricks.getMetaFromType(getType()) : 0);
                if (world.getBlockState(blockPos) != grillState) {
                    world.setBlockState(blockPos, grillState);
                }
            } else if (block instanceof BlockDragonforgeInput) {
                IBlockState inputState = IafBlockRegistry.dragonforge_input.getDefaultState().withProperty(BlockDragonforgeInput.ACTIVE, BlockDragonforgeInput.getMetaFromType(getType()));
                if (world.getBlockState(blockPos) != inputState) {
                    world.setBlockState(blockPos, inputState);
                }
            }
        }
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.forgeItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.forgeItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.forgeItemStacks, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        this.forgeItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.forgeItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.forgeItemStacks);
        this.cookTime = compound.getShort("CookTime");
        this.lastFlameTimer = compound.getShort("LastFlameTimer");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("CookTime", (short) this.cookTime);
        compound.setShort("LastFlameTimer", (short) this.lastFlameTimer);
        ItemStackHelper.saveAllItems(compound, this.forgeItemStacks);
        return compound;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isBurning() {
        return this.cookTime > 0;
    }

    @Override
    public void update() {
        if (!this.world.isRemote) {
            boolean flag = this.isBurning();
            boolean flag1 = false;

            updateBlocks(assembled());

            if (this.lastFlameTimer > 0) {
                this.lastFlameTimer--;
            }

            if (this.isBurning()) {
                if (!this.canSmelt() || this.lastFlameTimer == 0) {
                    this.cookTime = Math.max(this.cookTime - 1, 0);
                }

                if (this.canSmelt()) {
                    if (this.cookTime >= getMaxCookTime()) {
                        this.smeltItem();
                        this.cookTime = 0;
                        flag1 = true;
                    }
                }
            } else {
                if (this.getType() != null) {
                    this.updateBlockState(null);
                }
            }

            if (flag != this.isBurning()) {
                flag1 = true;
            }

            if (flag1) {
                this.markDirty();
            }
        }
    }

    public void updateBlockState(EnumDragonType type) {
        this.blockType = BlockDragonforgeCore.setState(type, world, pos, this);
    }

    public EnumDragonType getType() {
        Block block = this.world.getBlockState(this.pos).getBlock();
        if (block == IafBlockRegistry.dragonforge_core_fire) {
            return EnumDragonType.FIRE;
        } else if (block == IafBlockRegistry.dragonforge_core_ice) {
            return EnumDragonType.ICE;
        } else if (block == IafBlockRegistry.dragonforge_core_lightning) {
            return EnumDragonType.LIGHTNING;
        }
        return null;
    }

    public int getMaxCookTime() {
        return 1000;
    }

    private DragonForgeRecipe getRecipe(EnumDragonType type) {
        ItemStack inputItemStack = this.forgeItemStacks.get(0);
        DragonForgeRecipe recipe = IafDragonForgeRecipeRegistry.getForgeRecipe(type, inputItemStack);
        if (recipe != null && recipe.canSmelt(this.forgeItemStacks)) {
            return recipe;
        }
        return null;
    }

    public boolean canSmelt() {
        return canSmelt(getType());
    }

    public boolean canSmelt(EnumDragonType type) {
        DragonForgeRecipe recipe = getRecipe(type);
        if (recipe == null) {
            return false;
        }
        return recipe.canSmelt(this.forgeItemStacks);
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void smeltItem() {
        if (!this.canSmelt()) {
            return;
        }
        DragonForgeRecipe recipe = getRecipe(getType());
        if (recipe == null) {
            return;
        }
        recipe.smelt(this.forgeItemStacks);
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 2) {
            return false;
        } else if (index == 1) {
            DragonForgeRecipe forgeRecipe = IafDragonForgeRecipeRegistry.getForgeRecipeForBlood(getType(), stack);
            if (forgeRecipe != null) {
                return true;
            }
        }
        return index == 0;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            Item item = stack.getItem();

            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }

        return true;
    }

    @Override
    public int getField(int id) {
        return cookTime;
    }

    @Override
    public void setField(int id, int value) {
        cookTime = value;
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public void clear() {
        this.forgeItemStacks.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;
        return super.getCapability(capability, facing);
    }

    @Override
    public String getName() {
        return "container.dragonforge";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    public void transferPower(EnumDragonType type) {
        if (this.canSmelt(type)) {
            EnumDragonType currentType = getType();
            if (currentType == null) {
                this.updateBlockState(type);
            }

            if (this.lastFlameTimer != 40) {
                this.cookTime = Math.min(this.cookTime + 1, getMaxCookTime());
            }

            this.lastFlameTimer = 40;
        }
    }

    private boolean checkBoneCorners(BlockPos pos) {
        return doesBlockEqual(pos.north().east(), IafBlockRegistry.dragon_bone_block) &&
                doesBlockEqual(pos.north().west(), IafBlockRegistry.dragon_bone_block) &&
                doesBlockEqual(pos.south().east(), IafBlockRegistry.dragon_bone_block) &&
                doesBlockEqual(pos.south().west(), IafBlockRegistry.dragon_bone_block);
    }

    private boolean checkBrickCorners(BlockPos pos) {
        return doesBlockEqual(pos.north().east(), IafBlockRegistry.dragonforge_bricks) &&
                doesBlockEqual(pos.north().west(), IafBlockRegistry.dragonforge_bricks) &&
                doesBlockEqual(pos.south().east(), IafBlockRegistry.dragonforge_bricks) &&
                doesBlockEqual(pos.south().west(), IafBlockRegistry.dragonforge_bricks);
    }

    private boolean checkBrickSlots(BlockPos pos) {
        return doesBlockEqual(pos.north(), IafBlockRegistry.dragonforge_bricks) &&
                doesBlockEqual(pos.east(), IafBlockRegistry.dragonforge_bricks) &&
                doesBlockEqual(pos.west(), IafBlockRegistry.dragonforge_bricks) &&
                doesBlockEqual(pos.south(), IafBlockRegistry.dragonforge_bricks);
    }

    public boolean assembled() {
        return checkBoneCorners(pos.down()) && checkBrickSlots(pos.down()) &&
                checkBrickCorners(pos) && atleastThreeAreBricks(pos) &&
                checkBoneCorners(pos.up()) && checkBrickSlots(pos.up());
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing) {
        return getCapability(capability, facing) != null;
    }

    private boolean doesBlockEqual(BlockPos pos, Block block) {
        return world.getBlockState(pos).getBlock() == block;
    }

    private boolean atleastThreeAreBricks(BlockPos pos) {
        int count = 0;
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (world.getBlockState(pos.offset(facing)).getBlock() == IafBlockRegistry.dragonforge_bricks) {
                count++;
            }
        }
        return count > 2;
    }
}
