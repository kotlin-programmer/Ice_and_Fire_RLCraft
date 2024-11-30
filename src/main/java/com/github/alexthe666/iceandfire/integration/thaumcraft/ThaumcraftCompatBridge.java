package com.github.alexthe666.iceandfire.integration.thaumcraft;

import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;

/**
 * Created by Joseph on 6/23/2018.
 */
public class ThaumcraftCompatBridge {

	public static void loadThaumcraftCompat() {
		if (CompatLoadUtil.isThaumcraftLoaded()) {
			ThaumcraftCompat.register();
		}
	}
}