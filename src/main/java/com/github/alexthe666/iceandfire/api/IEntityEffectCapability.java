package com.github.alexthe666.iceandfire.api;

import com.github.alexthe666.iceandfire.capability.entityeffect.EntityEffectCapability;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public interface IEntityEffectCapability {
    EntityEffectCapability.EntityEffectEnum getEffect();
    EntityEffectCapability.EntityEffectEnum getPreviousEffect();
    int getTime();
    int getAdditionalData();

    void setEffect(EntityEffectCapability.EntityEffectEnum effect, int time, int additional);

    void reset();
    void setCharmed(int entityID);
    void setCharmed(int time, int entityID);
    void setFrozen();
    void setFrozen(int time);
    void setFrozen(int time, int severity);
    void setBlazed();
    void setBlazed(int time);
    void setBlazed(int time, int severity);
    void setShocked();
    void setShocked(int time);
    void setShocked(int time, int severity);
    void setSpooked(int entityID);
    void setSpooked(int time, int entityID);
    void setStoned();

    boolean isCharmed();
    boolean isFrozen();
    boolean isBlazed();
    boolean isShocked();
    boolean isSpooked();
    boolean isStoned();

    void tickUpdate(EntityLivingBase entity, World world);
    void tickTime();
    void tickData();
    boolean isDirty();
    void markClean();

    EntitySiren getSiren(World world);

    EntityGhost getGhost(World world);
}