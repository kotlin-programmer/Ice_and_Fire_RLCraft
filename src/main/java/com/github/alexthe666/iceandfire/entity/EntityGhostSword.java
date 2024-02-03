package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGhostSword extends EntityArrow
{
    public static final DataParameter<Integer> DISPOSE_TIME = EntityDataManager.createKey(EntityGhostSword.class, DataSerializers.VARINT);

    public Entity shooter;
    int maxDisposeTime = 15;

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
        if(key == DISPOSE_TIME)
        {
            maxDisposeTime = Math.min(dataManager.get(DISPOSE_TIME) - ticksExisted, 15);
        }
    }

    public float getAlpha(float partialTime)
    {
        if(maxDisposeTime == 0) return 0F;
        int time = dataManager.get(DISPOSE_TIME);
        return MathHelper.clamp(time - ticksExisted, 0, maxDisposeTime) / (float) maxDisposeTime;
    }

    public EntityGhostSword(World w)
    {
        super(w);
        this.setDamage(9F);
    }

    public EntityGhostSword(World worldIn, EntityLivingBase shooter, double dmg) {
        super(worldIn, shooter);
        this.setDamage(dmg);
        this.shooter = shooter;

        this.motionX *= -0.01F;
        this.motionY *= -0.1F;
        this.motionZ *= -0.01F;
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = posX - toX;
        double d1 = posY - toY;
        double d2 = posZ - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void onUpdate()
    {
        super.onUpdate();
        noClip = true;

        float sqrt = MathHelper.sqrt((float) (this.motionX * this.motionX + this.motionZ * this.motionZ));
        if ((sqrt < 0.1F) && this.ticksExisted > 200) {
            this.setDead();
        }
        double d0 = 0;
        double d1 = 0.0D;
        double d2 = 0.01D;
        double x = this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width;
        double y = this.posY + this.rand.nextFloat() * this.height - this.height;
        double z = this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width;
        float f = (this.width + this.height + this.width) * 0.333F + 0.5F;
        if (particleDistSq(x, y, z) < f * f) {
            this.world.spawnParticle(EnumParticleTypes.END_ROD, x, y + 0.5D, z, d0, d1, d2);
        }

        if(this.ticksExisted >= 200) //<- loop exit for primal
            this.setDead();
    }

    public boolean hasNoGravity()
    {
        return true;
    }

    @Override
    protected void onHit(RayTraceResult object) {
        if (this.isDead)
            return;

        if (world.isRemote)
            return;

        if (!this.world.isRemote && object.typeOfHit == RayTraceResult.Type.BLOCK) {
            return;
        }

        if (object.typeOfHit == RayTraceResult.Type.ENTITY) {
            Entity e = object.entityHit;
            if (e == shooter)
                return;

            if (e instanceof EntityLivingBase && e != this.shooter && !(e instanceof EntityGhostSword)) {
                EntityLivingBase elb = (EntityLivingBase) e;

                elb.attackEntityFrom(DamageSource.causeArrowDamage(this, shooter), 5);

                this.setDead();
            }
        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DISPOSE_TIME, 120);
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }
}