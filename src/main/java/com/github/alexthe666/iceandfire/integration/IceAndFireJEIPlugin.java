package com.github.alexthe666.iceandfire.integration;

import com.github.alexthe666.iceandfire.core.ModItems;
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
        addDescription(registry, new ItemStack(ModItems.fire_dragon_blood));
        addDescription(registry, new ItemStack(ModItems.ice_dragon_blood));
        addDescription(registry, new ItemStack(ModItems.lightning_dragon_blood));
        addDescription(registry, new ItemStack(ModItems.dragonegg_red));
        addDescription(registry, new ItemStack(ModItems.dragonegg_bronze));
        addDescription(registry, new ItemStack(ModItems.dragonegg_gray));
        addDescription(registry, new ItemStack(ModItems.dragonegg_green));
        addDescription(registry, new ItemStack(ModItems.dragonegg_blue));
        addDescription(registry, new ItemStack(ModItems.dragonegg_white));
        addDescription(registry, new ItemStack(ModItems.dragonegg_sapphire));
        addDescription(registry, new ItemStack(ModItems.dragonegg_silver));
        addDescription(registry, new ItemStack(ModItems.dragonegg_amethyst));
        addDescription(registry, new ItemStack(ModItems.dragonegg_copper));
        addDescription(registry, new ItemStack(ModItems.dragonegg_electric));
        addDescription(registry, new ItemStack(ModItems.dragonegg_black));
        addDescription(registry, new ItemStack(ModItems.dragon_skull));
        addDescription(registry, new ItemStack(ModItems.dragon_skull, 1, 1));
        addDescription(registry, new ItemStack(ModItems.dragon_skull, 1, 2));
        addDescription(registry, new ItemStack(ModItems.fire_stew));
        addDescription(registry, new ItemStack(ModItems.frost_stew));
        addDescription(registry, new ItemStack(ModItems.lightning_stew));

        for (EnumSkullType skull : EnumSkullType.values()) {
            addDescription(registry, new ItemStack(skull.skull_item));
        }
    }
}
