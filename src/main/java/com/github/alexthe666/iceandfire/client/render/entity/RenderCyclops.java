package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelCyclops;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCyclops extends RenderLiving<EntityCyclops> {

	private static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_0.png");
	private static final ResourceLocation BLINK_0_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_0_blink.png");
	private static final ResourceLocation BLINDED_0_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_0_injured.png");
	private static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_1.png");
	private static final ResourceLocation BLINK_1_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_1_blink.png");
	private static final ResourceLocation BLINDED_1_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_1_injured.png");
	private static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_2.png");
	private static final ResourceLocation BLINK_2_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_2_blink.png");
	private static final ResourceLocation BLINDED_2_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_2_injured.png");
	private static final ResourceLocation TEXTURE_3 = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_3.png");
	private static final ResourceLocation BLINK_3_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_3_blink.png");
	private static final ResourceLocation BLINDED_3_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_3_injured.png");

	public RenderCyclops(RenderManager renderManager) {
		super(renderManager, new ModelCyclops(), 1.6F);
	}

	@Override
	public void preRenderCallback(EntityCyclops entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(2.25F, 2.25F, 2.25F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCyclops cyclops) {
		switch(cyclops.getVariant()) {
			case 0: return cyclops.isBlinded() ? BLINDED_0_TEXTURE : cyclops.isBlinking() ? BLINK_0_TEXTURE : TEXTURE_0;
			case 1: return cyclops.isBlinded() ? BLINDED_1_TEXTURE : cyclops.isBlinking() ? BLINK_1_TEXTURE : TEXTURE_1;
			case 2: return cyclops.isBlinded() ? BLINDED_2_TEXTURE : cyclops.isBlinking() ? BLINK_2_TEXTURE : TEXTURE_2;
			case 3: return cyclops.isBlinded() ? BLINDED_3_TEXTURE : cyclops.isBlinking() ? BLINK_3_TEXTURE : TEXTURE_3;
			default: return TEXTURE_0;
		}
	}
}