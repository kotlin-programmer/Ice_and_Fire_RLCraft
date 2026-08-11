package com.github.alexthe666.iceandfire.integration.theoneprobe;

import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;
import com.github.alexthe666.iceandfire.integration.theoneprobe.TheOneProbeCompat;

public class TheOneProbeCompatBridge {

    public static void loadTheOneProbeCompat() {
        if (CompatLoadUtil.isTheOneProbeLoaded()) {
            TheOneProbeCompat.register();
        }
    }
}
