package com.github.alexthe666.iceandfire.integration;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class LycanitesCompat {

    private static final String PARALYSIS = "lycanitesmobs:paralysis";
    private static final String PENETRATION = "lycanitesmobs:penetration";

    private static Potion PARALYSIS_POTION;

    public static boolean isEnabled() {
        return CompatLoadUtil.isLycanitesMobsLoaded();
    }

    public static boolean canHurt(EntityLivingBase target, EntityLivingBase attacker) {
        if (target instanceof TameableCreatureEntity) {
            TameableCreatureEntity creature = (TameableCreatureEntity) target;
            return !creature.isTamed() || creature.getOwner() != attacker;
        }
        return true;
    }

    public static void applyParalysis(Entity entity, int duration) {
        if (entity instanceof EntityLivingBase && isEnabled()) {
            try {
                if (PARALYSIS_POTION == null) {
                    PARALYSIS_POTION = Potion.getPotionFromResourceLocation(PARALYSIS);
                }
                EntityLivingBase livingBase = (EntityLivingBase)entity;
                if (PARALYSIS_POTION == null) {
                    return;
                }
                if (!livingBase.isPotionActive(PARALYSIS_POTION)) {
                    livingBase.addPotionEffect(new PotionEffect(PARALYSIS_POTION, duration));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isParalysisEffect(PotionEffect effect) {
        if (effect == null) {
            return false;
        }
        if (isEnabled()) {
            ResourceLocation resource = effect.getPotion().getRegistryName();
            if (resource == null) return false;
            return resource.toString().equals(PARALYSIS);
        }
        return false;
    }

    public static boolean isPenetrationEffect(PotionEffect effect) {
        if (effect == null) {
            return false;
        }
        if (isEnabled()) {
            ResourceLocation resource = effect.getPotion().getRegistryName();
            if (resource == null) return false;
            return resource.toString().equals(PENETRATION);
        }
        return false;
    }
}