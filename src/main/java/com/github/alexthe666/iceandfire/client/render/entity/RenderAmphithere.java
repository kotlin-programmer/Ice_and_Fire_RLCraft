package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAmphithere extends RenderLiving<EntityAmphithere> {

	private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_blue.png");
	private static final ResourceLocation TEXTURE_BLUE_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_blue_blink.png");
	private static final ResourceLocation TEXTURE_GREEN = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_green.png");
	private static final ResourceLocation TEXTURE_GREEN_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_green_blink.png");
	private static final ResourceLocation TEXTURE_PURPLE = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_purple.png");
	private static final ResourceLocation TEXTURE_PURPLE_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_purple_blink.png");
	private static final ResourceLocation TEXTURE_RED = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_red.png");
	private static final ResourceLocation TEXTURE_RED_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_red_blink.png");
	private static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_yellow.png");
	private static final ResourceLocation TEXTURE_YELLOW_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_yellow_blink.png");
	private static final ResourceLocation TEXTURE_RAINBOW = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_rainbow.png");
	private static final ResourceLocation TEXTURE_RAINBOW_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_rainbow_blink.png");
	private static final String NISCHHELM = "Nischhelm";
	private static final String RISKY = "Risky";

	public RenderAmphithere(RenderManager renderManager) {
		super(renderManager, new ModelAmphithere(), 1.6F);
	}

	@Override
	public void preRenderCallback(EntityAmphithere entitylivingbaseIn, float partialTickTime) {
		String s = TextFormatting.getTextWithoutFormattingCodes(entitylivingbaseIn.getName());
		if (NISCHHELM.equals(s)) {
			GlStateManager.translate(0, -entitylivingbaseIn.height, 0);
			GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(1.1F, 1.1F, 1.1F);
		} else {
			GlStateManager.scale(2.0F, 2.0F, 2.0F);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAmphithere amphithere) {
		String s = TextFormatting.getTextWithoutFormattingCodes(amphithere.getName());
		if (RISKY.equals(s)) {
			return amphithere.isBlinking() ? TEXTURE_RAINBOW_BLINK : TEXTURE_RAINBOW;
		} else {
			switch(amphithere.getVariant()) {
				case 0: return amphithere.isBlinking() ? TEXTURE_BLUE_BLINK : TEXTURE_BLUE;
				case 1: return amphithere.isBlinking() ? TEXTURE_GREEN_BLINK : TEXTURE_GREEN;
				case 2: return amphithere.isBlinking() ? TEXTURE_PURPLE_BLINK : TEXTURE_PURPLE;
				case 3: return amphithere.isBlinking() ? TEXTURE_RED_BLINK : TEXTURE_RED;
				case 4: return amphithere.isBlinking() ? TEXTURE_YELLOW_BLINK : TEXTURE_YELLOW;
				case 5: return amphithere.isBlinking() ? TEXTURE_RAINBOW_BLINK : TEXTURE_RAINBOW;
				default: return TEXTURE_GREEN;
			}
		}
	}
}