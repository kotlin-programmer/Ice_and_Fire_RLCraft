package com.github.alexthe666.iceandfire.integration;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.core.ModRecipes;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class IceAndFireJEIPlugin implements IModPlugin {

    private static void addDescription(IModRegistry registry, ItemStack stack) {
        registry.addIngredientInfo(stack, ItemStack.class, stack.getTranslationKey() + ".jei_desc");
    }

    public void register(IModRegistry registry) {
        addDescription(registry, new ItemStack(IafItemRegistry.fire_dragon_blood));
        addDescription(registry, new ItemStack(IafItemRegistry.ice_dragon_blood));
        addDescription(registry, new ItemStack(IafItemRegistry.lightning_dragon_blood));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_red));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_bronze));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_gray));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_green));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_blue));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_white));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_sapphire));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_silver));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_amethyst));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_copper));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_electric));
        addDescription(registry, new ItemStack(IafItemRegistry.dragonegg_black));
        addDescription(registry, new ItemStack(IafItemRegistry.dragon_skull));
        addDescription(registry, new ItemStack(IafItemRegistry.dragon_skull, 1, 1));
        addDescription(registry, new ItemStack(IafItemRegistry.dragon_skull, 1, 2));
        addDescription(registry, new ItemStack(IafItemRegistry.fire_stew));
        addDescription(registry, new ItemStack(IafItemRegistry.frost_stew));
        addDescription(registry, new ItemStack(IafItemRegistry.lightning_stew));

        for (EnumSkullType skull : EnumSkullType.values()) {
            addDescription(registry, new ItemStack(skull.skull_item));
        }
        for (ItemStack stack : ModRecipes.BANNER_ITEMS) {
            registry.addIngredientInfo(stack, ItemStack.class, "item.iceandfire.custom_banner.jei_desc");
        }
    }
}
