package com.github.alexthe666.iceandfire.capability.entityproperties;

import com.github.alexthe666.iceandfire.api.IEntityPropertiesCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class EntityPropertiesStorage implements Capability.IStorage<IEntityPropertiesCapability> {
    public static final String deathwormLaunched = "DeathwormLaunched";
    public static final String deathwormReceded = "DeathwormReceded";
    public static final String deathwormLungeTicks = "DeathwormLungeTicks";
    public static final String previousDeathwormLungeTicks = "PreviousDeathwormLungeTicks";

    @Override
    public NBTBase writeNBT(Capability<IEntityPropertiesCapability> capability, IEntityPropertiesCapability instance, EnumFacing side) {
        if (instance == null) {
            return null;
        }
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean(deathwormLaunched, instance.isDeathwormLaunched());
        compound.setBoolean(deathwormReceded, instance.isDeathwormReceded());
        compound.setInteger(deathwormLungeTicks, instance.getDeathwormLungeTicks());
        compound.setInteger(previousDeathwormLungeTicks, instance.getPreviousDeathwormLungeTicks());
        return compound;
    }

    @Override
    public void readNBT(Capability<IEntityPropertiesCapability> capability, IEntityPropertiesCapability instance, EnumFacing side, NBTBase nbt) {
        if (instance == null || nbt == null) {
            return;
        }
        if (nbt instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound)nbt;
            if (compound.hasKey(deathwormLaunched)) {
                instance.setDeathwormLaunched(compound.getBoolean(deathwormLaunched));
            }
            if (compound.hasKey(deathwormReceded)) {
                instance.setDeathwormReceded(compound.getBoolean(deathwormReceded));
            }
            if (compound.hasKey(deathwormLungeTicks)) {
                instance.setDeathwormLungeTicks(compound.getInteger(deathwormLungeTicks));
            }
            if (compound.hasKey(previousDeathwormLungeTicks)) {
                instance.setPreviousDeathwormLungeTicks(compound.getInteger(previousDeathwormLungeTicks));
            }
        }
    }
}