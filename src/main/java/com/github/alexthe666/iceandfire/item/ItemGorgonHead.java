package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDropArmor;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.List;

public class ItemGorgonHead extends Item implements ICustomRendered {

	private static Method METHOD_DEATHSOUND;

	public ItemGorgonHead() {
		this.setCreativeTab(IceAndFire.TAB);
		this.setTranslationKey("iceandfire.gorgon_head");
		this.maxStackSize = 1;
		this.setRegistryName(IceAndFire.MODID, "gorgon_head");
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setTagCompound(new NBTTagCompound());
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
		if (worldIn.isRemote || stack.getMetadata() != 1) {
			stack.setItemDamage(0);
			return;
		}
		double dist = 32;
		Vec3d vec3d = entity.getPositionEyes(1.0F);
		Vec3d vec3d1 = entity.getLook(1.0F);
		Vec3d vec3d2 = vec3d.add(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist);
		double d1 = dist;
		Entity pointedEntity = null;
		List<Entity> list = worldIn.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
			public boolean apply(@Nullable Entity in) {
				if(in instanceof EntityLiving) {
					EntityLiving entity = (EntityLiving)in;
					if(!entity.isDead && DragonUtils.isAlive(entity) && entity.canBeCollidedWith() && !entity.isPotionActive(MobEffects.BLINDNESS) && !(entity instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues)entity).canBeTurnedToStone())) {
						if(!IceAndFireConfig.isEntityBlacklistedFromBeingStoned(entity)) {
							IEntityEffectCapability cap = InFCapabilities.getEntityEffectCapability(entity);
							return cap != null && !cap.isStoned();
						}
					}
				}
				return false;
			}
		}));
		double d2 = d1;
        for (Entity value : list) {
            AxisAlignedBB axisalignedbb = value.getEntityBoundingBox().grow(value.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

            if (axisalignedbb.contains(vec3d)) {
                if (d2 >= 0.0D) {
                    pointedEntity = value;
                    d2 = 0.0D;
                }
            } else if (raytraceresult != null) {
                double d3 = vec3d.distanceTo(raytraceresult.hitVec);
                if (d3 < d2 || d2 == 0.0D) {
                    if (value.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = value;
                        }
                    } else {
                        pointedEntity = value;
                        d2 = d3;
                    }
                }
            }
        }
		if (pointedEntity != null) {
			if (pointedEntity instanceof EntityLiving || pointedEntity instanceof EntityPlayer) {
				if (pointedEntity instanceof EntityPlayer) {
					pointedEntity.attackEntityFrom(IceAndFire.gorgon, Integer.MAX_VALUE);
					EntityStoneStatue statue = new EntityStoneStatue(worldIn);
					statue.setPositionAndRotation(pointedEntity.posX, pointedEntity.posY, pointedEntity.posZ, pointedEntity.rotationYaw, pointedEntity.rotationPitch);
					statue.smallArms = true;
                    worldIn.spawnEntity(statue);
                } else {
					IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLiving)pointedEntity);
					if (capability != null) {
						capability.setStoned();
					}
					if (pointedEntity instanceof EntityDragonBase) {
						EntityDragonBase dragon = (EntityDragonBase) pointedEntity;
						dragon.setFlying(false);
						dragon.setHovering(false);
						dragon.airTarget = null;
					}
					if (pointedEntity instanceof EntityHippogryph) {
						EntityHippogryph dragon = (EntityHippogryph) pointedEntity;
						dragon.setFlying(false);
						dragon.setHovering(false);
						dragon.airTarget = null;
					}
					if(pointedEntity instanceof IDropArmor){
						((IDropArmor) pointedEntity).dropArmor();
					}
				}

				if (pointedEntity instanceof EntityGorgon) {
					worldIn.playSound(null, pointedEntity.posX, pointedEntity.posY, pointedEntity.posZ, ModSounds.GORGON_PETRIFY, SoundCategory.HOSTILE, 1, 1);
				} else {
					worldIn.playSound(null, pointedEntity.posX, pointedEntity.posY, pointedEntity.posZ, ModSounds.GORGON_TURN_STONE, SoundCategory.HOSTILE, 1, 1);
				}
				SoundEvent deathSound = null;
				try {
					if(METHOD_DEATHSOUND == null) {
						METHOD_DEATHSOUND = ObfuscationReflectionHelper.findMethod(EntityLivingBase.class, "func_184615_bR", SoundEvent.class);
						METHOD_DEATHSOUND.setAccessible(true);
					}
					deathSound = (SoundEvent)METHOD_DEATHSOUND.invoke(pointedEntity);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if (deathSound != null) {
					worldIn.playSound(null, pointedEntity.posX, pointedEntity.posY, pointedEntity.posZ, deathSound, SoundCategory.HOSTILE, 1, 1);
				}
				if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative())) {
					stack.shrink(1);
				}
			}
		}
		stack.setItemDamage(0);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		playerIn.setActiveHand(hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		int i = this.getMaxItemUseDuration(stack) - count;
		if (i > 20 && stack.getMetadata() == 0) {
			stack.setItemDamage(1);
		}
	}
}
