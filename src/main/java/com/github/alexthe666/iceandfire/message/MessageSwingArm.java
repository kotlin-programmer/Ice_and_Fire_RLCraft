package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.event.EventLiving;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSwingArm extends AbstractMessage<MessageSwingArm> {

    public MessageSwingArm() {

    }

    @Override
    public void onClientReceived(Minecraft minecraft, MessageSwingArm messageSwingArm, EntityPlayer entityPlayer, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, MessageSwingArm messageSwingArm, EntityPlayer entityPlayer, MessageContext messageContext) {
        EventLiving.onLeftClick(entityPlayer, entityPlayer.getHeldItem(EnumHand.MAIN_HAND));
    }


    public void fromBytes(ByteBuf buf) {
    }

    public void toBytes(ByteBuf buf) {
    }

}