package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

public interface ICustomStoneLayer {
    LayerRenderer<? extends EntityLivingBase> getStoneLayer(RenderLivingBase<? extends EntityLivingBase> render);
}