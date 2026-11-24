package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.item.ItemMyrmexStaff;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class MessageGetMyrmexHive extends AbstractMessage<MessageGetMyrmexHive> {

	public MyrmexHive hive;

	public MessageGetMyrmexHive(MyrmexHive hive) {
		this.hive = hive;
	}

	public MessageGetMyrmexHive() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		hive = new MyrmexHive();
		hive.readVillageDataFromNBT(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		if(hive != null) {
			hive.writeVillageDataToNBT(tag);
		}
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientReceived(Minecraft client, MessageGetMyrmexHive message, EntityPlayer player, MessageContext messageContext) {
		ClientProxy.setReferedClientHive(message.hive);
	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageGetMyrmexHive message, EntityPlayer player, MessageContext messageContext) {
		if (message.hive == null || message.hive.hiveUUID == null || !isHoldingStaffFor(player, message.hive.hiveUUID)) {
			return;
		}
		MyrmexHive serverHive = MyrmexWorldData.get(player.world).getHiveFromUUID(message.hive.hiveUUID);
		if (serverHive != null) {
			NBTTagCompound edited = new NBTTagCompound();
			message.hive.writeVillageDataToNBT(edited);
			NBTTagCompound current = new NBTTagCompound();
			serverHive.writeVillageDataToNBT(current);
			for (String key : new String[]{"HasOwner", "OwnerUUIDMost", "OwnerUUIDLeast", "HiveMembers", "PopSize", "Players"}) {
				edited.removeTag(key);
				if (current.hasKey(key)) {
					edited.setTag(key, current.getTag(key));
				}
			}
			serverHive.readVillageDataFromNBT(edited);
		}
	}

	private static boolean isHoldingStaffFor(EntityPlayer player, UUID hiveId) {
		for (EnumHand hand : EnumHand.values()) {
			ItemStack stack = player.getHeldItem(hand);
			if (stack.getItem() instanceof ItemMyrmexStaff && stack.hasTagCompound()
					&& hiveId.equals(stack.getTagCompound().getUniqueId("HiveUUID"))) {
				return true;
			}
		}
		return false;
	}
}