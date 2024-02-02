package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EntityAIRestrictSunFlying extends EntityAIBase {
    private final EntityCreature entity;

    public EntityAIRestrictSunFlying(EntityCreature creature) {
        this.entity = creature;
    }

    public boolean shouldExecute() {
        return this.entity.world.isDaytime() && this.entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
    }

    public void startExecuting() {
        ((GhostPathNavigator)this.entity.getNavigator()).setAvoidSun(true);
    }

    public void resetTask() {
        ((GhostPathNavigator)this.entity.getNavigator()).setAvoidSun(false);
    }
}