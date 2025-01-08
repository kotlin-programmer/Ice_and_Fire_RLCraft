package com.github.alexthe666.iceandfire.client.render.entity.player;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.item.ItemCopperArmor;
import com.github.alexthe666.iceandfire.item.ItemDeathwormArmor;
import com.github.alexthe666.iceandfire.item.ItemScaleArmor;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentArmor;
import com.github.alexthe666.iceandfire.item.ItemSilverArmor;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderModArmor {

	private static final Map<EntityEquipmentSlot, Boolean> MOD_EQUIPMENT_MAP = new HashMap<>();

	@SubscribeEvent
	public void playerRender(RenderPlayerEvent.Pre event) {
		if (!IceAndFireConfig.CLIENT_SETTINGS.fixArmorRenderingBugs) {
			return;
		}
		if (event.getEntityPlayer() instanceof AbstractClientPlayer) {
			boolean hasModHelmet = false;
			boolean hasModChestplate = false;
			boolean hasModLeggings = false;
			boolean hasModBoots = false;

			for (ItemStack stack : event.getEntityPlayer().getArmorInventoryList()) {
				ItemStack disguise = getPhantomThreadDisguise(stack);
				if (disguise != null) {
					stack = disguise;
				}
				if (stack.getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) stack.getItem();
					if (isModArmor(armor)) {
						if (isHelmet(armor)) {
							hasModHelmet = true;
						} else if (isChestplate(armor)) {
							hasModChestplate = true;
						} else if (isLeggings(armor)) {
							hasModLeggings = true;
						} else if (isBoots(armor)) {
							hasModBoots = true;
						}
					}
				}
			}

			ModelPlayer playerModel = event.getRenderer().getMainModel();
			if (hasModHelmet) {
				MOD_EQUIPMENT_MAP.put(EntityEquipmentSlot.HEAD, true);
				playerModel.bipedHeadwear.isHidden = true;
			} else if (MOD_EQUIPMENT_MAP.getOrDefault(EntityEquipmentSlot.HEAD, false)) {
				MOD_EQUIPMENT_MAP.put(EntityEquipmentSlot.HEAD, false);
				playerModel.bipedHeadwear.isHidden = false;
			}

			if (hasModChestplate) {
				MOD_EQUIPMENT_MAP.put(EntityEquipmentSlot.CHEST, true);
				playerModel.bipedBodyWear.isHidden = true;
				playerModel.bipedLeftArmwear.isHidden = true;
				playerModel.bipedRightArmwear.isHidden = true;
			} else if (MOD_EQUIPMENT_MAP.getOrDefault(EntityEquipmentSlot.CHEST, false)) {
				MOD_EQUIPMENT_MAP.put(EntityEquipmentSlot.CHEST, false);
				playerModel.bipedBodyWear.isHidden = false;
				playerModel.bipedLeftArmwear.isHidden = false;
				playerModel.bipedRightArmwear.isHidden = false;
			}

			if (hasModLeggings || hasModBoots) {
				MOD_EQUIPMENT_MAP.put(EntityEquipmentSlot.LEGS, true);
				playerModel.bipedLeftLegwear.isHidden = true;
				playerModel.bipedRightLegwear.isHidden = true;
			} else if (MOD_EQUIPMENT_MAP.getOrDefault(EntityEquipmentSlot.LEGS, false)) {
				MOD_EQUIPMENT_MAP.put(EntityEquipmentSlot.LEGS, false);
				playerModel.bipedLeftLegwear.isHidden = false;
				playerModel.bipedRightLegwear.isHidden = false;
			}
		}
	}

	public boolean isModArmor(ItemArmor armor) {
        return armor instanceof ItemScaleArmor
                || armor instanceof ItemSeaSerpentArmor
                || armor instanceof ItemTrollArmor
                || armor instanceof ItemDeathwormArmor
                || armor instanceof ItemSilverArmor
                || armor instanceof ItemCopperArmor;
    }

	public ItemStack getPhantomThreadDisguise(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null || tagCompound.isEmpty()) {
			return null;
		}
		if (!tagCompound.hasKey("classy_hat_invisible")) {
			return null;
		}
		if (!tagCompound.hasKey("classy_hat_disguise")) {
			return ItemStack.EMPTY;
		}
		NBTTagCompound diguise = tagCompound.getCompoundTag("classy_hat_disguise");
		if (diguise.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return new ItemStack(diguise);
	}

	public boolean isHelmet(ItemArmor armor) {
		return armor.getEquipmentSlot() == EntityEquipmentSlot.HEAD;
	}

	public boolean isChestplate(ItemArmor armor) {
		return armor.getEquipmentSlot() == EntityEquipmentSlot.CHEST;
	}

	public boolean isLeggings(ItemArmor armor) {
		return armor.getEquipmentSlot() == EntityEquipmentSlot.LEGS;
	}

	public boolean isBoots(ItemArmor armor) {
		return armor.getEquipmentSlot() == EntityEquipmentSlot.FEET;
	}
}