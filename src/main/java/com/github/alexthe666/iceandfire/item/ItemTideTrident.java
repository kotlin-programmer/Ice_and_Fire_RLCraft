package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.projectile.EntityTideTrident;
import com.github.alexthe666.iceandfire.integration.SpartanWeaponryCompat;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemTideTrident extends Item {

    public ItemTideTrident() {
        super();
        this.setCreativeTab(IceAndFire.TAB);
        this.setTranslationKey("iceandfire.tide_trident");
        this.setRegistryName(IceAndFire.MODID, "tide_trident");
        this.maxStackSize = 1;
        this.setMaxDamage(400);
        this.addPropertyOverride(new ResourceLocation("empty"), (stack, worldIn, entityIn) -> isEmpty(stack) ? 1.0F : 0.0F);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 7D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.9D, 0));
        }
        return multimap;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        initializeNBT(stack, true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            initializeNBT(stack, false);
            items.add(new ItemStack(this));
        }
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        initializeNBT(stack, true);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (!isOriginal(toRepair) || isEmpty(toRepair)) {
            return false;
        }
        if (!repair.isEmpty() && repair.getItem() == ModItems.sea_serpent_fang) {
            return true;
        }
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return isOriginal(stack) && !isEmpty(stack);
    }

    @Override
    public int getItemEnchantability() {
        return 10;
    }

    private static void initializeNBT(ItemStack stack, boolean shouldInitializeUUID) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey("Empty")) {
            tag.setBoolean("Empty", false);
        }
        if (shouldInitializeUUID && !tag.hasUniqueId("UUID")) {
            tag.setUniqueId("UUID", UUID.randomUUID());
            tag.setBoolean("Original", true);
        }
    }

    public static float getArrowVelocity(int i) {
        float f = i / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public static boolean isEmpty(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("Empty");
    }

    public static void setEmpty(ItemStack stack, boolean empty) {
        if (!stack.hasTagCompound()) {
            return;
        }
        stack.getTagCompound().setBoolean("Empty", empty);
    }

    private static UUID getUUID(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasUniqueId("UUID")) {
            return null;
        }
        return tag.getUniqueId("UUID");
    }

    public static boolean hasMatchingUUID(ItemStack t1, ItemStack t2) {
        UUID uuid = getUUID(t1);
        if (uuid == null) {
            return false;
        }
        return uuid.equals(getUUID(t2));
    }

    public static boolean isOriginal(ItemStack stack) {
        return !stack.hasTagCompound() || stack.getTagCompound().getBoolean("Original");
    }

    public static void setOriginal(ItemStack stack, boolean original) {
        if (!stack.hasTagCompound()) {
            return;
        }
        stack.getTagCompound().setBoolean("Original", original);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
        if (isEmpty(stack)) {
            return;
        }
        int i = this.getMaxItemUseDuration(stack) - timeLeft;
        if (i < 0) return;
        float f = getArrowVelocity(i) * 3.0F;
        EntityTideTrident trident = new EntityTideTrident(worldIn, entity);
        trident.setWeapon(stack);
        trident.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, f, 1.0F);
        if (!worldIn.isRemote) {
            SoundEvent soundEvent = SpartanWeaponryCompat.getThrowingWeaponSoundEvent();
            if (soundEvent == null) {
                soundEvent = SoundEvents.ENTITY_EGG_THROW;
            }
            worldIn.playSound(null, entity.posX, entity.posY, entity.posZ, soundEvent, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            worldIn.spawnEntity(trident);
        }
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
            trident.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
        } else {
            if (isOriginal(stack)) {
                setEmpty(stack, true);
            } else {
                stack.shrink(1);
                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer) entity).inventory.deleteStack(stack);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (SpartanWeaponryCompat.RETURN_ENCHANTMENT.equals(enchantment.getRegistryName())) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag flag) {
        boolean isEmpty = isEmpty(stack);
        boolean isOriginal = isOriginal(stack);
        if (isEmpty || !isOriginal) {
            UUID uuid = getUUID(stack);
            if (uuid != null) {
                tooltip.add(TextFormatting.DARK_PURPLE + "UUID: " + Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits()));
            }
        }
        if (!isOriginal) {
            tooltip.add(TextFormatting.DARK_RED + I18n.format("item.iceandfire.tide_trident.not_original"));
        }
        tooltip.add(I18n.format("item.iceandfire.tide_trident.desc_0"));
        tooltip.add(I18n.format("item.iceandfire.tide_trident.desc_1"));
    }
}
