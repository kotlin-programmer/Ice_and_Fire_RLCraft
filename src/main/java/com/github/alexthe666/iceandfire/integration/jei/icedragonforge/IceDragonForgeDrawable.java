package com.github.alexthe666.iceandfire.integration.jei.icedragonforge;

import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class IceDragonForgeDrawable implements IDrawable {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/dragonforge_ice_jei.png");

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 120;
    }

    @Override
    public void draw(Minecraft minecraft, int xOffset, int yOffset) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(xOffset, yOffset, 3, 4, 170, 79);
        int scaledProgress = (minecraft.player.ticksExisted % 100) * 128 / 100;
        this.drawTexturedModalRect(xOffset + 9, yOffset + 19, 0, 166, scaledProgress, 38);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0).tex((float) (textureX) * 0.00390625F, (float) (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos(x + width, y + height, 0).tex((float) (textureX + width) * 0.00390625F, (float) (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos(x + width, y, 0).tex((float) (textureX + width) * 0.00390625F, (float) (textureY) * 0.00390625F).endVertex();
        bufferbuilder.pos(x, y, 0).tex((float) (textureX) * 0.00390625F, (float) (textureY) * 0.00390625F).endVertex();
        tessellator.draw();
    }
}