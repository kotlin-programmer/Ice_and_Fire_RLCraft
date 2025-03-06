package com.github.alexthe666.iceandfire.api;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class SensesUtils {

    public static boolean isBlind(EntityLivingBase entity){
        if(entity == null) return false;
        if(entity.isPotionActive(MobEffects.BLINDNESS)) return true;

        ItemStack helmet = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if(helmet.getItem() == IafItemRegistry.blindfold)
            return true;
        else if(CompatLoadUtil.isBaublesLoaded() && entity instanceof EntityPlayer)
            return BaublesApi.getBaublesHandler((EntityPlayer)entity).getStackInSlot(BaubleType.HEAD.getValidSlots()[0]).getItem() == IafItemRegistry.blindfold;

        return false;
    }

    public static boolean isDeaf(EntityLivingBase entity){
        if(entity == null) return false;

        ItemStack helmet = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if(helmet.getItem() == IafItemRegistry.earplugs || helmet != ItemStack.EMPTY && helmet.getItem().getTranslationKey().contains("earmuff"))
            return true;
        else if (CompatLoadUtil.isBaublesLoaded() && entity instanceof EntityPlayer)
            return BaublesApi.getBaublesHandler((EntityPlayer)entity).getStackInSlot(BaubleType.HEAD.getValidSlots()[0]).getItem() == IafItemRegistry.earplugs;

        return false;
    }
}
