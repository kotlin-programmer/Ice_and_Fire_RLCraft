package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSilverArmor extends ItemArmor implements ICustomRendered {

    public ItemSilverArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot) {
        super(material, renderIndex, slot);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.silver_" + getArmorPart(slot));
        this.setRegistryName(IceAndFire.MODID, "armor_silver_metal_" + getArmorPart(slot));
    }

    private String getArmorPart(EntityEquipmentSlot slot) {
        switch (slot) {
            case HEAD:
                return "helmet";
            case CHEST:
                return "chestplate";
            case LEGS:
                return "leggings";
            case FEET:
                return "boots";
        }
        return "";
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 3 : 2);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "iceandfire:textures/models/armor/" + (slot == EntityEquipmentSlot.LEGS ? "armor_silver_metal_layer_2" : "armor_silver_metal_layer_1") + ".png";
    }
}
