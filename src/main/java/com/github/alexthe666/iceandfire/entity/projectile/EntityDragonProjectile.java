package com.github.alexthe666.iceandfire.entity.projectile;

import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityDragonProjectile extends EntityFireball implements IDragonProjectile {

    private static final DataParameter<Integer> SHOOTING_ENTITY = EntityDataManager.<Integer>createKey(EntityDragonProjectile.class, DataSerializers.VARINT);

    public EntityDragonProjectile(World worldIn) {
        super(worldIn);
    }

    public EntityDragonProjectile(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
    }

    public EntityDragonProjectile(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }

    public void setSizes(float width, float height) {
        this.setSize(width, height);
    }

    public void setShootingEntity(int entityId) {
        this.dataManager.set(SHOOTING_ENTITY, entityId);
    }

    protected Entity getShootingEntity() {
        if (this.shootingEntity != null) {
            return this.shootingEntity;
        }
        int entityId = this.dataManager.get(SHOOTING_ENTITY);
        if (entityId == 0) {
            return null;
        }
        return world.getEntityByID(entityId);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SHOOTING_ENTITY, 0);
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
