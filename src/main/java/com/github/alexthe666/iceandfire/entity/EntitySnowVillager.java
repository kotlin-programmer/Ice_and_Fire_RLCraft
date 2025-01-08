package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class EntitySnowVillager extends EntityVillager {

	private static Field FIELD_BABY;
	private static Field FIELD_PROFESSION;
	private static Field FIELD_CAREER;

	private String professionName;
	private net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof;

	public EntitySnowVillager(World worldIn) {
		super(worldIn);
	}

	public EntitySnowVillager(World worldIn, int profession) {
		super(worldIn, profession);
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(this);
		if (capability != null && capability.isStoned()) {
			return false;
		}
		return super.processInteract(player, hand);
	}

	public void setProfession(int professionId) {
		if (professionId > 2) {
			professionId = 2;
		}
		this.dataManager.set(PROFESSION(), Integer.valueOf(professionId));

	}

	private DataParameter<Boolean> BABY() {
		try {
			if(FIELD_BABY == null) {
				FIELD_BABY = ObfuscationReflectionHelper.findField(EntityAgeable.class, "field_184751_bv");
				FIELD_BABY.setAccessible(true);
			}
			return (DataParameter<Boolean>)FIELD_BABY.get(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private DataParameter<Integer> PROFESSION() {
		try {
			if(FIELD_PROFESSION == null) {
				FIELD_PROFESSION = ObfuscationReflectionHelper.findField(EntityVillager.class, "field_184752_bw");
				FIELD_PROFESSION.setAccessible(true);
			}
			return (DataParameter<Integer>)FIELD_PROFESSION.get(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (HAND_STATES.equals(key) && this.world.isRemote) {
			if (this.isHandActive() && this.activeItemStack.isEmpty()) {
				this.activeItemStack = this.getHeldItem(this.getActiveHand());

				if (!this.activeItemStack.isEmpty()) {
					this.activeItemStackUseCount = this.activeItemStack.getMaxItemUseDuration();
				}
			} else if (!this.isHandActive() && !this.activeItemStack.isEmpty()) {
				this.activeItemStack = ItemStack.EMPTY;
				this.activeItemStackUseCount = 0;
			}
		}
//field_184751_bv
		if (BABY().equals(key)) {
			this.setScaleForAge(this.isChild());
		}
//field_184752_bw

	}

	@Override
	public void onDeath(DamageSource cause) {
		if (cause.getTrueSource() != null && cause.getTrueSource() instanceof EntityZombie
				&& (this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD)) {
		} else {
			super.onDeath(cause);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setProfession(net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof) {
		if (IafVillagerRegistry.INSTANCE.professions.containsValue(prof)) {
			this.setProfession(net.minecraftforge.fml.common.registry.VillagerRegistry.getId(prof));
		} else {
			IafVillagerRegistry.INSTANCE.setRandomProfession(this, this.world.rand);
		}
	}

	public EntityVillager createChild(EntityAgeable ageable) {
		EntitySnowVillager entityvillager = new EntitySnowVillager(this.world);
		entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData) null);
		return entityvillager;
	}

	public net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession getProfessionForge() {
		if (this.prof == null) {
			String p = this.getEntityData().getString("ProfessionName");
			if (p.isEmpty()) {
				this.prof = IafVillagerRegistry.INSTANCE.professions.get(this.getRNG().nextInt(3));

			} else {
				this.prof = IafVillagerRegistry.INSTANCE.professions.get(intFromProfesion(p));
			}
			try {
				if(FIELD_CAREER == null) {
					FIELD_CAREER = ObfuscationReflectionHelper.findField(EntityVillager.class, "field_175563_bv");
					FIELD_CAREER.setAccessible(true);
				}
				FIELD_CAREER.set(this, 1);
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return this.prof;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setProfession(compound.getInteger("Profession"));
		if (compound.hasKey("ProfessionName")) {
			net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession p =
					IafVillagerRegistry.INSTANCE.professions.get(intFromProfesion(compound.getString("ProfessionName")));
			if (p == null)
				p = IafVillagerRegistry.INSTANCE.professions.get(0);
			this.setProfession(p);
		}

	}

	@Override
	public IEntityLivingData finalizeMobSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData data, boolean forgeCheck) {
		this.prof = IafVillagerRegistry.INSTANCE.professions.get(this.getRNG().nextInt(3));
		return data;
	}


	private int intFromProfesion(String prof) {
		if (prof.contains("fisherman")) {
			return 0;
		}
		if (prof.contains("craftsman")) {
			return 1;
		}
		if (prof.contains("shaman")) {
			return 2;
		}
		return 0;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
}
