package com.github.alexthe666.iceandfire.mixin.vanilla;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.network.datasync.DataParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityVillager.class)
public interface IEntityVillagerAccessor {
	
	@Accessor(value = "careerId")
	void setCareerId(int val);
	
	@Accessor(value = "PROFESSION")
	static DataParameter<Integer> getProfessionParameter() {
		throw new UnsupportedOperationException("Profession Accessor Failed to Apply");
	}
}