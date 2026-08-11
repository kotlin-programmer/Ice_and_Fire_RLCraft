package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.projectile.EntityDragonArrow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemDragonBow extends ItemBow implements ICustomRendered {

	EntityDragonArrow.Type type;

	public ItemDragonBow(EntityDragonArrow.Type type, String gameName, String name) {
		this.type = type;
		this.maxStackSize = 1;
		if (type == EntityDragonArrow.Type.DEFAULT) {
			this.setMaxDamage(584);
		} else {
			this.setMaxDamage(700);
		}
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey(name);
		this.setRegistryName(IceAndFire.MODID, gameName);
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if (entityIn == null) {
					return 0.0F;
				} else {
					ItemStack itemstack = entityIn.getActiveItemStack();
					return !itemstack.isEmpty() && itemstack.getItem() instanceof ItemDragonBow ? (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
				}
			}
		});
		this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
			}
		});
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		NonNullList<ItemStack> boneItems = OreDictionary.getOres("boneDragon");
		for (ItemStack bone : boneItems) {
			if (OreDictionary.itemMatches(repair, bone, false)) {
				return true;
			}
		}
		return super.getIsRepairable(toRepair, repair);
	}

	@Override
	protected ItemStack findAmmo(EntityPlayer player) {
		ItemStack ammo = findDragonBoneArrow(player);
		if (!ammo.isEmpty()) {
			return ammo;
		}
		return super.findAmmo(player);
	}

	public static ItemStack findDragonBoneArrow(EntityPlayer player) {
		if (isDragonboneArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		} else if (isDragonboneArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		} else if (isArrowStack(player.getHeldItem(EnumHand.OFF_HAND))) {
			return ItemStack.EMPTY;
		} else if (isArrowStack(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return ItemStack.EMPTY;
		}
		for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (isDragonboneArrow(stack)) {
				return stack;
			} else if (isArrowStack(stack)) {
				return ItemStack.EMPTY;
			}
		}
		return ItemStack.EMPTY;
	}

	public static boolean isDragonboneArrow(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		return stack.getItem() == IafItemRegistry.dragonbone_arrow
				|| stack.getItem() == IafItemRegistry.dragonbone_arrow_fire
				|| stack.getItem() == IafItemRegistry.dragonbone_arrow_ice
				|| stack.getItem() == IafItemRegistry.dragonbone_arrow_lightning;
	}

	private static boolean isArrowStack(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		return stack.getItem() instanceof ItemArrow;
	}


	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			boolean hasInfinity = entityplayer.capabilities.isCreativeMode || hasInfinity(stack);
			ItemStack itemstack = this.findAmmo(entityplayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer) entityLiving, i, !itemstack.isEmpty() || hasInfinity);
			if (i < 0) return;

			if (!itemstack.isEmpty() || hasInfinity) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getArrowVelocity(i);

				if ((double) f >= 0.1D) {
					boolean isInfinity = entityplayer.capabilities.isCreativeMode || itemstack.getItem() instanceof ItemArrow && ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer);

					if (!worldIn.isRemote) {
						EntityArrow arrow = createArrow(worldIn, itemstack, entityplayer);
						arrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 0.7F);

						if (f == 1.0F) {
							arrow.setIsCritical(true);
						}

						int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

						if (j > 0) {
							arrow.setDamage(arrow.getDamage() + (double) j * 0.5D + 0.5D);
						}

						int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

						if (k > 0) {
							arrow.setKnockbackStrength(k);
						}

						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
							arrow.setFire(100);
						}

						stack.damageItem(1, entityplayer);

						if (isInfinity) {
							arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(arrow);
					}

					worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

					if (!isInfinity) {
						itemstack.shrink(1);

						if (itemstack.isEmpty()) {
							entityplayer.inventory.deleteStack(itemstack);
						}
					}

					entityplayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}

	public static float getArrowVelocity(int charge) {
		float f = (float)charge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;

		if (f > 1.25F) {
			f = 1.25F;
		}

		return f;
	}

	public boolean hasInfinity(ItemStack bow) {
		int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, bow);
		return enchant > 0;
	}

	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		if (stack.getItem() instanceof ItemArrow) {
			return ((ItemArrow) stack.getItem()).createArrow(worldIn, stack, shooter);
		}
		ItemDragonArrow item = (ItemDragonArrow) stack.getItem();
		EntityDragonArrow arrow = new EntityDragonArrow(worldIn, shooter);
		arrow.setType(item.getType() != EntityDragonArrow.Type.DEFAULT ? item.getType() : this.type);
		return arrow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		boolean flag = this.findAmmo(playerIn) != null;

		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStackIn, worldIn, playerIn, hand, flag);
		if (ret != null)
			return ret;

		if (!playerIn.capabilities.isCreativeMode && !flag) {
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		} else {
			playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}
	}
}
