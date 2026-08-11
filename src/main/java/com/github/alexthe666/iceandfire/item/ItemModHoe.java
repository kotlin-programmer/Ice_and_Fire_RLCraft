package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.integration.CompatLoadUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

public class ItemModHoe extends ItemHoe implements IHitEffect {

	public ItemModHoe(ToolMaterial toolmaterial, String gameName, String name) {
		super(toolmaterial);
		this.setTranslationKey(name);
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		ItemStack mat = this.toolMaterial.getRepairItemStack();
		if (this.toolMaterial == IafItemRegistry.silverTools) {
			NonNullList<ItemStack> silverItems = OreDictionary.getOres("ingotSilver");
			for (ItemStack ingot : silverItems){
				if (OreDictionary.itemMatches(repair, ingot, false)){
					return true;
				}
			}
		}
		if(this.toolMaterial == IafItemRegistry.copperTools) {
			NonNullList<ItemStack> copperItems = OreDictionary.getOres("ingotCopper");
			for (ItemStack ingot : copperItems){
				if (OreDictionary.itemMatches(repair, ingot, false)){
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
		if (this == IafItemRegistry.silver_hoe) {
			tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("silvertools.hurt"));
		}
		if (this == IafItemRegistry.myrmex_desert_hoe || this == IafItemRegistry.myrmex_jungle_hoe) {
			tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("myrmextools.hurt"));
		}
	}
}