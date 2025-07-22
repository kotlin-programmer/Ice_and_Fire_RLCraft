package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.client.model.ICustomStatueModel;
import com.github.alexthe666.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import com.github.alexthe666.iceandfire.client.texture.StonedTexture;
import com.github.alexthe666.iceandfire.mixin.vanilla.IRenderInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class LayerStoneEntity implements LayerRenderer<EntityLivingBase> {

	private final RenderLivingBase<? extends EntityLivingBase> renderer;

	public LayerStoneEntity(RenderLivingBase<? extends EntityLivingBase> renderer) {
		this.renderer = renderer;
	}
	
	//TODO unload textures and clear cache over time?
	private static final Map<ResourceLocation, ResourceLocation> DESATURATED_TEXTURE_CACHE = new HashMap<>();
	private static final Map<UUID, StonedEntityCache> STONED_ENTITY_CACHE = new HashMap<>();
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
	private static final ResourceLocation VANILLA_STONE = new ResourceLocation("textures/blocks/stone.png");
	
	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(entity instanceof EntityLiving) {
			if(((IEntityLivingBaseRenderContext)entity).iceAndFire$getStoned()) {
				//Get entity instance specific rendering from cache
				UUID uuid = entity.getUniqueID();
				StonedEntityCache cache = STONED_ENTITY_CACHE.get(uuid);
				if(cache == null) {
					ResourceLocation desaturatedTexture = null;
					if(IceAndFireConfig.CLIENT_SETTINGS.advancedStonedEntityRender) {
						//If not already cached, get texture at this moment
						ResourceLocation entityTexture = ((IRenderInvoker)this.renderer).invokeGetEntityTexture(entity);
						if(entityTexture != null) {
							//Check if texture has already been desaturated (Likely for non-animated entity textures)
							desaturatedTexture = DESATURATED_TEXTURE_CACHE.get(entityTexture);
							if(desaturatedTexture == null) {
								//Create and load desaturated texture
								desaturatedTexture = new ResourceLocation("iceandfire:stonecache/" + entityTexture.getNamespace() + "/" + entityTexture.getPath());
								Minecraft.getMinecraft().getTextureManager().loadTexture(desaturatedTexture, new StonedTexture(entityTexture));
								DESATURATED_TEXTURE_CACHE.put(entityTexture, desaturatedTexture);
							}
						}
					}
					//If custom render is disabled, just provide a null texture
					cache = new StonedEntityCache(desaturatedTexture, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
					//Also put null cache into map to avoid reprocessing failed textures
					STONED_ENTITY_CACHE.put(uuid, cache);
				}
				
				if(cache.desaturatedTexture == null) {
					GlStateManager.pushMatrix();
					this.renderer.bindTexture(VANILLA_STONE);
					GlStateManager.matrixMode(5890);
					GlStateManager.loadIdentity();
					GlStateManager.scale((float)this.renderer.getMainModel().textureHeight / 16.0F, (float)this.renderer.getMainModel().textureWidth / 16.0F, 1.0F);
					GlStateManager.matrixMode(5888);
					if(this.renderer.getMainModel() instanceof ICustomStatueModel) {
						((ICustomStatueModel)this.renderer.getMainModel()).renderStatue();
					}
					else {
						this.renderer.getMainModel().render(entity, cache.limbSwing, cache.limbSwingAmount, cache.ageInTicks, cache.netHeadYaw, cache.headPitch, cache.scale);
					}
					GlStateManager.matrixMode(5890);
					GlStateManager.loadIdentity();
					GlStateManager.matrixMode(5888);
					GlStateManager.popMatrix();
				}
				else {
					//Render stone texture
					GlStateManager.pushMatrix();
					this.renderer.bindTexture(cache.desaturatedTexture);
					GlStateManager.depthMask(true);
					if(this.renderer.getMainModel() instanceof ICustomStatueModel) {
						((ICustomStatueModel)this.renderer.getMainModel()).renderStatue();
					}
					else {
						this.renderer.getMainModel().render(entity, cache.limbSwing, cache.limbSwingAmount, cache.ageInTicks, cache.netHeadYaw, cache.headPitch, cache.scale);
					}
					GlStateManager.popMatrix();
				}
				
				//Render breaking texture
				int breakData = ((IEntityLivingBaseRenderContext)entity).iceAndFire$getStonedData();
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
					if(this.renderer.getMainModel() instanceof ICustomStatueModel) {
						((ICustomStatueModel)this.renderer.getMainModel()).renderStatue();
					}
					else {
						this.renderer.getMainModel().render(entity, cache.limbSwing, cache.limbSwingAmount, cache.ageInTicks, cache.netHeadYaw, cache.headPitch, cache.scale);
					}
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
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
	private static class StonedEntityCache {
		
		public final ResourceLocation desaturatedTexture;
		public final float limbSwing;
		public final float limbSwingAmount;
		public final float ageInTicks;
		public final float netHeadYaw;
		public final float headPitch;
		public final float scale;
		
		public StonedEntityCache(
				ResourceLocation desaturatedTexture,
				float limbSwing,
				float limbSwingAmount,
				float ageInTicks,
				float netHeadYaw,
				float headPitch,
				float scale) {
			this.desaturatedTexture = desaturatedTexture;
			this.limbSwing = limbSwing;
			this.limbSwingAmount = limbSwingAmount;
			this.ageInTicks = ageInTicks;
			this.netHeadYaw = netHeadYaw;
			this.headPitch = headPitch;
			this.scale = scale;
		}
	}
}