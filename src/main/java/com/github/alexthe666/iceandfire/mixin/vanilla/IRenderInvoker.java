package com.github.alexthe666.iceandfire.mixin.vanilla;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Render.class)
public interface IRenderInvoker {
	
	@Invoker("getEntityTexture")
	ResourceLocation invokeGetEntityTexture(Entity entity);
}