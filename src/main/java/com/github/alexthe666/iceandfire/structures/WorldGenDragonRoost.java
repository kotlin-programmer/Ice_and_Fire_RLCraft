package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.BlockCoinPile;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
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

public abstract class WorldGenDragonRoost extends WorldGenerator {
    private static boolean isMale;

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        isMale = rand.nextBoolean();

        int dragonAge = 50 + rand.nextInt(25);
        transformGround(worldIn, rand, position, dragonAge / 5);
        generateStructures(worldIn, rand, position, dragonAge / 5);
        generateDragon(worldIn, rand, position, dragonAge);
        return true;
    }

    private void transformGround(World world, Random rand, BlockPos position, int radius) {
        for (int i = 0; radius >= 0 && i < 3; ++i) {
            int j = radius + rand.nextInt(2);
            int k = (radius + rand.nextInt(2));
            int l = radius + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    IBlockState state = world.getBlockState(blockpos);
                    float hardness = state.getBlock().getBlockHardness(state, world, blockpos);
                    if (hardness != -1.0F) {
                        transformState(world, blockpos, state);
                    }
                }
            }
        }
    }

    public void generateStructures(World world, Random rand, BlockPos position, int radius) {
        for (int i = 0; radius >= 0 && i < 3; ++i) {
            int j = radius + rand.nextInt(2);
            int k = (radius + rand.nextInt(2));
            int l = radius + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && world.isAirBlock(blockpos) && isDragonTransformedBlock(world.getBlockState(blockpos.down()).getBlock())) {
                    int chance = rand.nextInt(100);
                    if (chance < 4) {
                        int chance2 = rand.nextInt(20);
                        switch (chance2) {
                            default:
                                generatePile(world, rand, blockpos);
                                break;
                            case 1:
                                generateArchNS(world, rand, blockpos);
                                break;
                            case 2:
                                generateArchEW(world, rand, blockpos);
                                break;
                        }

                    }
                }
            }
        }
    }

    public void generatePile(World world, Random rand, BlockPos position) {
        int height = 1 + rand.nextInt(7);
        int chance = rand.nextInt(100);
        if (chance < 20) {
            world.setBlockState(position, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[rand.nextInt(3)]), 3);
            if (world.getBlockState(position).getBlock() instanceof BlockChest) {
                TileEntity chest = world.getTileEntity(position);
                if (chest instanceof TileEntityChest && !(chest).isInvalid()) {
                    ((TileEntityChest) chest).setLootTable(getLootTable(), rand.nextLong());
                }
            }
        } else {
            world.setBlockState(position, getPileBlock(rand).withProperty(BlockCoinPile.LAYERS, height), 3);
        }
    }

    public void generateArchNS(World world, Random rand, BlockPos position) {
        int height = 3 + rand.nextInt(1);
        int width = 1 + rand.nextInt(2);
        for (int sides = 0; sides < height; sides++) {
            world.setBlockState(position.up(sides).east(width / 2), getBuildingBlock(), 3);
            world.setBlockState(position.up(sides).west(width / 2), getBuildingBlock(), 3);
        }
        for (int way = -1; way < width; way++) {
            world.setBlockState(position.up(height).east(way), getBuildingBlock(), 3);
        }
    }

    public void generateArchEW(World world, Random rand, BlockPos position) {
        int height = 3 + rand.nextInt(1);
        int width = 1 + rand.nextInt(2);
        for (int sides = 0; sides < height; sides++) {
            world.setBlockState(position.up(sides).north(width / 2), getBuildingBlock(), 3);
            world.setBlockState(position.up(sides).south(width / 2), getBuildingBlock(), 3);
        }
        for (int way = 0; way < width; way++) {
            world.setBlockState(position.up(height).south(way), getBuildingBlock(), 3);
        }
    }

    private void generateDragon(World worldIn, Random rand, BlockPos position, int dragonAge) {
        EntityDragonBase dragon = createDragon(worldIn);
        dragon.setGender(isMale);
        dragon.growDragon(dragonAge);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(rand.nextInt(4));
        dragon.setPositionAndRotation(position.getX() + 0.5, worldIn.getHeight(position).getY() + 1.5, position.getZ() + 0.5, worldIn.rand.nextFloat() * 360, 0);
        dragon.homePos = position;
        dragon.hasHomePosition = true;
        dragon.setHunger(50);
        worldIn.spawnEntity(dragon);
    }

    private boolean isDragonTransformedBlock(Block block) {
        for (Block dragonTransformedBlock : getDragonTransformedBlocks()) {
            if (block == dragonTransformedBlock) {
                return true;
            }
        }
        return false;
    }

    protected abstract void transformState(World world, BlockPos blockpos, IBlockState state);
    protected abstract IBlockState getPileBlock(Random rand);
    protected abstract IBlockState getBuildingBlock();
    protected abstract Block[] getDragonTransformedBlocks();
    protected abstract ResourceLocation getLootTable();
    protected abstract EntityDragonBase createDragon(World worldIn);
}
