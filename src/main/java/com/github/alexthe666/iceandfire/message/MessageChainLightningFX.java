package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.particle.lightning.ParticleLightningVector;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class MessageChainLightningFX extends AbstractMessage<MessageChainLightningFX> {

	private List<Integer> entityIds;

	public MessageChainLightningFX(List<Integer> entityIds) {
		this.entityIds = entityIds;
	}

	public MessageChainLightningFX() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int count = buf.readInt();

		entityIds = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			int entityId = buf.readInt();
			entityIds.add(entityId);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (entityIds == null) {
			buf.writeInt(0);
			return;
		}
		buf.writeInt(entityIds.size());
		for (int entityId : entityIds) {
			buf.writeInt(entityId);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientReceived(Minecraft client, MessageChainLightningFX message, EntityPlayer player, MessageContext messageContext) {
		if (entityIds == null || entityIds.size() < 2) {
			return;
		}
		Entity entity = player.world.getEntityByID(entityIds.get(0));
		ParticleLightningVector sourceVec = null;
		ParticleLightningVector targetVec = null;
		if (entity != null) {
			sourceVec = ParticleLightningVector.fromEntityCenter(entity);
		}
		for (int i = 1; i < entityIds.size(); i++) {
			entity = player.world.getEntityByID(entityIds.get(i));
			if (entity != null) {
				targetVec = ParticleLightningVector.fromEntityCenter(entity);
			}
			if (sourceVec != null && targetVec != null) {
				IceAndFire.PROXY.spawnLightningEffect(player.world, sourceVec, targetVec, false);
			}
			if (targetVec != null) {
				sourceVec = targetVec;
			}
		}
	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageChainLightningFX message, EntityPlayer player, MessageContext messageContext) {}
}