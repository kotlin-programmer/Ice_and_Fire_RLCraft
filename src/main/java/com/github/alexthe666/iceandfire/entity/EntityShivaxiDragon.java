package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.ai.DragonAIAirTarget;
import com.github.alexthe666.iceandfire.entity.ai.DragonAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.DragonAILookIdle;
import com.github.alexthe666.iceandfire.entity.ai.DragonAITarget;
import com.github.alexthe666.iceandfire.entity.ai.DragonAITargetItems;
import com.github.alexthe666.iceandfire.entity.ai.DragonAIWander;
import com.github.alexthe666.iceandfire.entity.ai.DragonAIWatchClosest;
import com.github.alexthe666.iceandfire.entity.projectile.EntityShivaxiDragonLightning;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityShivaxiDragon extends EntityLightningDragon {

	private static final UUID SHIVAXI = UUID.fromString("cdfccefb-1a2e-4fb8-a3b5-041da27fde61");

	public EntityShivaxiDragon(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, this.aiSit = new EntityAISit(this));
		this.tasks.addTask(3, new DragonAIAttackMelee(this, 1.5D, false));
		this.tasks.addTask(5, new DragonAIAirTarget(this));
		this.tasks.addTask(6, new DragonAIWander(this, 1.0D));
		this.tasks.addTask(7, new DragonAIWatchClosest(this, EntityLivingBase.class, 6.0F));
		this.tasks.addTask(7, new DragonAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(4, new DragonAITarget<>(this, EntityLivingBase.class, true, new Predicate<Entity>() {
			@Override
			public boolean apply(@Nullable Entity entity) {
				return entity instanceof EntityLivingBase
						&& DragonUtils.isAlive((EntityLivingBase) entity)
						&& !EntityShivaxiDragon.this.isControllingPassenger(entity);
			}
		}));
		this.targetTasks.addTask(5, new DragonAITargetItems<>(this, false));
	}

	public void riderShootFire(Entity controller) {
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
				EntityShivaxiDragonLightning lightningProjectile = new EntityShivaxiDragonLightning(world, this, d2, d3, d4);
				this.playSound(IafSoundRegistry.LIGHTNINGDRAGON_BREATH, 4, 1);
				lightningProjectile.setPosition(headPos.x, headPos.y, headPos.z);
				lightningProjectile.setShootingEntity(this.getEntityId());
				if (!world.isRemote) {
					world.spawnEntity(lightningProjectile);
				}
			}
		} else {
			this.setBreathingFire(true);
		}
	}

	@Override
	protected void shootLightningAtMob(EntityLivingBase entity) {
		if (!this.attackDecision) {
			if (this.isBreathingFire()) {
				if (this.isActuallyBreathingFire() && this.ticksExisted % 3 == 0) {
					rotationYaw = renderYawOffset;
					Vec3d headPos = getHeadPosition();
					double d2 = entity.posX - headPos.x;
					double d3 = entity.posY - headPos.y;
					double d4 = entity.posZ - headPos.z;
					float inaccuracy = 1.0F;
					d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
					d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
					d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
					this.playSound(IafSoundRegistry.LIGHTNINGDRAGON_BREATH, 4, 1);
					EntityShivaxiDragonLightning lightningProjectile = new EntityShivaxiDragonLightning(world, this, d2, d3, d4);
					float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
					lightningProjectile.setPosition(headPos.x, headPos.y, headPos.z);
					lightningProjectile.setShootingEntity(this.getEntityId());
					if (!world.isRemote && !entity.isDead) {
						world.spawnEntity(lightningProjectile);
					}
					lightningProjectile.setSizes(size, size);
					if (entity.isDead) {
						this.setBreathingFire(false);
						this.attackDecision = true;
					}
				}
			} else {
				this.setBreathingFire(true);
			}
			this.faceEntity(entity, 360, 360);
		}
	}

	@Override
	public String getVariantName(int variant) {
		return "electric_";
	}

	@Override
	protected ItemStack getSkull() {
		return new ItemStack(Items.AIR);
	}

	@Override
	public Item getBlood() {
		return Items.GLASS_BOTTLE;
	}

	@Override
	public Item getHeart() {
		return Items.AIR;
	}

	@Override
	public Item getFlesh() {
		return Items.AIR;
	}

	@Override
	public Item getVariantScale(int variant) {
		return Items.AIR;
	}

	@Override
	public Item getVariantEgg(int variant) {
		return Items.AIR;
	}

	public boolean isBreedingItem(@Nullable ItemStack stack) {
		return false;
	}

	@Override
	public boolean isMale() {
		return true;
	}

	@Override
	public boolean isTimeToWake() {
		return true;
	}

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setGender(true);
		this.setVariant(0);
		this.setSleeping(false);
		this.growDragon(125);
		this.updateAttributes();
		this.heal((float) maximumHealth);
		this.attackDecision = true;
		this.setHunger(50);
		return livingdata;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	public static boolean isAuthorized(EntityPlayer player) {
		return SHIVAXI.equals(player.getUniqueID());
	}
}
