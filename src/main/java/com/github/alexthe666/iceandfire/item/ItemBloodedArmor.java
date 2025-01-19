package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.enums.EnumBloodedDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
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
}
