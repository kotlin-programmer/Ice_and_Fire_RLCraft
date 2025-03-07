package com.github.alexthe666.iceandfire.integration.baubles;

import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;
import com.github.alexthe666.iceandfire.integration.baubles.client.model.layer.LayerHeadBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;

import java.util.Map;

public class BaublesCompatBridge {

    public static void loadBaublesClientModels() {
        if(CompatLoadUtil.isBaublesLoaded()) {
            addRenderLayers();
        }
    }

    public static void addRenderLayers() {
        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();

        addLayersToSkin(skinMap.get("default"));
        addLayersToSkin(skinMap.get("slim"));
    }

    private static void addLayersToSkin(RenderPlayer renderPlayer) {
        renderPlayer.addLayer(new LayerHeadBauble(renderPlayer));
    }
}
