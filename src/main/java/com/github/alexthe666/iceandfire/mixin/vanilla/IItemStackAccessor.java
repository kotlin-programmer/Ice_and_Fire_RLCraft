package com.github.alexthe666.iceandfire.mixin.vanilla;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStack.class)
public interface IItemStackAccessor {

	@Accessor(value = "capabilities", remap = false)
	CapabilityDispatcher iceAndFire$getCapabilities();
}