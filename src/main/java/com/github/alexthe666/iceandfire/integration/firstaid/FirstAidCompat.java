package com.github.alexthe666.iceandfire.integration.firstaid;

import com.github.alexthe666.iceandfire.item.ItemBloodedArmor;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import ichttt.mods.firstaid.api.event.FirstAidLivingDamageEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FirstAidCompat {
    @SubscribeEvent
    public static void onLivingDamageEvent(FirstAidLivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.isProjectile()
                || source.isFireDamage()
                || source.isExplosion()
                || source.isMagicDamage()) {
            return;
        }
        if (source.getImmediateSource() == source.getTrueSource() && source.getTrueSource() instanceof EntityLivingBase) {
            EntityPlayer player = event.getEntityPlayer();
            EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
            AbstractPlayerDamageModel before = event.getBeforeDamage();
            AbstractPlayerDamageModel after = event.getAfterDamage();
            if (wasHurt(before.HEAD, after.HEAD)) {
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!helmet.isEmpty() && helmet.getItem() instanceof ItemBloodedArmor) {
                    ((ItemBloodedArmor) helmet.getItem()).applyEffect(player, attacker);
                }
                return;
            }
            if (wasHurt(before.BODY, after.BODY)
                    || wasHurt(before.LEFT_ARM, after.LEFT_ARM)
                    || wasHurt(before.RIGHT_ARM, after.RIGHT_ARM)) {
                ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (!chestplate.isEmpty() && chestplate.getItem() instanceof ItemBloodedArmor) {
                    ((ItemBloodedArmor) chestplate.getItem()).applyEffect(player, attacker);
                }
                return;
            }
            if (wasHurt(before.LEFT_LEG, after.LEFT_LEG) || wasHurt(before.RIGHT_LEG, after.RIGHT_LEG)) {
                ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                if (!leggings.isEmpty() && leggings.getItem() instanceof ItemBloodedArmor) {
                    ((ItemBloodedArmor) leggings.getItem()).applyEffect(player, attacker);
                }
                return;
            }
            if (wasHurt(before.LEFT_FOOT, after.LEFT_FOOT) || wasHurt(before.RIGHT_FOOT, after.RIGHT_FOOT)) {
                ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                if (!boots.isEmpty() && boots.getItem() instanceof ItemBloodedArmor) {
                    ((ItemBloodedArmor) boots.getItem()).applyEffect(player, attacker);
                }
                return;
            }
            if (event.getUndistributedDamage() > 0F) {
                ItemBloodedArmor.applySetEffect(player, attacker);
            }
        }
    }

    private static boolean wasHurt(AbstractDamageablePart before, AbstractDamageablePart after) {
        return before.currentHealth > after.currentHealth;
    }
}
