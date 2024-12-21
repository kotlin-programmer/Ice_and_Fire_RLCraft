package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageMultipartInteract;
import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;

public class EntityMultipartPart extends PartEntity {

    public EntityMultipartPart(EntityLiving parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if(world.isRemote) IceAndFire.NETWORK_WRAPPER.sendToServer(
                new MessageMultipartInteract(this.parent.getEntityId(), 0, false));
        return this.parent.processInitialInteract(player, hand);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if(world.isRemote && source.getTrueSource() instanceof EntityPlayer) IceAndFire.NETWORK_WRAPPER.sendToServer(
                new MessageMultipartInteract(this.parent.getEntityId(), damage * damageMultiplier, true));
        return this.parent.attackEntityFrom(source, damage * this.damageMultiplier);
    }

    public EntityLivingBase getParent(){
        return this.parent;
    }

    public void resize(float width, float height){
        this.setSize(width, height);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.parent == null || shouldNotExist()) {
            this.world.removeEntityDangerously(this);
        }
    }

    public boolean shouldNotExist(){
        return !this.parent.isEntityAlive();
    }

    public boolean isEntityEqual(Entity entityIn) {
        if (entityIn instanceof EntityMultipartPart) {
            return super.isEntityEqual(entityIn);
        }
        return parent.isEntityEqual(entityIn);
    }
}