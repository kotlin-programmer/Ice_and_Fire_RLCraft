package com.github.alexthe666.iceandfire.integration.crafttweaker;

import net.minecraftforge.fml.common.Loader;

public class CraftTweakerCompatBridge {
    private static final String COMPAT_MOD_ID = "crafttweaker";

    public static void loadCraftTweakerCompat() {
        if (Loader.isModLoaded(COMPAT_MOD_ID)) {
            CraftTweakerCompat.preInit();
        }
    }
}
