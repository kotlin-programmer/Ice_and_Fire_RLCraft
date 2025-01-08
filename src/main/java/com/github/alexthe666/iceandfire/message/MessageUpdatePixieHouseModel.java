package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageUpdatePixieHouseModel extends AbstractMessage<MessageUpdatePixieHouseModel> {

	public long blockPos;
	public int houseType;

	public MessageUpdatePixieHouseModel(long blockPos, int houseType) {
		this.blockPos = blockPos;
		this.houseType = houseType;

	}

	public MessageUpdatePixieHouseModel() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		blockPos = buf.readLong();
		houseType = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(blockPos);
		buf.writeInt(houseType);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientReceived(Minecraft client, MessageUpdatePixieHouseModel message, EntityPlayer player, MessageContext messageContext) {
		BlockPos pos = BlockPos.fromLong(message.blockPos);
		TileEntity te = client.world.getTileEntity(pos);
		if (te != null) {
			if (te instanceof TileEntityPixieHouse) {
				TileEntityPixieHouse house = (TileEntityPixieHouse) te;
				house.houseType = message.houseType;
			} else if (te instanceof TileEntityJar) {
				TileEntityJar jar = (TileEntityJar) te;
				jar.pixieType = message.houseType;
			}
		}
	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageUpdatePixieHouseModel message, EntityPlayer player, MessageContext messageContext) {

	}
}