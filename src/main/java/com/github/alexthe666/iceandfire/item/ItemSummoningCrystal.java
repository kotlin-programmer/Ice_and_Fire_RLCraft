package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.world.DragonPosWorldData;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemSummoningCrystal extends Item {

    private ForgeChunkManager.Ticket lastChunkTicket = null;

    public ItemSummoningCrystal(String variant) {
        this.setTranslationKey("iceandfire.summoning_crystal_" + variant);
        this.setRegistryName(IceAndFire.MODID, "summoning_crystal_" + variant);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.addPropertyOverride(new ResourceLocation("has_dragon"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F;
            }
        });
        this.setMaxStackSize(1);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        boolean flag = false;
        String desc = "entity.firedragon.name";
        if (stack.getItem() == IafItemRegistry.summoning_crystal_ice) {
            desc = "entity.icedragon.name";
        } else if (stack.getItem() == IafItemRegistry.summoning_crystal_lightning) {
            desc = "entity.lightningdragon.name";
        }
        if (stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Dragon")) {
                    NBTTagCompound dragonTag = stack.getTagCompound().getCompoundTag(tagInfo);
                    String dragonName = I18n.format(desc);
                    if (!dragonTag.getString("CustomName").isEmpty()) {
                        dragonName = dragonTag.getString("CustomName");
                    }
                    tooltip.add(TextFormatting.GRAY + I18n.format("item.iceandfire.summoning_crystal.bound", dragonName));
                    flag = true;
                }
            }
        }
        if (!flag) {
            tooltip.add(TextFormatting.GRAY + I18n.format("item.iceandfire.summoning_crystal.desc_0"));
            tooltip.add(TextFormatting.GRAY + I18n.format("item.iceandfire.summoning_crystal.desc_1"));
        }
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            boolean found = false;
            BlockPos offsetPos = pos.offset(facing);
            float yaw = player.rotationYaw;
            boolean tryAgain = false;
            boolean displayError = false;
            if (stack.getItem() == this && hasDragon(stack)) {
                if (stack.getTagCompound() != null) {
                    for (String tagInfo : stack.getTagCompound().getKeySet()) {
                        if (tagInfo.contains("Dragon")) {
                            NBTTagCompound dragonTag = stack.getTagCompound().getCompoundTag(tagInfo);
                            UUID id = dragonTag.getUniqueId("DragonUUID");
                            if (id != null) {
                                found = summonEntity(id, worldIn, offsetPos, yaw);
                                displayError = !found;

                                if (lastChunkTicket != null) {
                                    try {
                                        ForgeChunkManager.releaseTicket(lastChunkTicket);
                                        lastChunkTicket = null;
                                    } catch (Exception e) {
                                        IceAndFire.logger.warn("Failed to release forced chunk load ticket");
                                        e.printStackTrace();
                                    }
                                }

                                if (!found) {
                                    try {
                                        BlockPos dragonChunkPos = DragonPosWorldData.get(worldIn).getDragonPos(id);
                                        if (dragonChunkPos != null && !worldIn.isBlockLoaded(dragonChunkPos)) {
                                            ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestPlayerTicket(IceAndFire.INSTANCE, player.getName(), worldIn, ForgeChunkManager.Type.NORMAL);
                                            if (ticket != null) {
                                                ForgeChunkManager.forceChunk(ticket, new ChunkPos(dragonChunkPos));
                                                lastChunkTicket = ticket;
                                            }
                                            tryAgain = true;
                                        }
                                    } catch (Exception e) {
                                        IceAndFire.logger.warn("Could not load chunk when summoning dragon");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
                if (found) {
                    player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
                    player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1, 1);
                    player.swingArm(hand);
                    player.sendStatusMessage(new TextComponentTranslation("message.iceandfire.dragonTeleport"), true);
                } else if (tryAgain) {
                    player.sendStatusMessage(new TextComponentTranslation("message.iceandfire.tryAgain"), true);
                } else if (displayError) {
                    player.sendStatusMessage(new TextComponentTranslation("message.iceandfire.noDragonTeleport"), true);
                }
            }
        }
        return EnumActionResult.PASS;
    }

    public boolean summonEntity(UUID id, World worldIn, BlockPos offsetPos, float yaw) {
        try {
            Entity entity = worldIn.getMinecraftServer().getEntityFromUuid(id);
            if (entity instanceof EntityLivingBase) {
                IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLivingBase) entity);
                if (capability.isStoned()) {
                    return false;
                }
                entity.setLocationAndAngles(offsetPos.getX() + 0.5D, offsetPos.getY() + 0.5D, offsetPos.getZ() + 0.5D, yaw, 0);
                DragonPosWorldData.get(worldIn).removeDragon(entity.getUniqueID());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasDragon(ItemStack stack) {
        if (stack.getItem() instanceof ItemSummoningCrystal && stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Dragon")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBoundTo(ItemStack stack, EntityDragonBase dragon) {
        if (stack.getItem() instanceof ItemSummoningCrystal && stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Dragon")) {
                    NBTTagCompound dragonTag = stack.getTagCompound().getCompoundTag(tagInfo);
                    UUID id = dragonTag.getUniqueId("DragonUUID");
                    return dragon.getUniqueID().equals(id);
                }
            }
        }
        return false;
    }
}
