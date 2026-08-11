package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.api.ChainLightningUtils;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.enums.EnumBloodedDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBloodedArmor extends ItemArmor {

	public EnumBloodedDragonArmor armor_type;
	public EnumDragonEgg eggType;

	public ItemBloodedArmor(EnumDragonEgg eggType, EnumBloodedDragonArmor armorType, ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot) {
		super(material, renderIndex, slot);
		this.armor_type = armorType;
		this.eggType = eggType;
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		EnumDragonType type = armor_type.eggType.dragonType;
		if (type == EnumDragonType.FIRE) {
			return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 17 : 16);
		} else if (type == EnumDragonType.ICE) {
			return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 19 : 18);
		}
		return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 21 : 20);
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return "iceandfire:textures/models/armor/" + armor_type.name() + (renderIndex == 2 ? "_legs.png" : ".png");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(eggType.color + StatCollector.translateToLocal("dragon." + eggType.toString().toLowerCase()));
	}

	public void applyEffect(EntityPlayer player, EntityLivingBase attacker) {
		if (isCooldownActive(player)) {
			return;
		}
		EnumDragonType type = eggType.dragonType;
		if (type == EnumDragonType.FIRE) {
			if (attacker instanceof EntityIceDragon) {
				attacker.attackEntityFrom(DamageSource.IN_FIRE, 13.5F);
			}
			attacker.setFire(5);
			attacker.knockBack(attacker, 1F, player.posX - attacker.posX, player.posZ - attacker.posZ);
		}
		else if (type == EnumDragonType.ICE) {
			if (attacker instanceof EntityFireDragon) {
				attacker.attackEntityFrom(DamageSource.DROWN, 13.5F);
			}
			if (!player.world.isRemote) {
				IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(attacker);
				if (capability != null) capability.setFrozen(200);
			}
			attacker.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
			attacker.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 100, 2));
			attacker.knockBack(attacker, 1F, player.posX - attacker.posX, player.posZ - attacker.posZ);
		}
		else if (type == EnumDragonType.LIGHTNING) {
			if (attacker instanceof EntityFireDragon || attacker instanceof EntityIceDragon) {
				attacker.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 4F);
			}
			ChainLightningUtils.createChainLightningToTargetFromPlayer(attacker, player);
			attacker.knockBack(attacker, 1F, player.posX - attacker.posX, player.posZ - attacker.posZ);
		}
	}

	public static void applySetEffect(EntityPlayer player, EntityLivingBase attacker) {
		ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		if (helmet.isEmpty() || !(helmet.getItem() instanceof ItemBloodedArmor)) {
			return;
		}
		ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (chestplate.isEmpty() || !(chestplate.getItem() instanceof ItemBloodedArmor)) {
			return;
		}
		ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (leggings.isEmpty() || !(leggings.getItem() instanceof ItemBloodedArmor)) {
			return;
		}
		ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		if (boots.isEmpty() || !(boots.getItem() instanceof ItemBloodedArmor)) {
			return;
		}
		switch (player.world.rand.nextInt(4)) {
			case 0:
				((ItemBloodedArmor) helmet.getItem()).applyEffect(player, attacker);
				break;
			case 1:
				((ItemBloodedArmor) chestplate.getItem()).applyEffect(player, attacker);
				break;
			case 2:
				((ItemBloodedArmor) leggings.getItem()).applyEffect(player, attacker);
				break;
			default:
				((ItemBloodedArmor) boots.getItem()).applyEffect(player, attacker);
		}
	}

	private static boolean isCooldownActive(EntityPlayer player) {
		Item item = EnumBloodedDragonArmor.armor_black.chestplate;
		if (player.getCooldownTracker().hasCooldown(item)) {
			return true;
		}
		player.getCooldownTracker().setCooldown(item, IceAndFireConfig.MISC_SETTINGS.bloodedDragonArmorSetEffectCooldown);
		return false;
	}
}
