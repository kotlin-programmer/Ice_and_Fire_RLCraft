package com.github.alexthe666.iceandfire.integration;

import net.minecraftforge.fml.common.Loader;

public abstract class CompatLoadUtil {

    private static final String CLAIMIT_MODID = "claimit";
    private static Boolean claimitLoaded;
    private static final String LYCANITE_MODID = "lycanitesmobs";
    private static Boolean lycaniteLoaded;
    private static final String THAUMCRAFT_MODID = "thaumcraft";
    private static Boolean thaumcraftLoaded;
    private static final String RLCOMBAT_MODID = "bettercombatmod";
    private static Boolean rlcombatLoaded;

    public static boolean isClaimItLoaded() {
        if(claimitLoaded == null) claimitLoaded = Loader.isModLoaded(CLAIMIT_MODID);
        return claimitLoaded;
    }

    public static boolean isLycanitesLoaded() {
        if(lycaniteLoaded == null) lycaniteLoaded = Loader.isModLoaded(LYCANITE_MODID);
        return lycaniteLoaded;
    }

    public static boolean isThaumcraftLoaded() {
        if(thaumcraftLoaded == null) thaumcraftLoaded = Loader.isModLoaded(THAUMCRAFT_MODID);
        return thaumcraftLoaded;
    }

    public static boolean isRLCombatLoaded() {
        if(rlcombatLoaded == null) rlcombatLoaded = Loader.isModLoaded(RLCOMBAT_MODID) && isRLCombatCorrectVersion();
        return rlcombatLoaded;
    }

    //RLCombat is 2.x.x, BetterCombat is 1.x.x
    private static boolean isRLCombatCorrectVersion() {
        String[] arrOfStr = Loader.instance().getIndexedModList().get(RLCOMBAT_MODID).getVersion().split("\\.");
        try {
            int i = Integer.parseInt(String.valueOf(arrOfStr[0]));
            if(i == 2) return true;
        }
        catch(Exception ignored) { }
        return false;
    }
}