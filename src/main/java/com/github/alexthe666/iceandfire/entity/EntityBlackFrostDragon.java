package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityBlackFrostDragon extends EntityIceDragon implements IDreadMob {

	protected static final DataParameter<Optional<UUID>> COMMANDER_UNIQUE_ID = EntityDataManager.createKey(EntityBlackFrostDragon.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	public EntityBlackFrostDragon(World worldIn) {
		super(worldIn);
		this.maximumArmor = 70D;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(COMMANDER_UNIQUE_ID, Optional.absent());
	}


	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		EntityDreadQueen queen = this.getRidingQueen();
		if (queen != null && queen.getAttackTarget() != null) {
			this.setAttackTarget(queen.getAttackTarget());
		}
	}
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new DreadAIDragonFindQueen(this));
		this.tasks.addTask(1, this.aiSit = new EntityAISit(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.5D, false));
		this.tasks.addTask(3, new DragonAIAirTarget(this));
		this.tasks.addTask(4, new DragonAIWaterTarget(this));
		this.tasks.addTask(5, new DragonAIWander(this, 1.0D));
		this.tasks.addTask(6, new DragonAIWatchClosest(this, EntityLivingBase.class, 6.0F));
		this.tasks.addTask(6, new DragonAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(4, new DreadAITargetNonDread(this, EntityLivingBase.class, false, new Predicate<Entity>() {
			@Override
			public boolean apply(@Nullable Entity entity) {
				return entity instanceof EntityLivingBase && DragonUtils.canHostilesTarget(entity);
			}
		}));
		this.targetTasks.addTask(5, new DragonAITargetItems<>(this, false));
	}

	@Nullable
	@Override
	public Entity getControllingPassenger() {
		Entity commander = getCommander();
		for (Entity passenger : this.getPassengers()) {
			if (passenger instanceof EntityDreadQueen || commander != null && passenger.getUniqueID().equals(commander.getUniqueID())) {
				return passenger;
			}
		}
		return super.getControllingPassenger();
	}

	@Override
	public boolean canBeSteered() {
		return false;
	}

	@Override
	public boolean isAllowedToTriggerFlight() {
		return this.hasFlightClearance() && !this.isSitting() && !this.isChild() && !this.isSleeping() && this.canMove() && this.onGround;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if (this.getCommanderId() == null) {
			compound.setString("CommanderUUID", "");
		} else {
			compound.setString("CommanderUUID", this.getCommanderId().toString());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		String s;
		if (compound.hasKey("CommanderUUID", 8)) {
			s = compound.getString("CommanderUUID");
		} else {
			String s1 = compound.getString("Owner");
			s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
		}
		if (!s.isEmpty()) {
			try {
				this.setCommanderId(UUID.fromString(s));
			} catch (Throwable ignored) {
			}
		}
	}

	@Override
	public boolean isOnSameTeam(Entity entityIn) {
		return IDreadMob.isOnSameTeam(entityIn) || super.isOnSameTeam(entityIn);
	}

	@Override
	public boolean shouldRiderSit() {
		return this.getControllingPassenger() != null || getRidingQueen() != null;
	}

	public EntityDreadQueen getRidingQueen() {
		for (Entity passenger : this.getPassengers()) {
			if (passenger instanceof EntityDreadQueen) {
				return (EntityDreadQueen) passenger;
			}
		}
		return null;
	}

	@Nullable
	public UUID getCommanderId() {
		return (UUID) ((Optional<?>) this.dataManager.get(COMMANDER_UNIQUE_ID)).orNull();
	}

	public void setCommanderId(@Nullable UUID uuid) {
		this.dataManager.set(COMMANDER_UNIQUE_ID, Optional.fromNullable(uuid));
	}

	@Override
	public Entity getCommander() {
		try {
			UUID uuid = this.getCommanderId();
			EntityLivingBase player = uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
			if (player != null) {
				return player;
			} else {
				if (!world.isRemote) {
					Entity entity = world.getMinecraftServer().getWorld(this.dimension).getEntityFromUuid(uuid);
					if (entity instanceof EntityLivingBase) {
						return entity;
					}
				}
			}
		} catch (IllegalArgumentException var2) {
			return null;
		}
		return null;
	}

	@Override
	public String getVariantName(int variant) {
		return "white_";
	}

	@Override
	public Item getVariantScale(int variant) {
		return IafItemRegistry.dragonscales_white;
	}

	@Override
	public Item getVariantEgg(int variant) {
		return IafItemRegistry.dragonegg_white;
	}

	public boolean isBreedingItem(@Nullable ItemStack stack) {
		return false;
	}

	@Override
	public int getDragonStage() {
		return 5;
	}

	@Override
	public int getAgeInDays() {
		return 125;
	}

	@Override
	public int getArmorOrdinal(ItemStack stack) {
		return 0;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	public boolean isMale() {
		return false;
	}

	@Override
	public boolean isTimeToWake() {
		return true;
	}

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setGender(false);
		this.setSleeping(false);
		this.setVariant(1);
		this.updateAttributes();
		this.growDragon(125);
		this.setAgingDisabled(true);
		this.heal((float) maximumHealth);
		this.setHunger(50);
		return livingdata;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected int getFlightChancePerTick(){
		return 15;
	}
}
