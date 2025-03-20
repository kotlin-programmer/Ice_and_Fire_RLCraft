package com.github.alexthe666.iceandfire.integration.baubles.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHeadBauble extends ModelBase {
    private final ModelRenderer bb_main;

    public ModelHeadBauble() {
        textureWidth = 64;
        textureHeight = 32;

        bb_main = new ModelRenderer(this, 0, 0);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.addBox(-4.0F, -32.0F, -4.0F, 8, 8, 8, 0.75F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        bb_main.render(scale);
    }
}
