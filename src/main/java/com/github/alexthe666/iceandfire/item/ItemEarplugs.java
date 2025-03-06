package com.github.alexthe666.iceandfire.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles", striprefs = true)
public class ItemEarplugs extends ItemArmor implements IBauble {

    public ItemEarplugs(){
        super(IafItemRegistry.earplugsArmor, 0, EntityEquipmentSlot.HEAD);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.earplugs");
        this.setRegistryName(IceAndFire.MODID, "earplugs");
    }

    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.HEAD;
    }
}
