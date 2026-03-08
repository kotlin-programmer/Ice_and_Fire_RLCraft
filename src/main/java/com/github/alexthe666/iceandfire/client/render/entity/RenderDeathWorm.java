package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderDeathWorm extends RenderLiving<EntityDeathWorm> {

    private static final ResourceLocation TEXTURE_RED = new ResourceLocation("iceandfire:textures/models/deathworm/deathworm_red.png");
    private static final ResourceLocation TEXTURE_WHITE = new ResourceLocation("iceandfire:textures/models/deathworm/deathworm_white.png");
    private static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation("iceandfire:textures/models/deathworm/deathworm_yellow.png");

    public RenderDeathWorm(RenderManager renderManager) {
        super(renderManager, new ModelDeathWorm(), 0);
    }

    @Override
    protected void preRenderCallback(EntityDeathWorm entity, float f) {
        this.shadowSize = entity.getScaleForAge() / 3;
        GlStateManager.scale(entity.getScaleForAge(), entity.getScaleForAge(), entity.getScaleForAge());
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityDeathWorm entity) {
        switch(entity.getVariant()) {
            case 0: return TEXTURE_YELLOW;
            case 1: return TEXTURE_WHITE;
            case 2: return TEXTURE_RED;
            default: return TEXTURE_YELLOW;
        }
    }
}