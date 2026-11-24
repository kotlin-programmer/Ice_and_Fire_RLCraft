package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageMultipartInteract;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityMultipartPart extends PartEntity implements IEntityMultiPart {

    public EntityMultipartPart(EntityLiving parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(
                    new MessageMultipartInteract(
                            this.parent.getEntityId(),
                            0,
                            false
                    )
            );
        }
        return this.parent.processInitialInteract(player, hand);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (world.isRemote && source.getTrueSource() instanceof EntityPlayer) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(
                    new MessageMultipartInteract(
                            this.parent.getEntityId(),
                            damage * damageMultiplier,
                            true
                    )
            );
        }
        return this.parent.attackEntityFrom(source, damage * this.damageMultiplier);
    }

    public EntityLivingBase getParent(){
        return this.parent;
    }

    private static final double REACH_SQ = 8 * 8;

    public static boolean isInReach(EntityPlayer player, EntityLivingBase parent) {
        Vec3d eyes = player.getPositionEyes(1.0F);
        if (distanceSq(parent.getEntityBoundingBox().grow(parent.width), eyes) <= REACH_SQ) {
            return true;
        }
        if (parent instanceof IMultipartEntity) {
            for (Entity part : ((IMultipartEntity) parent).getParts()) {
                if (part != null && distanceSq(part.getEntityBoundingBox(), eyes) <= REACH_SQ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static float maxClientDamage(EntityPlayer player, EntityLivingBase parent) {
        float attack = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        attack += EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), parent.getCreatureAttribute());
        float multiplier = 1.0F;
        if (parent instanceof IMultipartEntity) {
            for (Entity part : ((IMultipartEntity) parent).getParts()) {
                if (part instanceof EntityMultipartPart) {
                    multiplier = Math.max(multiplier, ((EntityMultipartPart) part).damageMultiplier);
                }
            }
        }
        return Math.max(1.0F, attack) * 1.5F * multiplier;
    }

    private static double distanceSq(AxisAlignedBB box, Vec3d point) {
        double dx = Math.max(Math.max(box.minX - point.x, 0), point.x - box.maxX);
        double dy = Math.max(Math.max(box.minY - point.y, 0), point.y - box.maxY);
        double dz = Math.max(Math.max(box.minZ - point.z, 0), point.z - box.maxZ);
        return dx * dx + dy * dy + dz * dz;
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

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
        return attackEntityFrom(source, damage);
    }
}