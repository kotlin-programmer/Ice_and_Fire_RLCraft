package com.github.alexthe666.iceandfire.api;

import com.github.alexthe666.iceandfire.capability.entityeffect.EntityEffectProvider;
import com.github.alexthe666.iceandfire.capability.entityproperties.EntityPropertiesProvider;
import net.minecraft.entity.EntityLivingBase;

public class InFCapabilities {

    public static IEntityEffectCapability getEntityEffectCapability(EntityLivingBase entity) {
        return entity.getCapability(EntityEffectProvider.ENTITY_EFFECT, null);
    }

    public static IEntityPropertiesCapability getEntityPropertiesCapability(EntityLivingBase entity) {
        return entity.getCapability(EntityPropertiesProvider.ENTITY_PROPERTIES, null);
    }
}