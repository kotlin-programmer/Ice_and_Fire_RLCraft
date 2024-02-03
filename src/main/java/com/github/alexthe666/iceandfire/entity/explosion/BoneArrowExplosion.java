package com.github.alexthe666.iceandfire.entity.explosion;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.ChainLightningUtils;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.projectile.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.EntityMultipartPart;
import com.github.alexthe666.iceandfire.enums.EnumParticle;
import com.github.alexthe666.iceandfire.message.MessageParticleFX;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.*;

public class BoneArrowExplosion extends Explosion {
	private final EntityDragonArrow.Type type;
	private final World world;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final float explosionSize;
	private final List<BlockPos> affectedBlockPositions;
	private final Map<EntityPlayer, Vec3d> playerKnockbackMap;
	private final Vec3d position;

	public BoneArrowExplosion(World world, Entity entity, double x, double y, double z, float size, EntityDragonArrow.Type type) {
		super(world, entity, x, y, z, size, true, false);
		this.affectedBlockPositions = Lists.newArrayList();
		this.playerKnockbackMap = Maps.newHashMap();
		this.world = world;
		this.exploder = entity;
		this.explosionSize = size;
		this.explosionX = x;
		this.explosionY = y;
		this.explosionZ = z;
		this.position = new Vec3d(explosionX, explosionY, explosionZ);
		this.type = type;
	}

