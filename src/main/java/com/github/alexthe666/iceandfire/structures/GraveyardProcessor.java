package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.BlockGhostChest;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.Random;

public class GraveyardProcessor implements ITemplateProcessor {

    private float integrity = 1.0F;
    public static final ResourceLocation DREAD_CHEST_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "graveyard_chest"));

    public GraveyardProcessor() {
    }

    @Nullable
    @Override
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        if (worldIn.rand.nextFloat() <= integrity) {
            if (blockInfoIn.blockState.getBlock() instanceof BlockGhostChest) {
                ResourceLocation loot = DREAD_CHEST_LOOT;
                Random rand = new Random(worldIn.getSeed() + pos.toLong());
                NBTTagCompound tag = blockInfoIn.tileentityData == null ? new NBTTagCompound() : blockInfoIn.tileentityData;
                tag.setString("LootTable", loot.toString());
                tag.setLong("LootTableSeed", rand.nextLong());
                Template.BlockInfo newInfo = new Template.BlockInfo(pos, ModBlocks.ghost_chest.getDefaultState(), tag);
                return newInfo;
            }
            return blockInfoIn;
        }
        return blockInfoIn;

    }
}
