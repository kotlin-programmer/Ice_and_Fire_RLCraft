package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.BlockDreadBase;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import com.github.alexthe666.iceandfire.entity.EntityDreadScuttler;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.Random;

public class MausoleumProcessor implements ITemplateProcessor {

    public static final ResourceLocation DREAD_CHEST_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "mausoleum_chest"));

    public MausoleumProcessor() {
    }

    @Nullable
    @Override
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        if (blockInfoIn.blockState.getBlock() == IafBlockRegistry.dread_stone_bricks) {
            Random rand = new Random(worldIn.getSeed() + pos.toLong());
            IBlockState state = blockInfoIn.blockState;
            if (!blockInfoIn.blockState.getValue(BlockDreadBase.PLAYER_PLACED)) {
                state = getRandomCrackedBlock(rand);
            } else if (rand.nextInt() % 5 == 0) {
                state = Blocks.AIR.getDefaultState();
            }
            return new Template.BlockInfo(pos, state, null);
        } else if (blockInfoIn.blockState.getBlock() == IafBlockRegistry.dragon_bone_block) {
            Random rand = new Random(worldIn.getSeed() + pos.toLong());
            IBlockState state = blockInfoIn.blockState;
            if (rand.nextInt() % 5 == 0) {
                state = Blocks.AIR.getDefaultState();
            }
            return new Template.BlockInfo(pos, state, null);
        } else if (blockInfoIn.blockState.getBlock() instanceof BlockChest) {
            ResourceLocation loot = DREAD_CHEST_LOOT;
            Random rand = new Random(worldIn.getSeed() + pos.toLong());
            NBTTagCompound tag = blockInfoIn.tileentityData == null ? new NBTTagCompound() : blockInfoIn.tileentityData;
            tag.setString("LootTable", loot.toString());
            tag.setLong("LootTableSeed", rand.nextLong());
            return new Template.BlockInfo(pos, Blocks.CHEST.getDefaultState(), tag);
        } else if (blockInfoIn.blockState.getBlock() == IafBlockRegistry.dread_spawner) {
            NBTTagCompound tag = blockInfoIn.tileentityData == null ? new NBTTagCompound() : blockInfoIn.tileentityData;
            NBTTagCompound spawnData = new NBTTagCompound();
            Random rand = new Random(worldIn.getSeed() + pos.toLong());
            ResourceLocation spawnerMobId = EntityList.getKey(getRandomMobForMobSpawner(rand));
            if (spawnerMobId != null) {
                spawnData.setString("id", spawnerMobId.toString());
                tag.removeTag("SpawnPotentials");
                tag.setTag("SpawnData", spawnData.copy());
            }
            return new Template.BlockInfo(pos, IafBlockRegistry.dread_spawner.getDefaultState(), tag);

        }
        return blockInfoIn;
    }

    private Class<? extends Entity> getRandomMobForMobSpawner(Random random) {
        float rand = random.nextFloat();
        if (rand < 0.3D) {
            return EntityDreadThrall.class;
        } else if (rand < 0.5D) {
            return EntityDreadGhoul.class;
        } else if (rand < 0.7D) {
            return EntityDreadBeast.class;
        } else if (rand < 0.85D) {
            return EntityDreadScuttler.class;
        }
        return EntityDreadKnight.class;
    }

    public static IBlockState getRandomCrackedBlock(Random random) {
        float rand = random.nextFloat();
        if (rand < 0.5) {
            return IafBlockRegistry.dread_stone_bricks.getDefaultState();
        } else if (rand < 0.9) {
            return IafBlockRegistry.dread_stone_bricks_cracked.getDefaultState();
        } else {
            return IafBlockRegistry.dread_stone_bricks_mossy.getDefaultState();
        }
    }
}
