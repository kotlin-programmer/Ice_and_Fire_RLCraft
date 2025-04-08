package com.github.alexthe666.iceandfire.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles", striprefs = true)
public class ItemBlindfold extends ItemArmor implements IBauble {

	public ItemBlindfold() {
		super(IafItemRegistry.blindfoldArmor, 0, EntityEquipmentSlot.HEAD);
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey("iceandfire.blindfold");
		this.setRegistryName(IceAndFire.MODID, "blindfold");
	}

	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 30, 2, true, false));
	}

	@Optional.Method(modid = "baubles")
	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.HEAD;
	}

	@Optional.Method(modid = "baubles")
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player){
		player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 30, 2, true, false));
	}
}
