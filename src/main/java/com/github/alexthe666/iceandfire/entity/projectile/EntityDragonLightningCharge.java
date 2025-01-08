package com.github.alexthe666.iceandfire.entity.projectile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.explosion.FireChargeExplosion;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import com.github.alexthe666.iceandfire.entity.explosion.LightningExplosion;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.enums.EnumParticle;
import com.github.alexthe666.iceandfire.integration.LycanitesCompat;
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

public class EntityDragonLightningCharge extends EntityFireball implements IDragonProjectile {

	public int ticksInAir;

	public EntityDragonLightningCharge(World worldIn) {
		super(worldIn);
	}

	public EntityDragonLightningCharge(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
	}

	public EntityDragonLightningCharge(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.setSize(0.5F, 0.5F);
		double d0 = (double) MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d0 * (0.1D * (shooter.isFlying() ? 4 * shooter.getDragonStage() : 1));
		this.accelerationY = accelY / d0 * (0.1D * (shooter.isFlying() ? 4 * shooter.getDragonStage() : 1));
		this.accelerationZ = accelZ / d0 * (0.1D * (shooter.isFlying() ? 4 * shooter.getDragonStage() : 1));
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

	@Override
	public void onUpdate() {
		if(this.world.isRemote) {
			for (int i = 0; i < 10; ++i) {
				IceAndFire.PROXY.spawnParticle(EnumParticle.SPARK, world, this.posX + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posY + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posZ + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), 0.0D, 0.0D, 0.0D);
			}
		}
		if (this.isInWater()) {
			setDead();
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
			if (movingObject.entityHit == null || !(movingObject.entityHit instanceof IDragonProjectile) && this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase && movingObject.entityHit != shootingEntity) {
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase) {
					LightningExplosion explosion = new LightningExplosion(world, shootingEntity, this.posX, this.posY, this.posZ, ((EntityDragonBase) this.shootingEntity).getDragonStage() * 2.5F, flag);
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
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase) {
					movingObject.entityHit.attackEntityFrom(IceAndFire.dragonLightning, 10.0F);
					if (movingObject.entityHit instanceof EntityLivingBase && ((EntityLivingBase) movingObject.entityHit).getHealth() == 0) {
						((EntityDragonBase) this.shootingEntity).attackDecision = true;
					}
					if(movingObject.entityHit instanceof EntityLivingBase) {
						if (IceAndFireConfig.DRAGON_SETTINGS.lightningDragonKnockback) {
							double xRatio = this.shootingEntity.posX - movingObject.entityHit.posX;
							double zRatio = this.shootingEntity.posZ - movingObject.entityHit.posZ;
							((EntityLivingBase) movingObject.entityHit).knockBack(this.shootingEntity, 0.9F, xRatio, zRatio);
						}
						if (IceAndFireConfig.DRAGON_SETTINGS.lightningDragonParalysis) {
							LycanitesCompat.applyParalysis(movingObject.entityHit, IceAndFireConfig.DRAGON_SETTINGS.lightningDragonParalysisTicks);
						}
					}
				}
				this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
				LightningExplosion explosion = new LightningExplosion(world, null, this.posX, this.posY, this.posZ, 2, flag);
				if (shootingEntity != null) {
					explosion = new LightningExplosion(world, shootingEntity, this.posX, this.posY, this.posZ, 2, flag);
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