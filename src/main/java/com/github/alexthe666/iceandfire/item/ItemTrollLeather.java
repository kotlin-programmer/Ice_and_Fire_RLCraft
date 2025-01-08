package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.minecraft.item.Item;

public class ItemTrollLeather extends Item {

    public ItemTrollLeather(EnumTroll troll, String gameName, String name) {
        this.setRegistryName(IceAndFire.MODID, gameName);
        this.setTranslationKey(name);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
    }
}
