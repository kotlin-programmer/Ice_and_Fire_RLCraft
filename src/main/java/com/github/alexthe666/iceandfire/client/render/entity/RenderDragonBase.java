package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderDragonBase extends RenderLiving<EntityDragonBase> {

	private final Map<String, ResourceLocation> LAYERED_TEXTURE_CACHE = Maps.newHashMap();

	public RenderDragonBase(RenderManager renderManager, ModelBase model) {
		super(renderManager, model, 0.8F);
		this.addLayer(new LayerDragonEyes(this));
		this.addLayer(new LayerDragonRider(this));
		this.addLayer(new LayerDragonArmor(this));
	}

	@Override
	public boolean shouldRender(EntityDragonBase dragon, ICamera camera, double camX, double camY, double camZ) {
		return true;
	}

	@Override
	protected void preRenderCallback(EntityDragonBase entity, float f) {
		this.shadowSize = entity.getRenderSize() / 3;
		GL11.glScalef(shadowSize, shadowSize, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
		String baseTexture = entity.getVariantName(entity.getVariant()) + " " + entity.getDragonStage() + entity.isModelDead() + entity.isSkeletal() + entity.isSleeping() + entity.isBlinking();
		ResourceLocation resourcelocation = LAYERED_TEXTURE_CACHE.get(baseTexture);
		if (resourcelocation == null) {
			resourcelocation = EnumDragonTextures.getTextureFromDragon(entity);
			LAYERED_TEXTURE_CACHE.put(baseTexture, resourcelocation);
		}
		return resourcelocation;
	}

	public static void clearCache(String str){
		LayerDragonArmor.LAYERED_ARMOR_CACHE.remove(str);
	}

	@SideOnly(Side.CLIENT)
	public static class LayerDragonEyes implements LayerRenderer<EntityDragonBase> {

		private final RenderLiving<EntityDragonBase> render;

		public LayerDragonEyes(RenderLiving<EntityDragonBase> renderIn) {
			this.render = renderIn;
		}

		@Override
		public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			if (dragon.shouldRenderEyes()) {
				this.render.bindTexture(EnumDragonTextures.getEyeTextureFromDragon(dragon));
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

				GlStateManager.depthMask(!dragon.isInvisible());

				int i = 61680;
				int j = i % 65536;
				int k = i / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
				this.render.getMainModel().render(dragon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
				Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
				i = dragon.getBrightnessForRender();
				j = i % 65536;
				k = i / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
				this.render.setLightmap(dragon);
				GlStateManager.disableBlend();
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return true;
		}
	}

	//TODO: rework this class along with dragon biting rework
	@SideOnly(Side.CLIENT)
	public static class LayerDragonRider implements LayerRenderer<EntityDragonBase> {

		private final RenderLiving<EntityDragonBase> render;

		public LayerDragonRider(RenderLiving<EntityDragonBase> renderIn) {
			this.render = renderIn;
		}

		@Override
		public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			GlStateManager.pushMatrix();
			if (!dragon.getPassengers().isEmpty()) {
				float dragonScale = dragon.getRenderSize() / 3;
				for (Entity passenger : dragon.getPassengers()) {
					boolean prey = dragon.getControllingPassenger() == null || !dragon.isControllingPassenger(passenger);
					ClientProxy.currentDragonRiders.remove(passenger.getUniqueID());
					float riderRot = passenger.prevRotationYaw + (passenger.rotationYaw - passenger.prevRotationYaw) * partialTicks;
					int animationTicks = 0;
					if (dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
						animationTicks = dragon.getAnimationTick();
					}
					if (animationTicks == 0 || animationTicks >= 15) {
						translateToBody();
					}
					if (prey) {
						if (animationTicks == 0 || animationTicks >= 15 || dragon.isFlying()) {
							translateToHead();
							Render<Entity> render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(passenger);
							ModelBase modelBase = null;
							if (render instanceof RenderLiving) {
								modelBase = ((RenderLiving<?>) render).getMainModel();
							}
							if ((passenger.height > passenger.width || modelBase instanceof ModelBiped) && !(modelBase instanceof ModelQuadruped) && !(modelBase instanceof ModelHorse)) {
								GlStateManager.translate(-0.15F * passenger.height, 0.1F * dragonScale - 0.1F * passenger.height, -0.1F * dragonScale - 0.1F * passenger.width);
								GlStateManager.rotate(90, 0, 0, 1);
								GlStateManager.rotate(45, 0, 1, 0);
							} else {
								boolean horse = modelBase instanceof ModelHorse;
								GlStateManager.translate((horse ? -0.08F : -0.15F) * passenger.width, 0.1F * dragonScale - 0.15F * passenger.width, -0.1F * dragonScale - 0.1F * passenger.width);
								GlStateManager.rotate(-90, 0, 1, 0);
							}
						} else {
							GlStateManager.translate(0, 0.555F * dragonScale, -0.5F * dragonScale);
						}
					} else{
						GlStateManager.translate(0, -0.01F * dragonScale, -0.035F * dragonScale);
					}
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180, 0, 0, 1);

					GlStateManager.rotate(riderRot + 180, 0, 1, 0);
					GlStateManager.scale(1 / dragonScale, 1 / dragonScale, 1 / dragonScale);
					GlStateManager.translate(0, -0.25F, 0);
					renderEntity(passenger, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, true);
					GlStateManager.popMatrix();
					ClientProxy.currentDragonRiders.add(passenger.getUniqueID());
				}
			}
			GlStateManager.popMatrix();
		}

		protected void translateToBody() {
			postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("BodyUpper"), 0.0625F);
			postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck1"), 0.0625F);
		}

		protected void translateToHead() {
			postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck2"), 0.0625F);
			postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck3"), 0.0625F);
			postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Head"), 0.0625F);
		}

		protected void postRender(AdvancedModelRenderer renderer, float scale) {
			if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
				if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.rotationPointZ != 0.0F) {
					GlStateManager.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);
				}
			} else {
				GlStateManager.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);

				if (renderer.rotateAngleZ != 0.0F) {
					GlStateManager.rotate(renderer.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
				}

				if (renderer.rotateAngleY != 0.0F) {
					GlStateManager.rotate(renderer.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
				}

				if (renderer.rotateAngleX != 0.0F) {
					GlStateManager.rotate(renderer.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
				}
			}
		}

		public void renderEntity(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_) {
			Render<Entity> render = null;
			RenderManager manager = Minecraft.getMinecraft().getRenderManager();
			try {
				render = manager.getEntityRenderObject(entityIn);

				if (render != null && manager.renderEngine != null) {
					try {
						render.doRender(entityIn, x, y, z, yaw, partialTicks);
					} catch (Throwable throwable1) {
						throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Rendering entity in world"));
					}
				}
			} catch (Throwable throwable3) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
				entityIn.addEntityCrashInfo(crashreportcategory);
				CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
				crashreportcategory1.addCrashSection("Assigned renderer", render);
				crashreportcategory1.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
				crashreportcategory1.addCrashSection("Rotation", yaw);
				crashreportcategory1.addCrashSection("Delta", partialTicks);
				throw new ReportedException(crashreport);
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class LayerDragonArmor implements LayerRenderer<EntityDragonBase> {

		private final RenderLiving<EntityDragonBase> render;
		private static final Map<String, ResourceLocation> LAYERED_ARMOR_CACHE = Maps.newHashMap();

		public LayerDragonArmor(RenderLiving<EntityDragonBase> renderIn) {
			this.render = renderIn;
		}

		@Override
		public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			int armorHead = dragon.getArmorInSlot(EntityEquipmentSlot.HEAD);
			int armorNeck = dragon.getArmorInSlot(EntityEquipmentSlot.CHEST);
			int armorLegs = dragon.getArmorInSlot(EntityEquipmentSlot.LEGS);
			int armorFeet = dragon.getArmorInSlot(EntityEquipmentSlot.FEET);
			if (armorHead != 0 || armorNeck != 0 || armorLegs != 0 || armorFeet != 0) {
				String armorTexture = dragon.dragonType.getName() + "|" + armorHead + "|" + armorNeck + "|" + armorLegs + "|" + armorFeet;
				ResourceLocation resourcelocation = LAYERED_ARMOR_CACHE.get(armorTexture);
				if(resourcelocation == null){
					resourcelocation = new ResourceLocation("iceandfire" + "dragonArmor_" + armorTexture);
					List<String> tex = new ArrayList<>();
					for (EntityEquipmentSlot slot : EntityDragonBase.ARMOR_SLOTS) {
						if (dragon.dragonType == EnumDragonType.ICE) {
							tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).ICETEXTURE.toString());
						} else if (dragon.dragonType == EnumDragonType.LIGHTNING) {
							tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).LIGHTNINGTEXTURE.toString());
						} else {
							tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).FIRETEXTURE.toString());
						}
					}
					ArrayLayeredTexture layeredBase = new ArrayLayeredTexture(tex);
					Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, layeredBase);
					LAYERED_ARMOR_CACHE.put(armorTexture, resourcelocation);
				}
				this.render.bindTexture(resourcelocation);
				this.render.getMainModel().render(dragon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}
}