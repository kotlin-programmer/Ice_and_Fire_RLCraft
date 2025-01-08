package com.github.alexthe666.iceandfire.integration;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.EntityMultipartPart;
import com.github.alexthe666.iceandfire.integration.waila.HUDHandlerDragon;
import com.github.alexthe666.iceandfire.integration.waila.HUDHandlerMultipartMob;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class IceAndFireWailaPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerHeadProvider(HUDHandlerMultipartMob.INSTANCE, EntityMultipartPart.class);
        registrar.registerBodyProvider(HUDHandlerMultipartMob.INSTANCE, EntityMultipartPart.class);
        registrar.registerBodyProvider(HUDHandlerDragon.INSTANCE, EntityDragonBase.class);
        registrar.registerTailProvider(HUDHandlerMultipartMob.INSTANCE, EntityMultipartPart.class);
        registrar.registerNBTProvider(HUDHandlerMultipartMob.INSTANCE, EntityMultipartPart.class);
    }
}