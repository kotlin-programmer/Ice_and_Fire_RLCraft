package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelGorgonHead;
import com.github.alexthe666.iceandfire.client.model.ModelGorgonHeadActive;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGorgonHead extends TileEntitySpecialRenderer<TileEntity> {

	private static final ModelBase ACTIVE_MODEL = new ModelGorgonHeadActive();
	private static final ModelBase INACTIVE_MODEL = new ModelGorgonHead();
	private static final ResourceLocation ACTIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/head_active.png");
	private static final ResourceLocation INACTIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/head_inactive.png");
	private final boolean active;

	public RenderGorgonHead(boolean active) {
		this.active = active;
	}

	@Override
	public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y - 0.75D, z + 0.5D);
		if (this.active) {
			this.bindTexture(ACTIVE_TEXTURE);
			ACTIVE_MODEL.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else {
			this.bindTexture(INACTIVE_TEXTURE);
			INACTIVE_MODEL.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		GlStateManager.popMatrix();
	}

}
