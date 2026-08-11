package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockDragonScalesPile;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemDragonScales;
import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Map;

public enum EnumDragonEgg {
    RED(0, TextFormatting.DARK_RED, EnumDragonType.FIRE), GREEN(1, TextFormatting.DARK_GREEN, EnumDragonType.FIRE), BRONZE(2, TextFormatting.GOLD, EnumDragonType.FIRE), GRAY(3, TextFormatting.GRAY, EnumDragonType.FIRE),
    BLUE(4, TextFormatting.AQUA, EnumDragonType.ICE), WHITE(5, TextFormatting.WHITE, EnumDragonType.ICE), SAPPHIRE(6, TextFormatting.BLUE, EnumDragonType.ICE), SILVER(7, TextFormatting.DARK_GRAY, EnumDragonType.ICE),
    ELECTRIC(8, TextFormatting.DARK_BLUE, EnumDragonType.LIGHTNING), AMETHYST(9, TextFormatting.LIGHT_PURPLE, EnumDragonType.LIGHTNING), COPPER(10, TextFormatting.GOLD, EnumDragonType.LIGHTNING),
    BLACK(11, TextFormatting.DARK_GRAY, EnumDragonType.LIGHTNING);

    private static final Map<Integer, EnumDragonEgg> META_LOOKUP = Maps.newHashMap();

    static {
        for (EnumDragonEgg egg : values()) {
            META_LOOKUP.put(egg.meta, egg);
        }
    }

    public final String resourceName;
    public final int meta;
    public final TextFormatting color;
    public final EnumDragonType dragonType;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg")
    public Item egg;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales")
    public Item scales;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_pile")
    public BlockDragonScalesPile pile;

    EnumDragonEgg(int meta, TextFormatting color, EnumDragonType dragonType) {
        this.resourceName = this.name().toLowerCase();
        this.meta = meta;
        this.color = color;
        this.dragonType = dragonType;
    }

    public static EnumDragonEgg byMetadata(int meta) {
        EnumDragonEgg i = META_LOOKUP.get(meta);
        return i == null ? RED : i;
    }

    public static void initEggs() {
        for (EnumDragonEgg color : EnumDragonEgg.values()) {
            color.egg = new ItemDragonEgg(color);
            color.scales = new ItemDragonScales(color.resourceName, color.color);
            color.pile = new BlockDragonScalesPile(color.resourceName, color.color, color.scales);
        }
    }
}
