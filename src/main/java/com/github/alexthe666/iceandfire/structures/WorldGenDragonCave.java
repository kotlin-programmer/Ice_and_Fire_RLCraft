package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.block.BlockCoinPile;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public abstract class WorldGenDragonCave extends WorldGenerator {

    protected boolean isMale;

    public void setGoldPile(World world, Random rand, BlockPos pos) {
        int chance = rand.nextInt(100);
        if (world.getBlockState(pos).getBlock() instanceof BlockChest) {
            return;
        }

        if (chance < 60) {
            boolean generateGold = IceAndFireConfig.WORLDGEN.dragonDenGoldAmount <= 1 || rand.nextInt(IceAndFireConfig.WORLDGEN.dragonDenGoldAmount) == 0;
            world.setBlockState(pos, generateGold ? getPile().withProperty(BlockCoinPile.LAYERS, 1 + rand.nextInt(7)) : Blocks.AIR.getDefaultState(), 3);
        } else if (chance == 60) {
            world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[rand.nextInt(3)]), 3);
            if (world.getBlockState(pos).getBlock() instanceof BlockChest) {
                TileEntity chest = world.getTileEntity(pos);
                if (chest instanceof TileEntityChest && !(chest).isInvalid()) {
                    ((TileEntityChest) chest).setLootTable(getLootTable(), rand.nextLong());
                }
            }
        }
    }

    public void setOres(World world, Random rand, BlockPos pos) {
        float hardness = world.getBlockState(pos).getBlock().getBlockHardness(world.getBlockState(pos), world, pos);
        if(hardness == -1.0F || world.isAirBlock(pos)){
            return;
        }
        boolean isOre = rand.nextInt(IceAndFireConfig.WORLDGEN.oreToStoneRatioForDragonCaves + 1) == 0;
        if (isOre) {
            int chance = world.rand.nextInt(200);
            if (chance < 30) {
                world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState(), 3);
            } else if (chance < 40) {
                world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState(), 3);
            } else if (chance < 45) {
                world.setBlockState(pos, IceAndFireConfig.WORLDGEN.generateCopperOre ? ModBlocks.copperOre.getDefaultState() : getStone(), 3);
            } else if (chance < 50) {
                world.setBlockState(pos, IceAndFireConfig.WORLDGEN.generateSilverOre ? ModBlocks.silverOre.getDefaultState() : getStone(), 3);
            } else if (chance < 60) {
                world.setBlockState(pos, Blocks.COAL_ORE.getDefaultState(), 3);
            } else if (chance < 70) {
                world.setBlockState(pos, Blocks.REDSTONE_ORE.getDefaultState(), 3);
            } else if (chance < 80) {
                world.setBlockState(pos, Blocks.LAPIS_ORE.getDefaultState(), 3);
            } else if (chance < 90) {
                world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState(), 3);
            } else {
                world.setBlockState(pos, getGemstone(), 3);
            }
        } else {
            int chance = rand.nextInt(2);
            if (chance == 0) {
                world.setBlockState(pos, getStone(), 3);
            } else {
                world.setBlockState(pos, getCobblestone(), 3);
            }
        }
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        isMale = rand.nextBoolean();
        int dragonAge = 75 + rand.nextInt(50);
        int i1 = dragonAge / 4;
        int i2 = i1 - 2;
        int ySize = rand.nextInt(2);
        for (int i = 0; i < 3; i++) {
            int j = i1 + rand.nextInt(2);
            int k = i1 / 2 + ySize;
            int l = i1 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest) && worldIn.getBlockState(position).getBlock().getBlockHardness(worldIn.getBlockState(position), worldIn, position) >= 0) {
                        worldIn.setBlockState(blockpos, Blocks.STONE.getDefaultState(), 3);
                    }
                }
            }
        }
        for (int i = 0; i2 >= 0 && i < 3; ++i) {
            int j = i2 + rand.nextInt(2);
            int k = i2 / 2 + ySize;
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest)) {
                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
        for (int i = 0; i2 >= 0 && i < 3; ++i) {
            int j = i2 + rand.nextInt(2);
            int k = (i2 + rand.nextInt(2));
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                float hardness = worldIn.getBlockState(position).getBlock().getBlockHardness(worldIn.getBlockState(position), worldIn, position);
                if (blockpos.distanceSq(position) <= (double) (f * f) && hardness >= 0) {
                    this.setOres(worldIn, rand, blockpos);
                }
            }
        }
        for (int i = 0; i2 >= 0 && i < 3; ++i) {
            int j = i2 + rand.nextInt(2);
            int k = (i2 + rand.nextInt(2)) / 2;
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && worldIn.getBlockState(blockpos.down()).getMaterial() == Material.ROCK && worldIn.getBlockState(blockpos).getMaterial() != Material.ROCK) {
                    this.setGoldPile(worldIn, rand, blockpos);
                }
            }
        }
        EntityDragonBase dragon = createDragon(worldIn);
        dragon.setGender(isMale);
        dragon.growDragon(dragonAge);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(rand.nextInt(4));
        dragon.setPositionAndRotation(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        dragon.setSleeping(true);
        dragon.setHunger(50);
        worldIn.spawnEntity(dragon);
        return true;
    }

    protected abstract IBlockState getStone();
    protected abstract IBlockState getCobblestone();
    protected abstract IBlockState getPile();
    protected abstract IBlockState getGemstone();
    protected abstract ResourceLocation getLootTable();
    protected abstract EntityDragonBase createDragon(World worldIn);
}
