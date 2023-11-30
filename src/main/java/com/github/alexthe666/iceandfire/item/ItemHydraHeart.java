package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHydraHeart extends Item {

    public ItemHydraHeart() {
        super();
        this.setCreativeTab(IceAndFire.TAB);
        this.setTranslationKey("iceandfire.hydra_heart");
        this.setRegistryName(IceAndFire.MODID, "hydra_heart");
        if (IceAndFireConfig.MISC_SETTINGS.hydraHeartPassiveHealing) {
            this.setMaxDamage(IceAndFireConfig.MISC_SETTINGS.hydraHeartMaximumDurability);
        }
        this.maxStackSize = 1;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (IceAndFireConfig.MISC_SETTINGS.hydraHeartPassiveHealing) {
            return !oldStack.isItemEqual(newStack);
        }
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean unused2) {
        if (!IceAndFireConfig.MISC_SETTINGS.hydraHeartPassiveHealing) {
            return;
        }
        if (entity instanceof EntityPlayer && slot >= 0 && slot <= 8) {
            double healthPercentage = ((EntityPlayer) entity).getHealth() / Math.max(1, ((EntityPlayer) entity).getMaxHealth());
            if (healthPercentage < 1.0D) {
                int level = 0;
                if (healthPercentage < 0.25D) {
                    level = 3;
                } else if(healthPercentage < 0.5D) {
                    level = 2;
                } else if(healthPercentage < 0.75D) {
                    level = 1;
                }
                if (shouldApplyEffect((EntityPlayer) entity, level)) {
                    applyEffect((EntityPlayer) entity, stack, level);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (IceAndFireConfig.MISC_SETTINGS.hydraHeartPassiveHealing) {
            tooltip.add(I18n.format("item.iceandfire.hydra_heart.desc_0"));
            tooltip.add(I18n.format("item.iceandfire.hydra_heart.desc_1"));
        }
    }

    private boolean shouldApplyEffect(EntityPlayer player, int amplifier) {
        PotionEffect activeRegen = player.getActivePotionEffect(MobEffects.REGENERATION);
        return activeRegen == null || activeRegen.getAmplifier() < amplifier;
    }

    private void applyEffect(EntityPlayer player, ItemStack stack, int amplifier) {
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, amplifier, true, false));
        if (!player.isCreative()) {
            damageItem(stack);
        }
    }

    private void damageItem(ItemStack stack) {
        int itemDamage = getDamage(stack) + 1;
        setDamage(stack, itemDamage);
        if (itemDamage >= getMaxDamage(stack)) {
            stack.shrink(1);
        }
    }
}
