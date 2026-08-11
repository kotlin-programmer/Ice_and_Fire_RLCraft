package com.github.alexthe666.iceandfire.integration.theoneprobe;

import mcjty.theoneprobe.TheOneProbe;

public class TheOneProbeCompat {

    public static void register() {
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DragonInfoProvider());
    }
}