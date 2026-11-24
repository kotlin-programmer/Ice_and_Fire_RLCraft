package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.*;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDragonControl extends AbstractMessage<MessageDragonControl> {

	public int dragonId;
	public byte controlState;
	private double posX;
	private double posY;
	private double posZ;

	public MessageDragonControl(int dragonId, byte controlState, double posX, double posY, double posZ) {
		this.dragonId = dragonId;
		this.controlState = controlState;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public MessageDragonControl() {
	}

	private static final double MAX_STEP_SQ = 10 * 10;

	private static void setRiddenPosition(Entity mount, MessageDragonControl message) {
		if (Double.isFinite(message.posX) && Double.isFinite(message.posY) && Double.isFinite(message.posZ)
				&& mount.getDistanceSq(message.posX, message.posY, message.posZ) <= MAX_STEP_SQ) {
			mount.setPosition(message.posX, message.posY, message.posZ);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dragonId = buf.readInt();
		controlState = buf.readByte();
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dragonId);
		buf.writeByte(controlState);
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientReceived(Minecraft client, MessageDragonControl message, EntityPlayer player, MessageContext messageContext) {}

	@Override
	public void onServerReceived(MinecraftServer server, MessageDragonControl message, EntityPlayer player, MessageContext messageContext) {
		Entity entity = player.world.getEntityByID(message.dragonId);
		if (!(entity instanceof EntityTameable) || !((EntityTameable) entity).isOwner(player)) {
			return;
		}
		if (entity instanceof EntityDragonBase) {
			((EntityDragonBase) entity).setControlState(message.controlState);
		} else if (entity instanceof EntityHippogryph) {
			((EntityHippogryph) entity).setControlState(message.controlState);
		} else if (entity instanceof EntityHippocampus) {
			((EntityHippocampus) entity).setControlState(message.controlState);
		} else if (entity instanceof EntityDeathWorm) {
			((EntityDeathWorm) entity).setControlState(message.controlState);
		} else if (entity instanceof EntityAmphithere) {
			((EntityAmphithere) entity).setControlState(message.controlState);
		} else {
			return;
		}
		setRiddenPosition(entity, message);
	}
}