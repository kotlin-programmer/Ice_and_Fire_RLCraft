package com.github.alexthe666.iceandfire.capability.entityeffect;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.util.IHearsSiren;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityEffectCapability implements IEntityEffectCapability {

    //TODO: Bite effect to replace mounting system
    public enum EntityEffectEnum {
        CHARMED(0, true, false) {
            @Override
            public boolean canBeApplied(EntityLivingBase entity) {
                return super.canBeApplied(entity) && (EntitySiren.isDrawnToSong(entity)) && !EntitySiren.isWearingEarplugs(entity);
            }
        },
        FROZEN(1, true, true) {
            @Override
            public boolean canBeApplied(EntityLivingBase entity) {
                return super.canBeApplied(entity) &&
                        !(entity instanceof EntityIceDragon);
            }
        },
        BLAZED(1, true, true) {
            @Override
            public boolean canBeApplied(EntityLivingBase entity) {
                return super.canBeApplied(entity) &&
                        !(entity instanceof EntityFireDragon);
            }
        },
        SHOCKED(2, true, true) {
            @Override
            public boolean canBeApplied(EntityLivingBase entity) {
                return super.canBeApplied(entity) &&
                        !(entity instanceof EntityLightningDragon);
            }
        },
        NONE(3, false, false) {
            @Override
            public boolean canBeApplied(EntityLivingBase entity) {
                return true;
            }
        },
        STONED(4, false, true);

        private final int priority;
        private final boolean syncToClient;
        private final boolean syncToTracking;

        EntityEffectEnum(int priority, boolean syncToClient, boolean syncToTracking) {
            this.priority = priority;
            this.syncToClient = syncToClient;
            this.syncToTracking = syncToTracking;
        }

        /**
         * Return true if the instance effect should take priority over the current active effect
         */
        public boolean takesPriority(EntityEffectEnum current) {
            return current == NONE || this.priority >= current.priority;
        }

        /**
         * Return true if the effect applied to player should be synced to their client
         */
        public boolean syncToAffectedClient() {
            return this.syncToClient;
        }

        /**
         * Return true if the effect applied to an entity/other player should be synced to tracking clients
         */
        public boolean syncToAllTracking() {
            return this.syncToTracking;
        }

        /**
         * Returns true if the effect can be applied to the given entity
         */
        public boolean canBeApplied(EntityLivingBase entity) {
            if(entity instanceof EntityPlayer) {
                return !(((EntityPlayer)entity).isSpectator() || ((EntityPlayer)entity).isCreative());
            }
            return entity instanceof EntityLiving;//Afaik there's nothing non-EntityLiving besides players that should have effects
        }
    }

    private EntityEffectEnum activeEffect = EntityEffectEnum.NONE;
    private int activeTime = 0;
    private int activeAdditionalData = 0;

    private EntityEffectEnum previousEffect = EntityEffectEnum.NONE;
    private int previousAdditionalData = 0;

    @Override
    public EntityEffectEnum getEffect() {
        return this.activeEffect;
    }

    @Override
    public EntityEffectEnum getPreviousEffect() {
        return this.previousEffect;
    }

    @Override
    public int getTime() {
        return this.activeTime;
    }

    @Override
    public int getAdditionalData() {
        return this.activeAdditionalData;
    }

    @Override
    public void setEffect(EntityEffectCapability.EntityEffectEnum effect, int time, int additional) {
        if(effect.takesPriority(this.activeEffect)) {
            this.activeEffect = effect;
            this.activeTime = time;
            this.activeAdditionalData = additional;
        }
    }

    @Override
    public void reset() {
        this.setEffect(EntityEffectEnum.NONE, 0, 0);
    }

    @Override
    public void setCharmed(int entityID) {
        this.setCharmed(IceAndFireConfig.ENTITY_SETTINGS.sirenMaxSingTime, entityID);
    }

    @Override
    public void setCharmed(int time, int entityID) {
        this.setEffect(EntityEffectEnum.CHARMED, time, entityID);
    }

    @Override
    public void setFrozen() {
        this.setFrozen(200);
    }

    @Override
    public void setFrozen(int time) {
        this.setFrozen(time, 0);
    }

    @Override
    public void setFrozen(int time, int severity) {
        this.setEffect(EntityEffectEnum.FROZEN, time, severity);
    }

    @Override
    public void setBlazed() {
        this.setBlazed(200);
    }

    @Override
    public void setBlazed(int time) {
        this.setBlazed(time, 0);
    }

    @Override
    public void setBlazed(int time, int severity) {
        this.setEffect(EntityEffectEnum.BLAZED, time, severity);
    }

    @Override
    public void setShocked() {
        this.setShocked(100);
    }

    @Override
    public void setShocked(int time) {
        this.setShocked(time, 0);
    }

    @Override
    public void setShocked(int time, int severity) {
        this.setEffect(EntityEffectEnum.SHOCKED, time, severity);
    }

    @Override
    public void setStoned() {
        this.setEffect(EntityEffectEnum.STONED, 0, 0);
    }


    @Override
    public boolean isCharmed() {
        return this.activeEffect == EntityEffectEnum.CHARMED;
    }

    @Override
    public boolean isFrozen() {
        return this.activeEffect == EntityEffectEnum.FROZEN;
    }

    @Override
    public boolean isBlazed() {
        return this.activeEffect == EntityEffectEnum.BLAZED;
    }

    @Override
    public boolean isShocked() {
        return this.activeEffect == EntityEffectEnum.SHOCKED;
    }

    @Override
    public boolean isStoned() {
        return this.activeEffect == EntityEffectEnum.STONED;
    }


    @Override
    public void tickUpdate(EntityLivingBase entity, World world) {
        if(world.isRemote) EntityEffectClientHandler.tickUpdate(entity, world, this);
        else EntityEffectHandler.tickUpdate(entity, world, this);
    }

    @Override
    public void tickTime() {
        this.activeTime--;
    }
    @Override
    public void tickData() {
        this.activeAdditionalData++;
    }

    @Override
    public boolean isDirty() {
        return this.activeEffect != this.previousEffect || this.activeAdditionalData != this.previousAdditionalData;
    }

    @Override
    public void markClean() {
        this.previousEffect = this.activeEffect;
        this.previousAdditionalData = this.activeAdditionalData;
    }

    @Override
    public EntitySiren getSiren(World world) {
        Entity temp = world.getEntityByID(this.getAdditionalData());
        if(temp instanceof EntitySiren) return (EntitySiren)temp;
        return null;
    }
}