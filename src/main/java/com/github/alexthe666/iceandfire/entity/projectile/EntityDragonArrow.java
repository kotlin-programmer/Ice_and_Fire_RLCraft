package com.github.alexthe666.iceandfire.entity.projectile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.explosion.BoneArrowExplosion;
import com.github.alexthe666.iceandfire.enums.EnumParticle;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityDragonArrow extends EntityArrow {

	private static final DataParameter<ItemStack> ARROW = EntityDataManager.createKey(EntityDragonArrow.class, DataSerializers.ITEM_STACK);

	public enum Type {
		DEFAULT,
		FIRE,
		ICE,
		LIGHTNING;

		public EnumParticle getParticle() {
			if (this == FIRE) {
				return EnumParticle.FLAME;
			} else if (this == ICE) {
				return EnumParticle.SNOWFLAKE;
			} else if (this == LIGHTNING) {
				return EnumParticle.SPARK;
			}
			return null;
		}

		public Item getArrow() {
			if (this == FIRE) {
				return IafItemRegistry.dragonbone_arrow_fire;
			} else if (this == ICE) {
				return IafItemRegistry.dragonbone_arrow_ice;
			} else if (this == LIGHTNING) {
				return IafItemRegistry.dragonbone_arrow_lightning;
			}
			return IafItemRegistry.dragonbone_arrow;
		}
	}

	public EntityDragonArrow(World worldIn) {
		super(worldIn);
		this.setDamage(10);
	}

	public EntityDragonArrow(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.setDamage(10);
	}

	public EntityDragonArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
		this.setDamage(10);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ARROW, ItemStack.EMPTY);
	}

	public void setArrow(ItemStack stack) {
		this.getDataManager().set(ARROW, stack);
		this.getDataManager().setDirty(ARROW);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		EnumParticle particle = getType().getParticle();
		if (particle != null) {
			if (world.isRemote && !this.inGround) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				double xRatio = motionX * height;
				double zRatio = motionZ * height;
				IceAndFire.PROXY.spawnParticle(particle, world, this.posX + xRatio + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d0 * 10.0D, this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * 10.0D, this.posZ + zRatio + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d2 * 10.0D, d0, d1, d2);
			}
		}
	}

	public Type getType() {
		ItemStack arrowStack = getArrowStack();
		if (arrowStack == null || arrowStack.isEmpty()) {
			return Type.DEFAULT;
		}
		if (arrowStack.getItem() == IafItemRegistry.dragonbone_arrow_fire) {
			return Type.FIRE;
		} else if (arrowStack.getItem() == IafItemRegistry.dragonbone_arrow_ice) {
			return Type.ICE;
		} else if (arrowStack.getItem() == IafItemRegistry.dragonbone_arrow_lightning) {
			return Type.LIGHTNING;
		}
		return Type.DEFAULT;
	}

	protected void damageShield(EntityPlayer player, float damage) {
		if (damage >= 3.0F && player.getActiveItemStack().getItem().isShield(player.getActiveItemStack(), player)) {
			ItemStack copyBeforeUse = player.getActiveItemStack().copy();
			int i = 1 + MathHelper.floor(damage);
			player.getActiveItemStack().damageItem(i, player);

			if (player.getActiveItemStack().isEmpty()) {
				EnumHand enumhand = player.getActiveHand();
				net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, enumhand);

				if (enumhand == EnumHand.MAIN_HAND) {
					this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
				} else {
					this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
				}
				player.resetActiveHand();
				this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
			}
		}
	}

	@Override
	protected void arrowHit(EntityLivingBase living) {
		if (living instanceof EntityPlayer) {
			this.damageShield((EntityPlayer) living, (float) this.getDamage());
		}
		if (!world.isRemote) {
			BoneArrowExplosion explosion = new BoneArrowExplosion(world, shootingEntity, living.posX, living.posY, living.posZ, 3F, getType());
			explosion.doExplosionA();
			explosion.doExplosionB(true);
		}
	}

	@Override
	protected ItemStack getArrowStack() {
		return this.getDataManager().get(ARROW);
	}
}