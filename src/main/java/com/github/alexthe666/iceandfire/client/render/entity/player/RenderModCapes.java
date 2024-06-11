package com.github.alexthe666.iceandfire.client.render.entity.player;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RenderModCapes {
	private static final ResourceLocation shivaxiCapeTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_shivaxi.png");
	private static final ResourceLocation shivaxiElytraTex = new ResourceLocation("iceandfire", "textures/models/misc/elytra_shivaxi.png");
	private static final ResourceLocation artsyDyCapeTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_artsydy.png");
	private static final ResourceLocation artsyDyElytraTex = new ResourceLocation("iceandfire", "textures/models/misc/elytra_artsydy.png");
	private static final ResourceLocation eagleCapeTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_eagle.png");
	private static final ResourceLocation eagleElytraTex = new ResourceLocation("iceandfire", "textures/models/misc/elytra_eagle.png");
	private static final ResourceLocation heavenCapeTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_heaven.png");
	private static final ResourceLocation fireCapeTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_fire.png");
	private static final ResourceLocation fireElytraTex = new ResourceLocation("iceandfire", "textures/models/misc/elytra_fire.png");
	private static final ResourceLocation iceCapeTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_ice.png");
	private static final ResourceLocation iceElytraTex = new ResourceLocation("iceandfire", "textures/models/misc/elytra_ice.png");
	private static final ResourceLocation lightningCapeTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_lightning.png");
	private static final ResourceLocation lightningElytraTex = new ResourceLocation("iceandfire", "textures/models/misc/elytra_lightning.png");

	private static final UUID[] firecapes = new UUID[] {
			User.Alexthe666,
			User.Zeklo
	};
	private static final UUID[] iceCapes = new UUID[] {
			User.Meldexun,
			User.Raptorfarian,
			User.Zyranna
	};
	private static final UUID[] lightningCapes = new UUID[] {
			User.Fonnymunkey,
			User.Kotlin_Dev,
			User.Rayquazafallout
	};

	private static Field playerInfoField;
	private static Field playerTexturesField;

	@SubscribeEvent
	public void playerRender(RenderPlayerEvent.Pre event) {
		if (event.getEntityPlayer() instanceof AbstractClientPlayer) {
			NetworkPlayerInfo info = getNetworkPlayerInfo(event.getEntityPlayer());
			if (info != null) {
				Map<MinecraftProfileTexture.Type, ResourceLocation> textureMap = getPlayerTexturesMap(info);
				if (textureMap != null) {
					UUID uniqueID = event.getEntityPlayer().getUniqueID();
					if (User.Shivaxi.equals(uniqueID)) {
						textureMap.put(MinecraftProfileTexture.Type.CAPE, shivaxiCapeTex);
						textureMap.put(MinecraftProfileTexture.Type.ELYTRA, shivaxiElytraTex);
					}
					if (User.ArtsyDy.equals(uniqueID)) {
						textureMap.put(MinecraftProfileTexture.Type.CAPE, artsyDyCapeTex);
						textureMap.put(MinecraftProfileTexture.Type.ELYTRA, artsyDyElytraTex);
					}
					if (User.NLBlackEagle.equals(uniqueID)) {
						textureMap.put(MinecraftProfileTexture.Type.CAPE, eagleCapeTex);
						textureMap.put(MinecraftProfileTexture.Type.ELYTRA, eagleElytraTex);
					}
					if (User.NLBlackHeavenNL.equals(uniqueID)) {
						textureMap.put(MinecraftProfileTexture.Type.CAPE, heavenCapeTex);
						textureMap.put(MinecraftProfileTexture.Type.ELYTRA, eagleElytraTex);
					}
					if (this.hasFireCape(uniqueID)) {
						textureMap.put(MinecraftProfileTexture.Type.CAPE, fireCapeTex);
						textureMap.put(MinecraftProfileTexture.Type.ELYTRA, fireElytraTex);
					}
					if (this.hasIceCape(uniqueID)) {
						textureMap.put(MinecraftProfileTexture.Type.CAPE, iceCapeTex);
						textureMap.put(MinecraftProfileTexture.Type.ELYTRA, iceElytraTex);
					}
					if (this.hasLightningCape(uniqueID)) {
						textureMap.put(MinecraftProfileTexture.Type.CAPE, lightningCapeTex);
						textureMap.put(MinecraftProfileTexture.Type.ELYTRA, lightningElytraTex);
					}
				}
			}
		}
	}

	private NetworkPlayerInfo getNetworkPlayerInfo(EntityPlayer player) {
		try {
			if (playerInfoField == null) {
				playerInfoField = ObfuscationReflectionHelper.findField(AbstractClientPlayer.class, "field_175157_a");
				playerInfoField.setAccessible(true);
			}
			return (NetworkPlayerInfo) playerInfoField.get(player);
		}
		catch (IllegalArgumentException | IllegalAccessException error) {
			error.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Map<MinecraftProfileTexture.Type, ResourceLocation> getPlayerTexturesMap(NetworkPlayerInfo info) {
		try {
			if (playerTexturesField == null) {
				playerTexturesField = ObfuscationReflectionHelper.findField(NetworkPlayerInfo.class, "field_187107_a");
				playerTexturesField.setAccessible(true);
			}
			return (Map<MinecraftProfileTexture.Type, ResourceLocation>) playerTexturesField.get(info);
		}
		catch (IllegalArgumentException | IllegalAccessException error) {
			error.printStackTrace();
		}
		return null;
	}

	private boolean hasFireCape(UUID uniqueID) {
		for (UUID uuid1 : firecapes) {
			if (uniqueID.equals(uuid1)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasIceCape(UUID uniqueID) {
		for (UUID uuid1 : iceCapes) {
			if (uniqueID.equals(uuid1)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasLightningCape(UUID uniqueID) {
		for (UUID uuid1 : lightningCapes) {
			if (uniqueID.equals(uuid1)) {
				return true;
			}
		}
		return false;
	}

	private static class User {
		private static final UUID Alexthe666 = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
		private static final UUID ArtsyDy = UUID.fromString("14bba455-affa-46d0-9cf0-806cc0f3d454");
		private static final UUID Fonnymunkey = UUID.fromString("648ab92b-e045-43c8-af1e-85a069d7bf42");
		private static final UUID Kotlin_Dev = UUID.fromString("2cda280f-2547-4f0c-88b3-05fa236c4bf4");
		private static final UUID Meldexun = UUID.fromString("fe29a2f2-aefc-4ad5-a2b9-86ee8bc41a66");
		private static final UUID NLBlackEagle = UUID.fromString("82da4f98-9835-4744-bcd5-62ba2d5e2df6");
		private static final UUID NLBlackHeavenNL = UUID.fromString("ba54bb01-4afa-4543-a877-9642e4c582c9");
		private static final UUID Raptorfarian = UUID.fromString("0ed918c8-d612-4360-b711-cd415671356f");
		private static final UUID Rayquazafallout = UUID.fromString("369a3e3f-371c-4ce6-ba44-48c3eb91ce8b");
		private static final UUID Shivaxi = UUID.fromString("cdfccefb-1a2e-4fb8-a3b5-041da27fde61");
		private static final UUID Zeklo = UUID.fromString("59efccaf-902d-45da-928a-5a549b9fd5e0");
		private static final UUID Zyranna = UUID.fromString("5d43896a-06a0-49fb-95c5-38485c63667f");
	}
}