	/**
	 * Does the first part of the explosion
	 */
	@Override
	public void doExplosionA() {
		Set<BlockPos> set = Sets.newHashSet();
		for (int i = 0; i < this.explosionSize - 1; i++) {
			for (int j = 0; j < this.explosionSize - 1; j++) {
				for (int k = 0; k < this.explosionSize - 1; k++) {
					double distance = Math.sqrt(i * i + j * j + k * k);
					if (distance > this.explosionSize) {
						continue;
					}
					BlockPos pos = new BlockPos(this.explosionX + i, this.explosionY + j, this.explosionZ + k);
					if (isAir(pos)) {
						set.add(pos);
					}
					if (k != 0) {
						pos = new BlockPos(this.explosionX + i, this.explosionY + j, this.explosionZ - k);
						if (isAir(pos)) {
							set.add(pos);
						}
					}
					if (j != 0) {
						pos = new BlockPos(this.explosionX + i, this.explosionY - j, this.explosionZ + k);
						if (isAir(pos)) {
							set.add(pos);
						}
						if (k != 0) {
							pos = new BlockPos(this.explosionX + i, this.explosionY - j, this.explosionZ - k);
							if (isAir(pos)) {
								set.add(pos);
							}
						}
					}
					if (i != 0) {
						pos = new BlockPos(this.explosionX - i, this.explosionY + j, this.explosionZ + k);
						if (isAir(pos)) {
							set.add(pos);
						}
						if (k != 0) {
							pos = new BlockPos(this.explosionX - i, this.explosionY + j, this.explosionZ - k);
							if (isAir(pos)) {
								set.add(pos);
							}
						}
						if (j != 0) {
							pos = new BlockPos(this.explosionX - i, this.explosionY - j, this.explosionZ + k);
							if (isAir(pos)) {
								set.add(pos);
							}
							if (k != 0) {
								pos = new BlockPos(this.explosionX - i, this.explosionY - j, this.explosionZ - k);
								if (isAir(pos)) {
									set.add(pos);
								}
							}
						}
					}
				}
			}
		}

		this.affectedBlockPositions.addAll(set);

		int k1 = MathHelper.floor(this.explosionX - this.explosionSize - 1.0D);
		int l1 = MathHelper.floor(this.explosionX + this.explosionSize + 1.0D);
		int i2 = MathHelper.floor(this.explosionY - this.explosionSize - 1.0D);
		int i1 = MathHelper.floor(this.explosionY + this.explosionSize + 1.0D);
		int j2 = MathHelper.floor(this.explosionZ - this.explosionSize - 1.0D);
		int j1 = MathHelper.floor(this.explosionZ + this.explosionSize + 1.0D);
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));

		for (Entity entity : list) {
			if (!(entity instanceof EntityDragonArrow)) {
				if (!entity.isEntityEqual(exploder)) {
					double distance = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / this.explosionSize;

					if (distance <= 1.0D) {
						if (entity instanceof EntityMultipartPart) {
							entity = ((EntityMultipartPart) entity).getParent();
						}
						if (!(entity instanceof EntityLivingBase)) {
							continue;
						}
						if (!DragonUtils.isAlive((EntityLivingBase) entity)) {
							continue;
						}
						if (entity instanceof IEntityOwnable && ((IEntityOwnable) entity).getOwner() != null) {
							EntityLivingBase revengeTarget = ((EntityLivingBase) entity).getRevengeTarget();
							if (!exploder.equals(revengeTarget)) {
								continue;
							}
							if (entity instanceof EntityLiving) {
								EntityLivingBase attackTarget = ((EntityLiving) entity).getAttackTarget();
								if (!exploder.equals(attackTarget)) {
									continue;
								}
							}
						}
						applyEffect((EntityLivingBase) entity);
					}
				}
			}
		}
	}

	/**
	 * Does the second part of the explosion (sound, particles, drop spawn)
	 */
	@Override
	public void doExplosionB(boolean spawnParticles) {
		EnumParticle particle = type.getParticle();
		if (spawnParticles && particle != null) {
			List<MessageParticleFX.Particle> particles = new ArrayList<>();
			for (BlockPos blockpos : this.affectedBlockPositions) {
				double x = blockpos.getX();
				double y = blockpos.getY();
				double z = blockpos.getZ();
				double d3 = x - this.explosionX;
				double d4 = y - this.explosionY;
				double d5 = z - this.explosionZ;
				double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
				d3 = d3 / d6;
				d4 = d4 / d6;
				d5 = d5 / d6;
				double d7 = 0.3D / (d6 / this.explosionSize);
				d7 = d7 * (this.world.rand.nextFloat() * this.world.rand.nextFloat());
				d3 = d3 * d7;
				d4 = d4 * d7;
				d5 = d5 * d7;
				particles.add(MessageParticleFX.createParticle(blockpos, d3, d4, d5));
			}
			if (!particles.isEmpty()) {
				NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(this.exploder.dimension, this.explosionX, this.explosionY, this.explosionZ, 60);
				IceAndFire.NETWORK_WRAPPER.sendToAllTracking(new MessageParticleFX(particle, particles), targetPoint);
			}
		}
	}

	@Override
	public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap() {
		return this.playerKnockbackMap;
	}

	/**
	 * Returns either the entity that placed the explosive block, the entity
	 * that caused the explosion or null.
	 */
	@Override
	public EntityLivingBase getExplosivePlacedBy() {
		return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase) this.exploder : null));
	}

	@Override
	public void clearAffectedBlockPositions() {
		this.affectedBlockPositions.clear();
	}

	@Override
	public List<BlockPos> getAffectedBlockPositions() {
		return this.affectedBlockPositions;
	}

	@Override
	public Vec3d getPosition() {
		return this.position;
	}

	private void applyEffect(EntityLivingBase entity) {
		switch (type) {
			case FIRE:
				if (entity instanceof EntityIceDragon) {
					entity.attackEntityFrom(DamageSource.IN_FIRE, 13.5F);
				}
				entity.setFire(6);
				break;
			case ICE:
				if(entity instanceof EntityFireDragon) {
					entity.attackEntityFrom(DamageSource.DROWN, 13.5F);
				}
				if (!entity.world.isRemote) {
					IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(entity);
					if (capability != null) capability.setFrozen(200);
				}
				entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
				entity.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 100, 2));
				break;
			case LIGHTNING:
				if (entity instanceof EntityFireDragon || entity instanceof EntityIceDragon) {
					entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 6.75F);
				}
				ChainLightningUtils.attackEntityWithLightningDamage(this.exploder, entity);
		}
	}

	private boolean isAir(BlockPos pos) {
		IBlockState state = this.world.getBlockState(pos);
		return state.getMaterial() == Material.AIR;
	}
}