package com.github.alexthe666.iceandfire.client.gui.bestiary;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ChangePageButton extends GuiButton {
	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png");
	private final boolean right;
	private final int color;

	public ChangePageButton(int id, int x, int y, boolean right, int color) {
		super(id, x, y, 23, 10, "");
		this.right = right;
		this.color = color;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		if (this.enabled) {
			boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			mc.renderEngine.bindTexture(TEXTURE);
			int i = 0;
			int j = 64;
			if (flag) {
				i += 23;
			}

			if (!this.right) {
				j += 13;
			}
			j += color * 23;

			this.drawTexturedModalRect(this.x, this.y, i, j, width, height);
		}
	}
}