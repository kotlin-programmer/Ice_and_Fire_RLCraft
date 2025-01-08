package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAlchemySword extends ItemSword implements IHitEffect {

	protected final ToolMaterial toolMaterial;

	public ItemAlchemySword(ToolMaterial toolmaterial, String gameName, String name) {
		super(toolmaterial);
		this.toolMaterial = toolmaterial;
		this.setTranslationKey(name);
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if(!CompatLoadUtil.isRLCombatLoaded()) this.doHitEffect(target, attacker);
		return super.hitEntity(stack, target, attacker);
	}

	@Override
	public ToolMaterial getMaterial() { return this.toolMaterial; }

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (this == IafItemRegistry.dragonbone_sword_fire) {
			tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("dragon_sword_fire.hurt1"));
			tooltip.add(TextFormatting.DARK_RED + StatCollector.translateToLocal("dragon_sword_fire.hurt2"));
		}
		if (this == IafItemRegistry.dragonbone_sword_ice) {
			tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("dragon_sword_ice.hurt1"));
			tooltip.add(TextFormatting.AQUA + StatCollector.translateToLocal("dragon_sword_ice.hurt2"));
		}
		if (this == IafItemRegistry.dragonbone_sword_lightning) {
			tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("dragon_sword_lightning.hurt1"));
			tooltip.add(TextFormatting.AQUA + StatCollector.translateToLocal("dragon_sword_lightning.hurt2"));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}