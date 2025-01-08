package com.github.alexthe666.iceandfire.entity.projectile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.explosion.FireChargeExplosion;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import com.github.alexthe666.iceandfire.entity.explosion.IceExplosion;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.enums.EnumParticle;
import com.github.alexthe666.iceandfire.util.ParticleHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDragonIceCharge extends EntityFireball implements IDragonProjectile {

	public int ticksInAir;

	public EntityDragonIceCharge(World worldIn) {
		super(worldIn);

	}

	public EntityDragonIceCharge(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
		double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d0 * 0.07D;
		this.accelerationY = accelY / d0 * 0.07D;
		this.accelerationZ = accelZ / d0 * 0.07D;
	}

	public EntityDragonIceCharge(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d0 * 0.07D;
		this.accelerationY = accelY / d0 * 0.07D;
		this.accelerationZ = accelZ / d0 * 0.07D;
	}

	public void setSizes(float width, float height) {
		this.setSize(width, height);
	}

	protected boolean isFireballFiery() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	public void onUpdate() {
		if(this.world.isRemote) {
			for (int i = 0; i < 10; ++i) {
				IceAndFire.PROXY.spawnParticle(EnumParticle.SNOWFLAKE, world, this.posX + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posY + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posZ + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), 0.0D, 0.0D, 0.0D);
			}
		}
		if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this))) {
			this.onEntityUpdate();

			if (this.isFireballFiery()) {
				this.setFire(1);
			}

			++this.ticksInAir;
			RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, false, this.ticksInAir >= 25, this.shootingEntity);

			if (raytraceresult != null) {
				this.onImpact(raytraceresult);
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			ProjectileHelper.rotateTowardsMovement(this, 0.2F);
			float f = this.getMotionFactor();

			if (this.isInWater()) {
				if(this.world.isRemote) {
					for (int i = 0; i < 4; ++i) {
						ParticleHelper.spawnParticle(this.world, EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
					}
				}
				f = 0.8F;
			}

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= f;
			this.motionY *= f;
			this.motionZ *= f;
			if(this.world.isRemote) ParticleHelper.spawnParticle(this.world, this.getParticleType(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
			this.setPosition(this.posX, this.posY, this.posZ);
		} else {
			this.setDead();
		}
	}

	@Override
	protected void onImpact(RayTraceResult movingObject) {
		boolean flag = this.world.getGameRules().getBoolean("mobGriefing");

		if (!this.world.isRemote) {
			if (movingObject.entityHit instanceof IDragonProjectile) {
				return;
			}
			if (DragonUtils.isOwner(movingObject.entityHit, shootingEntity) || DragonUtils.hasSameOwner(movingObject.entityHit, shootingEntity)) {
				this.setDead();
				return;
			}
			if (movingObject.entityHit == null || !(movingObject.entityHit instanceof IDragonProjectile) && movingObject.entityHit != shootingEntity) {
				if (this.shootingEntity != null) {
					int explodeSize = 2;
					if(this.shootingEntity instanceof EntityDragonBase){
						explodeSize = 2 + ((EntityDragonBase) this.shootingEntity).getDragonStage();
					}
					IceExplosion explosion = new IceExplosion(world, shootingEntity, this.posX, this.posY, this.posZ, explodeSize, flag);
					explosion.doExplosionA();
					explosion.doExplosionB(true);
					FireChargeExplosion explosion2 = new FireChargeExplosion(world, shootingEntity, this.posX, this.posY, this.posZ, 2 + ((EntityDragonBase) this.shootingEntity).getDragonStage(), flag);
					explosion2.doExplosionA();
					explosion2.doExplosionB(true);
				}
				this.setDead();
				return;
			}
			if (!(movingObject.entityHit instanceof IDragonProjectile) && !movingObject.entityHit.isEntityEqual(shootingEntity)) {
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase && !movingObject.entityHit.isEntityEqual(shootingEntity)) {
					movingObject.entityHit.attackEntityFrom(IceAndFire.dragonIce, 10.0F);
					if (movingObject.entityHit instanceof EntityLivingBase) {
						IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLivingBase)movingObject.entityHit);
						if (capability != null) {
							capability.setFrozen(200);
						}
					}
					if (movingObject.entityHit instanceof EntityLivingBase && ((EntityLivingBase) movingObject.entityHit).getHealth() == 0) {
						((EntityDragonBase) this.shootingEntity).attackDecision = true;
					}
				}
				this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
				IceExplosion explosion = new IceExplosion(world, null, this.posX, this.posY, this.posZ, 2, flag);
				if (shootingEntity != null) {
					explosion = new IceExplosion(world, shootingEntity, this.posX, this.posY, this.posZ, 2, flag);
				}
				explosion.doExplosionA();
				explosion.doExplosionB(true);
				this.world.createExplosion(this, this.posX, this.posY, this.posZ, 4, true);
			}
		}
		this.setDead();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	@Override
	public float getCollisionBorderSize() {
		return 0F;
	}
}
