package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.util.EntityMultipartPart;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageMultipartInteract extends AbstractMessage<MessageMultipartInteract> {

    public int creatureID;
    public float dmg;
    public boolean attack;

    public MessageMultipartInteract(int creatureID, float dmg, boolean attack) {
        this.creatureID = creatureID;
        this.dmg = dmg;
        this.attack = attack;
    }

    public MessageMultipartInteract() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.creatureID = buf.readInt();
        this.dmg = buf.readFloat();
        this.attack = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.creatureID);
        buf.writeFloat(this.dmg);
        buf.writeBoolean(this.attack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageMultipartInteract message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.creatureID);
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase mob = (EntityLivingBase)entity;
            if(message.attack) mob.attackEntityFrom(DamageSource.causeMobDamage(player), dmg);
            else mob.processInitialInteract(player, EnumHand.MAIN_HAND);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageMultipartInteract message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.creatureID);
        if (entity instanceof EntityLivingBase && EntityMultipartPart.isInReach(player, (EntityLivingBase) entity)) {
            EntityLivingBase mob = (EntityLivingBase)entity;
            if (message.attack) {
                if (message.dmg > 0 && !Float.isInfinite(message.dmg)) {
                    mob.attackEntityFrom(DamageSource.causeMobDamage(player), Math.min(message.dmg, EntityMultipartPart.maxClientDamage(player, mob)));
                }
            }
            else mob.processInitialInteract(player, EnumHand.MAIN_HAND);
        }
    }
}