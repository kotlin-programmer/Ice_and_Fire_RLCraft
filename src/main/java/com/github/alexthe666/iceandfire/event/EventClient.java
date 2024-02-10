package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.client.render.entity.ICustomStoneLayer;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntity;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntityCrack;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.entity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.Map;

public class EventClient {

	public static void initializeStoneLayer() {
		for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet()) {
			Render<? extends Entity> render = entry.getValue();
			if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
				((RenderLivingBase) render).addLayer(new LayerStoneEntity((RenderLivingBase) render));
				((RenderLivingBase) render).addLayer(new LayerStoneEntityCrack((RenderLivingBase) render));
			}
		}

		RenderingRegistry registry = null;
		try {
			Field renderingRegInstanceField = RenderingRegistry.class.getDeclaredField("INSTANCE");
			renderingRegInstanceField.setAccessible(true);
			registry = (RenderingRegistry)renderingRegInstanceField.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (registry != null) {
			Map<Class<? extends Entity>, IRenderFactory<? extends Entity>> entityRenders = null;
			Map<Class<? extends Entity>, Render<? extends Entity>> entityRendersOld = null;
			try {
				Field renderingRegRendersField = RenderingRegistry.class.getDeclaredField("entityRenderers");
				Field renderingRegOldRendersField = RenderingRegistry.class.getDeclaredField("entityRenderersOld");
				renderingRegRendersField.setAccessible(true);
				renderingRegOldRendersField.setAccessible(true);
				entityRenders = (Map<Class<? extends Entity>, IRenderFactory<? extends Entity>>)renderingRegRendersField.get(registry);
				entityRendersOld = (Map<Class<? extends Entity>, Render<? extends Entity>>)renderingRegOldRendersField.get(registry);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (entityRenders != null) {
				for (Map.Entry<Class<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenders.entrySet()) {
					if (entry.getValue() != null) {
						try{
							Render<? extends Entity> render = entry.getValue().createRenderFor(Minecraft.getMinecraft().getRenderManager());
							if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
								LayerRenderer stoneLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer)render).getStoneLayer((RenderLivingBase) render) : new LayerStoneEntity((RenderLivingBase) render);
								LayerRenderer crackLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer)render).getCrackLayer((RenderLivingBase) render) : new LayerStoneEntityCrack((RenderLivingBase) render);
								((RenderLivingBase) render).addLayer(stoneLayer);
								((RenderLivingBase) render).addLayer(crackLayer);
							}
						}
						catch(NullPointerException exp){
							IceAndFire.logger.error("Ice and Fire: Could not apply stone render layer to " + entry.getKey().getSimpleName() + ", someone isn't registering their renderer properly... <.<");
						}
					}
				}
			}
			if (entityRendersOld != null) {
				for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : entityRendersOld.entrySet()) {
					Render<? extends Entity> render = entry.getValue();
					if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
						LayerRenderer stoneLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer)render).getStoneLayer((RenderLivingBase) render) : new LayerStoneEntity((RenderLivingBase) render);
						LayerRenderer crackLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer)render).getCrackLayer((RenderLivingBase) render) : new LayerStoneEntityCrack((RenderLivingBase) render);
						((RenderLivingBase) render).addLayer(stoneLayer);
						((RenderLivingBase) render).addLayer(crackLayer);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player.getRidingEntity() != null) {
			if(player.getRidingEntity() instanceof EntityDragonBase){
				int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
				float scale = ((EntityDragonBase) player.getRidingEntity()).getRenderSize() / 3;
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 1) {
					if (currentView == 1) {
						GL11.glTranslatef(scale * 0.5F, 0F, -scale * 3F);
					} else if (currentView == 2) {
						GL11.glTranslatef(0, 0F, -scale * 3F);
					} else if (currentView == 3) {
						GL11.glTranslatef(scale * 0.5F, 0F, -scale * 0.5F);
					}
				}
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
					if (currentView == 1) {
						GL11.glTranslatef(-scale  * 1.2F, 0F, 5);
					} else if(currentView == 2) {
						GL11.glTranslatef(scale  * 1.2F, 0F, 5);
					} else if(currentView == 3) {
						GL11.glTranslatef(0, 0F, scale * 3F);
					}
				}
			}

		}
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase livingBase = event.getEntityLiving();
		if (livingBase instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) livingBase;
			if (player.world.isRemote && ModKeys.dragon_change_view.isPressed()) {
				int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
				if(currentView + 1 > 3){
					currentView = 0;
				}else{
					currentView++;
				}
				IceAndFire.PROXY.setDragon3rdPersonView(currentView);
			}
		}
	}

	private static final ResourceLocation TEXTURE_0 = new ResourceLocation("textures/blocks/frosted_ice_0.png");
	private static final ResourceLocation TEXTURE_1 = new ResourceLocation("textures/blocks/frosted_ice_1.png");
	private static final ResourceLocation TEXTURE_2 = new ResourceLocation("textures/blocks/frosted_ice_2.png");
	private static final ResourceLocation TEXTURE_3 = new ResourceLocation("textures/blocks/frosted_ice_3.png");

	@SubscribeEvent
	public void onPreRenderLiving(RenderLivingEvent.Pre event){
		if (event.getEntity().getRidingEntity() != null && event.getEntity().getRidingEntity() instanceof EntityDragonBase) {
			if (ClientProxy.currentDragonRiders.contains(event.getEntity().getUniqueID()) || event.getEntity() == Minecraft.getMinecraft().player && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
				event.setCanceled(true);
			}
		}
	}
	@SubscribeEvent
	public void onPostRenderLiving(RenderLivingEvent.Post event) {
		IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(event.getEntity());
		if(capability != null && capability.isFrozen()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableNormalize();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			float sideExpand = 0.25F;
			AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(event.getEntity().getRenderBoundingBox().minX - event.getEntity().posX + event.getX() - sideExpand, event.getEntity().getRenderBoundingBox().minY - event.getEntity().posY + event.getY(), event.getEntity().getRenderBoundingBox().minZ - event.getEntity().posZ + event.getZ() - sideExpand, event.getEntity().getRenderBoundingBox().maxX - event.getEntity().posX + event.getX() + sideExpand, event.getEntity().getRenderBoundingBox().maxY - event.getEntity().posY + event.getY() + sideExpand, event.getEntity().getRenderBoundingBox().maxZ - event.getEntity().posZ + event.getZ() + sideExpand);
			event.getRenderer().bindTexture(getIceTexture(capability.getTime()));
			renderAABB(axisalignedbb1, 0, 0, 0);
			GlStateManager.disableBlend();
			GlStateManager.disableNormalize();
		}
	}

	private static ResourceLocation getIceTexture(int ticksFrozen) {
		if (ticksFrozen < 100) {
			if(ticksFrozen < 50){
				if(ticksFrozen < 20) {
					return TEXTURE_3;
				}
				return TEXTURE_2;
			}
			return TEXTURE_1;
		}
		return TEXTURE_0;
	}

	public static void renderAABB(AxisAlignedBB boundingBox, double x, double y, double z) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		double maxX = boundingBox.maxX * 0.625F;
		double minX = boundingBox.minX * 0.625F;
		double maxY = boundingBox.maxY * 0.625F;
		double minY = boundingBox.minY * 0.625F;
		double maxZ = boundingBox.maxZ * 0.625F;
		double minZ = boundingBox.minZ * 0.625F;
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(minX - maxX, maxY - minY).normal(0.0F, 0.0F, -1.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(maxX - minX, maxY - minY).normal(0.0F, 0.0F, -1.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(maxX - minX, minY - maxY).normal(0.0F, 0.0F, -1.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(minX - maxX, minY - maxY).normal(0.0F, 0.0F, -1.0F).endVertex();

		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(minX - maxX, minY - maxY).normal(0.0F, 0.0F, 1.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(maxX - minX, minY - maxY).normal(0.0F, 0.0F, 1.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(maxX - minX, maxY - minY).normal(0.0F, 0.0F, 1.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(minX - maxX, maxY - minY).normal(0.0F, 0.0F, 1.0F).endVertex();

		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(minX - maxX, minY - maxY).normal(0.0F, -1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(maxX - minX, minY - maxY).normal(0.0F, -1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(maxX - minX, maxZ - minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(minX - maxX, maxZ - minZ).normal(0.0F, -1.0F, 0.0F).endVertex();

		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(minX - maxX, minY - maxY).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(maxX - minX, minY - maxY).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(maxX - minX, maxZ - minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(minX - maxX, maxZ - minZ).normal(0.0F, 1.0F, 0.0F).endVertex();

		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(minX - maxX, minY - maxY).normal(-1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(minX - maxX, maxY - minY).normal(-1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(maxX - minX, maxY - minY).normal(-1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(maxX - minX, minY - maxY).normal(-1.0F, 0.0F, 0.0F).endVertex();

		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(minX - maxX, minY - maxY).normal(1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(minX - maxX, maxY - minY).normal(1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(maxX - minX, maxY - minY).normal(1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(maxX - minX, minY - maxY).normal(1.0F, 0.0F, 0.0F).endVertex();
		tessellator.draw();
	}
}