package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.client.StatCollector;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockDragonScalesPile extends BlockCoinPile {

    private final TextFormatting color;
    private final String colorName;
    private final ItemBlock itemBlock;

    public BlockDragonScalesPile(String colorName, TextFormatting color, Item scale) {
        super("dragonscales_" + colorName + "_", scale);
        this.setTranslationKey("iceandfire.dragonscales_pile");
        this.color = color;
        this.colorName = colorName;
        this.itemBlock = new ItemBlock(this);
        this.itemBlock.setRegistryName(this.getRegistryName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(color + StatCollector.translateToLocal("dragon." + colorName));
    }

    public ItemBlock getItemBlock() {
        return itemBlock;
    }
}