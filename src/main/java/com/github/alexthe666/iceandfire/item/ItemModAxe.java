package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

public class ItemModAxe extends ItemAxe implements IHitEffect {

	public ItemModAxe(ToolMaterial toolmaterial, String gameName, String name) {
		super(toolmaterial, toolmaterial == ModItems.boneTools ? 8 : 6, -3);
		this.setTranslationKey(name);
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		ItemStack mat = this.toolMaterial.getRepairItemStack();
		if(this.toolMaterial == ModItems.silverTools) {
			NonNullList<ItemStack> silverItems = OreDictionary.getOres("ingotSilver");
			for (ItemStack ingot : silverItems){
				if (OreDictionary.itemMatches(repair, ingot, false)) {
					return true;
				}
			}
		}
		if (this.toolMaterial == ModItems.copperTools) {
			NonNullList<ItemStack> copperItems = OreDictionary.getOres("ingotCopper");
			for (ItemStack ingot : copperItems) {
				if (OreDictionary.itemMatches(repair, ingot, false)) {
					return true;
				}
			}
		}
		if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
		return super.getIsRepairable(toRepair, repair);
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
		if (this == ModItems.silver_axe) {
			tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("silvertools.hurt"));
		}
		if (this == ModItems.myrmex_desert_axe || this == ModItems.myrmex_jungle_axe) {
			tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("myrmextools.hurt"));
		}
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
		return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker){
		return true;
	}
}