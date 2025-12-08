package com.github.alexthe666.iceandfire.capability.entityproperties;

import com.github.alexthe666.iceandfire.api.IEntityPropertiesCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class EntityPropertiesProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IEntityPropertiesCapability.class)
    public static final Capability<IEntityPropertiesCapability> ENTITY_PROPERTIES = null;

    private final IEntityPropertiesCapability instance = ENTITY_PROPERTIES.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> requested, EnumFacing facing) {
        return requested == ENTITY_PROPERTIES;
    }

    @Override
    public <T> T getCapability(Capability<T> requested, EnumFacing facing) {
        return requested == ENTITY_PROPERTIES ? ENTITY_PROPERTIES.cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return writeNBT(this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        readNBT(this.instance, null, nbt);
    }

    public static NBTBase writeNBT(IEntityPropertiesCapability capability, EnumFacing side) {
        return ENTITY_PROPERTIES.writeNBT(capability, side);
    }

    public static void readNBT(IEntityPropertiesCapability capability, EnumFacing side, NBTBase nbt) {
        ENTITY_PROPERTIES.readNBT(capability, side, nbt);
    }
}