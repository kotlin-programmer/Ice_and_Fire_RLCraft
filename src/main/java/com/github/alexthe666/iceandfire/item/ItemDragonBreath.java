package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.projectile.EntityDragonBreath;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemDragonBreath extends ItemGeneric {

	public final EnumDragonType type;

	public ItemDragonBreath(EnumDragonType type) {
		super(
				type.getName().toLowerCase() + "_dragon_breath",
				"iceandfire." + type.getName().toLowerCase() + "_dragon_breath",
				0
		);
		this.type = type;
		this.maxStackSize = 8;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack heldItemStack = playerIn.getHeldItem(handIn);
		ItemStack stack = playerIn.capabilities.isCreativeMode ? heldItemStack.copy() : heldItemStack.splitStack(1);
		worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isRemote) {
			EntityDragonBreath dragonBreath = new EntityDragonBreath(worldIn, playerIn, stack);
			dragonBreath.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
			worldIn.spawnEntity(dragonBreath);
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, heldItemStack);
	}
}
