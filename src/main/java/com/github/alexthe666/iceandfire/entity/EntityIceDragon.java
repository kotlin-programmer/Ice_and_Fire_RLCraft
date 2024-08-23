package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.projectile.EntityDragonIce;
import com.github.alexthe666.iceandfire.entity.projectile.EntityDragonIceCharge;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityIceDragon extends EntityDragonBase {

	private static final DataParameter<Boolean> SWIMMING = EntityDataManager.<Boolean>createKey(EntityIceDragon.class, DataSerializers.BOOLEAN);
	public static Animation ANIMATION_FIRECHARGE;
	public static final float[] growth_stage_1 = new float[]{1F, 3F};
	public static final float[] growth_stage_2 = new float[]{3F, 7F};
	public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
	public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
	public static final float[] growth_stage_5 = new float[]{20F, 30F};
	public boolean isSwimming;
	public float swimProgress;
	public int ticksSwimming;
	public int swimCycle;
	public BlockPos waterTarget;
	public static final ResourceLocation FEMALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_female"));
	public static final ResourceLocation MALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_male"));
	public static final ResourceLocation SKELETON_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_skeleton"));

	public EntityIceDragon(World worldIn) {
		super(worldIn, EnumDragonType.ICE, 1, 1 + IceAndFireConfig.DRAGON_SETTINGS.dragonAttackDamage, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth * 0.04, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth, 0.15F, 0.4F);
		this.setSize(0.78F, 1.2F);
		this.ignoreFrustumCheck = true;
		ANIMATION_SPEAK = Animation.create(20);
		ANIMATION_BITE = Animation.create(35);
		ANIMATION_SHAKEPREY = Animation.create(65);
		ANIMATION_TAILWHACK = Animation.create(40);
		ANIMATION_FIRECHARGE = Animation.create(25);
		ANIMATION_WINGBLAST = Animation.create(50);
		ANIMATION_ROAR = Animation.create(40);
		this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
		this.stepHeight = 1;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, this.aiSit = new EntityAISit(this));
		this.tasks.addTask(2, new DragonAIMate(this, 1.0D));
		this.tasks.addTask(3, new DragonAIAttackMelee(this, 1.5D, false));
		this.tasks.addTask(4, new AquaticAITempt(this, 1.0D, ModItems.frost_stew, false));
		this.tasks.addTask(5, new DragonAIAirTarget(this));
		this.tasks.addTask(5, new DragonAIWaterTarget(this));
		this.tasks.addTask(6, new DragonAIWander(this, 1.0D));
		this.tasks.addTask(7, new DragonAIWatchClosest(this, EntityLivingBase.class, 6.0F));
		this.tasks.addTask(7, new DragonAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(4, new DragonAITarget<>(this, EntityLivingBase.class, true, new Predicate<Entity>() {
			@Override
			public boolean apply(@Nullable Entity entity) {
				return entity instanceof EntityLivingBase && DragonUtils.isAlive((EntityLivingBase) entity) && !EntityIceDragon.this.isControllingPassenger(entity);
			}
		}));
		this.targetTasks.addTask(5, new DragonAITargetItems<>(this, false));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(SWIMMING, Boolean.FALSE);
	}

	public String getVariantName(int variant) {
		switch (variant) {
			default:
				return "blue_";
			case 1:
				return "white_";
			case 2:
				return "sapphire_";
			case 3:
				return "silver_";
		}
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public Item getVariantScale(int variant) {
		switch (variant) {
			default:
				return ModItems.dragonscales_blue;
			case 1:
				return ModItems.dragonscales_white;
			case 2:
				return ModItems.dragonscales_sapphire;
			case 3:
				return ModItems.dragonscales_silver;
		}
	}

	public Item getVariantEgg(int variant) {
		switch (variant) {
			default:
				return ModItems.dragonegg_blue;
			case 1:
				return ModItems.dragonegg_white;
			case 2:
				return ModItems.dragonegg_sapphire;
			case 3:
				return ModItems.dragonegg_silver;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("Swimming", this.isSwimming());
		compound.setInteger("SwimmingTicks", this.ticksSwimming);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setSwimming(compound.getBoolean("Swimming"));
		this.ticksSwimming = compound.getInteger("SwimmingTicks");
	}

	public boolean canBeSteered() {
		return true;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (this.getAnimation() == ANIMATION_WINGBLAST) {
			return false;
		}
		switch (new Random().nextInt(4)) {
			case 0:
				if (this.getAnimation() != ANIMATION_BITE) {
					this.setAnimation(ANIMATION_BITE);
					return false;
				} else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
					boolean success = this.doBiteAttack(entityIn);
					this.attackDecision = this.getRNG().nextBoolean();
					return success;
				}
				break;
			case 1:
				if (new Random().nextInt(2) == 0 && isDirectPathBetweenPoints(this, this.getPositionVector(), entityIn.getPositionVector()) && entityIn.width < this.width * 0.5F && this.getControllingPassenger() == null && this.getDragonStage() > 1 && !(entityIn instanceof EntityDragonBase) && !DragonUtils.isAnimaniaMob(entityIn)) {
					if (this.getAnimation() != ANIMATION_SHAKEPREY) {
						this.setAnimation(ANIMATION_SHAKEPREY);
                        entityIn.dismountRidingEntity();
						entityIn.startRiding(this);
						this.attackDecision = this.getRNG().nextBoolean();
						return true;
					}
				} else {
					if (this.getAnimation() != ANIMATION_BITE) {
						this.setAnimation(ANIMATION_BITE);
						return false;
					} else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
						boolean success = this.doBiteAttack(entityIn);
						this.attackDecision = this.getRNG().nextBoolean();
						return success;
					}
				}
				break;
			case 2:
				if (this.getAnimation() != ANIMATION_TAILWHACK) {
					this.setAnimation(ANIMATION_TAILWHACK);
					return false;
				} else if (this.getAnimationTick() > 20 && this.getAnimationTick() < 30) {
					boolean flag2 = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
					if (entityIn instanceof EntityLivingBase) {
						((EntityLivingBase)entityIn).knockBack(entityIn, this.getDragonStage() * 0.6F, 1, 1);
					}
					this.attackDecision = this.getRNG().nextBoolean();
					return flag2;
				}
				break;
			case 3:
				if (this.onGround && !this.isHovering() && !this.isFlying()) {
					if (this.getAnimation() != ANIMATION_WINGBLAST) {
						this.setAnimation(ANIMATION_WINGBLAST);
						return true;
					}
				} else {
					if (this.getAnimation() != ANIMATION_BITE) {
						this.setAnimation(ANIMATION_BITE);
						return false;
					} else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
						boolean success = this.doBiteAttack(entityIn);
						this.attackDecision = this.getRNG().nextBoolean();
						return success;
					}
				}
				break;
			default:
				if (this.getAnimation() != ANIMATION_BITE) {
					this.setAnimation(ANIMATION_BITE);
					return false;
				} else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
					boolean success = this.doBiteAttack(entityIn);
					this.attackDecision = this.getRNG().nextBoolean();
					return success;
				}
				break;
		}

		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (IceAndFire.dragonIce.damageType.contentEquals(source.damageType) || "ooze".contentEquals(source.damageType) || "cold_fire".contentEquals(source.damageType)) {
				return false;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!world.isRemote) {
			if (this.isInLava() && !this.isFlying() && !this.isChild() && !this.isHovering() && this.canMove()) {
				this.setHovering(true);
				this.jump();
				this.motionY += 0.8D;
				this.flyTicks = 0;
			}
			if (this.getAttackTarget() != null && !this.isSleeping() && this.getAnimation() != ANIMATION_SHAKEPREY) {
				if ((!attackDecision || this.isFlying()) && !isInLava() && !isTargetBlocked(new Vec3d(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ))) {
					shootIceAtMob(this.getAttackTarget());
				} else {
					if (this.getEntityBoundingBox().grow(this.getRenderSize() * 0.5F, this.getRenderSize() * 0.5F, this.getRenderSize() * 0.5F).intersects(this.getAttackTarget().getEntityBoundingBox())) {
						attackEntityAsMob(this.getAttackTarget());
					}

				}
			} else {
				this.setBreathingFire(false);
			}
			if (this.isInsideWaterBlock() && !this.isSwimming() && (!this.isFlying() && !this.isHovering() || this.flyTicks > 100)) {
				this.setSwimming(true);
				this.setHovering(false);
				this.setFlying(false);
				this.flyTicks = 0;
				this.ticksSwimming = 0;
			}
			if (this.isInsideWaterBlock()) {
				swimAround();
			}
			if (!this.isInsideWaterBlock() && this.isSwimming()) {
				this.setSwimming(false);
				ticksSwimming = 0;
			}
			if (this.isSwimming()) {
				ticksSwimming++;
				if ((this.isInsideWaterBlock() || this.isOverWater()) && (ticksSwimming > 4000 || this.getAttackTarget() != null && this.isInWater() != this.getAttackTarget().isInWater()) && !this.isChild() && !this.isHovering() && !this.isFlying()) {
					this.setHovering(true);
					this.jump();
					this.motionY += 0.8D;
					this.flyTicks = 0;
					this.setSwimming(false);
				}
			}
		}
		boolean swimming = isSwimming() && !isHovering() && !isFlying() && ridingProgress == 0;
		if (swimming && swimProgress < 20.0F) {
			swimProgress += 0.5F;
		} else if (!swimming && swimProgress > 0.0F) {
			swimProgress -= 0.5F;
		}
		if (swimCycle < 48) {
			swimCycle += 2;
		} else {
			swimCycle = 0;
		}
		if (this.isModelDead() && swimCycle != 0) {
			swimCycle = 0;
		}
	}

	public boolean isInsideWaterBlock() {
		return this.isInsideOfMaterial(Material.WATER);
	}


	public void riderShootFire(Entity controller) {
		if (this.getRNG().nextInt(5) == 0 && !this.isChild()) {
			if (this.getAnimation() != ANIMATION_FIRECHARGE) {
				this.setAnimation(ANIMATION_FIRECHARGE);
			} else if (this.getAnimationTick() == 15) {
				rotationYaw = renderYawOffset;
				Vec3d headPos = getHeadPosition();
				this.playSound(ModSounds.ICEDRAGON_BREATH, 4, 1);
				double d2 = controller.getLookVec().x;
				double d3 = controller.getLookVec().y;
				double d4 = controller.getLookVec().z;
				float inaccuracy = 1.0F;
				d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
				d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
				d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
				EntityDragonIceCharge iceChargeProjectile = new EntityDragonIceCharge(world, this, d2, d3, d4);
				float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
				iceChargeProjectile.setSizes(size, size);
				iceChargeProjectile.setPosition(headPos.x, headPos.y, headPos.z);
				if (!world.isRemote) {
					world.spawnEntity(iceChargeProjectile);
				}

			}
		} else {
			if (this.isBreathingFire()) {
				if (this.isActuallyBreathingFire() && this.ticksExisted % 3 == 0) {
					rotationYaw = renderYawOffset;
					Vec3d headPos = getHeadPosition();
					double d2 = controller.getLookVec().x;
					double d3 = controller.getLookVec().y;
					double d4 = controller.getLookVec().z;
					float inaccuracy = 1.0F;
					d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
					d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
					d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
					EntityDragonIce iceProjectile = new EntityDragonIce(world, this, d2, d3, d4);
					this.playSound(ModSounds.ICEDRAGON_BREATH, 4, 1);
					iceProjectile.setPosition(headPos.x, headPos.y, headPos.z);
					if (!world.isRemote) {
						world.spawnEntity(iceProjectile);
					}
				}
			} else {
				this.setBreathingFire(true);
			}
		}
	}

	public void swimAround() {
		if (waterTarget != null) {
			if (!isTargetInWater() || getDistance(waterTarget.getX() + 0.5D, waterTarget.getY() + 0.5D, waterTarget.getZ() + 0.5D) < 2 || ticksSwimming > 6000) {
				waterTarget = null;
			}
			swimTowardsTarget();
		}
	}

	@Override
	public ResourceLocation getDeadLootTable() {
		if (this.getDeathStage() >= (this.getAgeInDays() / 5) / 2) {
			return SKELETON_LOOT;
		}else{
			return isMale() ? MALE_LOOT : FEMALE_LOOT;
		}
	}

	public void swimTowardsTarget() {
		if (waterTarget != null && isTargetInWater() && this.isInsideWaterBlock() && this.getDistanceSquared(new Vec3d(waterTarget.getX(), this.posY, waterTarget.getZ())) > 3) {
			double targetX = waterTarget.getX() + 0.5D - posX;
			double targetY = waterTarget.getY() + 1D - posY;
			double targetZ = waterTarget.getZ() + 0.5D - posZ;
			motionX += (Math.signum(targetX) * 0.5D - motionX) * 0.100000000372529 * ((3 * ((double) this.getAgeInDays() / 125)) + 2);
			motionY += (Math.signum(targetY) * 0.5D - motionY) * 0.100000000372529 * ((3 * ((double) this.getAgeInDays() / 125)) + 2);
			motionZ += (Math.signum(targetZ) * 0.5D - motionZ) * 0.100000000372529 * ((3 * ((double) this.getAgeInDays() / 125)) + 2);
			float angle = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) - 90.0F;
			float rotation = MathHelper.wrapDegrees(angle - rotationYaw);
			moveForward = 0.5F;
			prevRotationYaw = rotationYaw;
			rotationYaw += rotation;
		} else {
			this.waterTarget = null;
		}
	}

	protected boolean isTargetInWater() {
		return waterTarget != null && (world.getBlockState(waterTarget).getMaterial() == Material.WATER);
	}

	private void shootIceAtMob(EntityLivingBase entity) {
		if (!this.attackDecision) {
			if (this.getRNG().nextInt(5) == 0) {
				if (this.getAnimation() != ANIMATION_FIRECHARGE) {
					this.setAnimation(ANIMATION_FIRECHARGE);
				} else if (this.getAnimationTick() == 15) {
					rotationYaw = renderYawOffset;
					Vec3d headPos = getHeadPosition();
					double d2 = entity.posX - headPos.x;
					double d3 = entity.posY - headPos.y;
					double d4 = entity.posZ - headPos.z;
					float inaccuracy = 1.0F;
					d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
					d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
					d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
					this.playSound(ModSounds.ICEDRAGON_BREATH, 4, 1);
					EntityDragonIceCharge iceChargeProjectile = new EntityDragonIceCharge(world, this, d2, d3, d4);
					float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
					iceChargeProjectile.setSizes(size, size);
					iceChargeProjectile.setPosition(headPos.x, headPos.y, headPos.z);
					if (!world.isRemote) {
						world.spawnEntity(iceChargeProjectile);
					}
					if (entity.isDead) {
						this.setBreathingFire(false);
						this.attackDecision = true;
					}
				}
			} else {
				if (this.isBreathingFire()) {
					if (this.isActuallyBreathingFire() && this.ticksExisted % 3 == 0) {
						rotationYaw = renderYawOffset;
						Vec3d headPos = getHeadPosition();
						double d2 = entity.posX - headPos.x;
						double d3 = entity.posY - headPos.y;
						double d4 = entity.posZ - headPos.z;
						float inaccuracy = 1.0F;
						d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
						d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
						d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
						this.playSound(ModSounds.ICEDRAGON_BREATH, 4, 1);
						EntityDragonIce iceProjectile = new EntityDragonIce(world, this, d2, d3, d4);
						float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
						iceProjectile.setPosition(headPos.x, headPos.y, headPos.z);
						if (!world.isRemote && !entity.isDead) {
							world.spawnEntity(iceProjectile);
						}
						iceProjectile.setSizes(size, size);
						if (entity.isDead) {
							this.setBreathingFire(false);
							this.attackDecision = true;
						}
					}
				} else {
					this.setBreathingFire(true);
				}
			}
		}
		this.faceEntity(entity, 360, 360);
	}

	public boolean isSwimming() {
		if (world.isRemote) {
			boolean swimming = this.dataManager.get(SWIMMING);
			this.isSwimming = swimming;
			return swimming;
		}
		return isSwimming;
	}

	public void setSwimming(boolean swimming) {
		this.dataManager.set(SWIMMING, swimming);
		if (!world.isRemote) {
			this.isSwimming = swimming;
		}
	}

	@Override
	protected ItemStack getSkull() {
		return new ItemStack(ModItems.dragon_skull, 1, 1);
	}

	@Override
	protected ItemStack getHorn() {
		return new ItemStack(ModItems.dragon_horn_ice);
	}

	@Override
	public Item getBlood() {
		return ModItems.ice_dragon_blood;
	}

	@Override
	public Item getHeart() {
		return ModItems.ice_dragon_heart;
	}

	@Override
	public Item getFlesh() {
		return ModItems.ice_dragon_flesh;
	}

	@Override
	protected int getBaseEggTypeValue() {
		return 4;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTeen() ? ModSounds.ICEDRAGON_TEEN_IDLE : this.isAdult() ? ModSounds.ICEDRAGON_ADULT_IDLE : ModSounds.ICEDRAGON_CHILD_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return this.isTeen() ? ModSounds.ICEDRAGON_TEEN_HURT : this.isAdult() ? ModSounds.ICEDRAGON_ADULT_HURT : ModSounds.ICEDRAGON_CHILD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTeen() ? ModSounds.ICEDRAGON_TEEN_DEATH : this.isAdult() ? ModSounds.ICEDRAGON_ADULT_DEATH : ModSounds.ICEDRAGON_CHILD_DEATH;
	}

	@Override
	public SoundEvent getRoarSound() {
		return this.isTeen() ? ModSounds.ICEDRAGON_TEEN_ROAR : this.isAdult() ? ModSounds.ICEDRAGON_ADULT_ROAR : ModSounds.ICEDRAGON_CHILD_ROAR;
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT, EntityDragonBase.ANIMATION_SPEAK, EntityDragonBase.ANIMATION_BITE, EntityDragonBase.ANIMATION_SHAKEPREY, EntityIceDragon.ANIMATION_TAILWHACK, EntityIceDragon.ANIMATION_FIRECHARGE, EntityIceDragon.ANIMATION_WINGBLAST, EntityIceDragon.ANIMATION_ROAR};
	}

	public boolean isBreedingItem(@Nullable ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() != null && stack.getItem() == ModItems.frost_stew;
	}
}
