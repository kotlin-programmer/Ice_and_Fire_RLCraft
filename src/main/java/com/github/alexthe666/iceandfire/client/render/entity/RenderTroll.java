package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RenderTroll extends RenderLiving<EntityTroll> implements ICustomStoneLayer {

	public RenderTroll(RenderManager renderManager) {
		super(renderManager, new ModelTroll(), 0.9F);
		this.layerRenderers.add(new LayerTrollWeapon(this));
		this.layerRenderers.add(new LayerTrollEyes(this));
	}

	@Override
	public void preRenderCallback(EntityTroll entitylivingbaseIn, float partialTickTime) { }

	@Override
	protected ResourceLocation getEntityTexture(EntityTroll troll) {
		return troll.getType().TEXTURE;
	}

	@Override
	public LayerRenderer<EntityLivingBase> getStoneLayer(RenderLivingBase<? extends EntityLivingBase> render) {
		return new LayerTrollStone(render);
	}

	@SideOnly(Side.CLIENT)
	public static class LayerTrollWeapon implements LayerRenderer<EntityTroll> {

		private final RenderTroll renderer;

		public LayerTrollWeapon(RenderTroll renderer) {
			this.renderer = renderer;
		}

		@Override
		public void doRenderLayer(EntityTroll entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
			if (entity.getWeaponType() != null && !EntityGorgon.isStoneMob(entity)) {
				this.renderer.bindTexture(entity.getWeaponType().TEXTURE);
				this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class LayerTrollEyes implements LayerRenderer<EntityTroll> {

		private final RenderTroll renderer;

		public LayerTrollEyes(RenderTroll renderer) {
			this.renderer = renderer;
		}

		@Override
		public void doRenderLayer(EntityTroll troll, float f, float f1, float i, float f2, float f3, float f4, float f5) {
			if (!EntityGorgon.isStoneMob(troll)) {
				this.renderer.bindTexture(troll.getType().TEXTURE_EYES);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
				GlStateManager.disableLighting();
				GlStateManager.depthMask(!troll.isInvisible());
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
				GlStateManager.enableLighting();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.renderer.getMainModel().render(troll, f, f1, f2, f3, f4, f5);
				this.renderer.setLightmap(troll);
				GlStateManager.depthMask(true);
				GlStateManager.disableBlend();
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class LayerTrollStone implements LayerRenderer<EntityLivingBase> {

		private final RenderLivingBase<? extends EntityLivingBase> renderer;

		public LayerTrollStone(RenderLivingBase<? extends EntityLivingBase> renderer) {
			this.renderer = renderer;
		}
		
		private static final Map<UUID,StonedEntityCache> STONED_ENTITY_CACHE = new HashMap<>();
		private static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{
				new ResourceLocation("textures/blocks/destroy_stage_0.png"),
				new ResourceLocation("textures/blocks/destroy_stage_1.png"),
				new ResourceLocation("textures/blocks/destroy_stage_2.png"),
				new ResourceLocation("textures/blocks/destroy_stage_3.png"),
				new ResourceLocation("textures/blocks/destroy_stage_4.png"),
				new ResourceLocation("textures/blocks/destroy_stage_5.png"),
				new ResourceLocation("textures/blocks/destroy_stage_6.png"),
				new ResourceLocation("textures/blocks/destroy_stage_7.png"),
				new ResourceLocation("textures/blocks/destroy_stage_8.png"),
				new ResourceLocation("textures/blocks/destroy_stage_9.png")};
		
		@Override
		public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float f, float f1, float i, float f2, float f3, float f4, float f5) {
			if(entitylivingbaseIn instanceof EntityTroll) {
				EntityTroll troll = (EntityTroll)entitylivingbaseIn;
				if((((IEntityLivingBaseRenderContext)troll).iceAndFire$getStoned())) {
					UUID uuid = troll.getUniqueID();
					StonedEntityCache cache = STONED_ENTITY_CACHE.get(uuid);
					if(cache == null && !STONED_ENTITY_CACHE.containsKey(uuid)) {
						cache = new StonedEntityCache(troll.getType().TEXTURE_STONE, f, f1, f2, f3, f4, f5);
						STONED_ENTITY_CACHE.put(uuid, cache);
					}
					
					if(cache != null) {
						//Render stone texture
						GlStateManager.pushMatrix();
						GlStateManager.depthMask(true);
						GL11.glEnable(GL11.GL_CULL_FACE);
						this.renderer.bindTexture(cache.texture);
						this.renderer.getMainModel().render(troll, cache.limbSwing, cache.limbSwingAmount, cache.ageInTicks, cache.netHeadYaw, cache.headPitch, cache.scale);
						GL11.glDisable(GL11.GL_CULL_FACE);
						GlStateManager.popMatrix();
						
						//Render breaking texture
						int breakData = ((IEntityLivingBaseRenderContext)troll).iceAndFire$getStonedData();
						if(breakData > 0) {
							GlStateManager.pushMatrix();
							GlStateManager.enableBlend();
							GlStateManager.enableAlpha();
							GlStateManager.depthFunc(514);
							GlStateManager.depthMask(false);
							GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
							GlStateManager.matrixMode(5890);
							GlStateManager.loadIdentity();
							GlStateManager.scale((float)this.renderer.getMainModel().textureHeight / 16.0F, (float)this.renderer.getMainModel().textureWidth / 16.0F, 1.0F);
							GlStateManager.matrixMode(5888);
							this.renderer.bindTexture(DESTROY_STAGES[breakData - 1]);
							this.renderer.getMainModel().render(troll, cache.limbSwing, cache.limbSwingAmount, cache.ageInTicks, cache.netHeadYaw, cache.headPitch, cache.scale);
							GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
							GlStateManager.matrixMode(5890);
							GlStateManager.loadIdentity();
							GlStateManager.matrixMode(5888);
							GlStateManager.depthMask(true);
							GlStateManager.depthFunc(515);
							GlStateManager.disableBlend();
							GlStateManager.popMatrix();
						}
					}
				}
			}
		}
		
		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
		
		private static class StonedEntityCache {
			
			public final ResourceLocation texture;
			public final float limbSwing;
			public final float limbSwingAmount;
			public final float ageInTicks;
			public final float netHeadYaw;
			public final float headPitch;
			public final float scale;
			
			public StonedEntityCache(
					ResourceLocation texture,
					float limbSwing,
					float limbSwingAmount,
					float ageInTicks,
					float netHeadYaw,
					float headPitch,
					float scale) {
				this.texture = texture;
				this.limbSwing = limbSwing;
				this.limbSwingAmount = limbSwingAmount;
				this.ageInTicks = ageInTicks;
				this.netHeadYaw = netHeadYaw;
				this.headPitch = headPitch;
				this.scale = scale;
			}
		}
	}
}