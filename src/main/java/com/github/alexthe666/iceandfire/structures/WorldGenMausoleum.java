package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IDreadBlock;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class WorldGenMausoleum extends WorldGenerator {

    public EnumFacing facing;
    private static final ResourceLocation STRUCTURE = new ResourceLocation(IceAndFire.MODID, "dread_mausoleum");

    public WorldGenMausoleum(EnumFacing facing) {
        super(false);
        this.facing = facing;
    }

    public static boolean isPartOfMausoleum(IBlockState state) {
        return state.getBlock() instanceof IDreadBlock;
    }

    public static Rotation getRotationFromFacing(EnumFacing facing) {
        switch (facing) {
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }

    public static BlockPos getGround(BlockPos pos, World world) {
        return getGround(pos.getX(), pos.getZ(), world);
    }

    public static BlockPos getGround(int x, int z, World world) {
        BlockPos skyPos = new BlockPos(x, world.getHeight(), z);
        while ((!world.getBlockState(skyPos).isOpaqueCube() || canHeightSkipBlock(skyPos, world)) && skyPos.getY() > 1) {
            skyPos = skyPos.down();
        }
        return skyPos;
    }

    private static boolean canHeightSkipBlock(BlockPos pos, World world) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockLog || state.getBlock() instanceof BlockLeaves || state.getBlock() instanceof BlockLiquid;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        BlockPos height = getGround(position, worldIn);
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(worldIn.getMinecraftServer(), STRUCTURE);
        PlacementSettings settings = new PlacementSettings().setRotation(getRotationFromFacing(facing));
        BlockPos pos = height.offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2);
        if (checkIfCanGenAt(worldIn, pos, template.getSize().getX(), template.getSize().getZ(), facing)) {
            template.addBlocksToWorld(worldIn, pos, new MausoleumProcessor(), settings, 2);
            for (BlockPos underPos : BlockPos.getAllInBox(
                    height.down().offset(facing, (-template.getSize().getZ() / 2) + 1).offset(facing.rotateYCCW(), -template.getSize().getX() / 2),
                    height.down(3).offset(facing, (template.getSize().getZ() / 2) - 1).offset(facing.rotateYCCW(), template.getSize().getX() / 2)
            )) {
                worldIn.setBlockState(underPos, IafBlockRegistry.dread_stone_tile.getDefaultState(), 2);
            }
        }
        return true;
    }

    public boolean checkIfCanGenAt(World world, BlockPos middle, int x, int z, EnumFacing facing) {
        IBlockState corner1 = world.getBlockState(middle.offset(facing, z / 2).down());
        IBlockState corner2 = world.getBlockState(middle.offset(facing.rotateY(), x / 2).down());
        IBlockState corner3 = world.getBlockState(middle.offset(facing.getOpposite(), z / 2).down());
        IBlockState corner4 = world.getBlockState(middle.offset(facing.rotateYCCW(), x / 2).down());
        if (isPartOfMausoleum(corner1)
                || isPartOfMausoleum(corner2)
                || isPartOfMausoleum(corner3)
                || isPartOfMausoleum(corner4)) {
            return false;
        }
       return corner1.isOpaqueCube()
               && corner2.isOpaqueCube()
               && corner3.isOpaqueCube()
               && corner4.isOpaqueCube();
    }
}