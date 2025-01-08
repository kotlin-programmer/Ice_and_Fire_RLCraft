package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.block.BlockEggInIce;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.effect.EntityLightningBolt;

import java.util.Objects;

public enum EnumDragonType {

    FIRE("fire") {

        @Override
        public void updateEggCondition(EntityDragonEgg egg) {
            BlockPos pos = new BlockPos(egg);
            if(!meetsEggCondition(egg, pos)) return;
            egg.setDragonAge(egg.getDragonAge() + 1);
            if(egg.getDragonAge() > IceAndFireConfig.DRAGON_SETTINGS.dragonEggTime) {
                egg.world.setBlockToAir(pos);
                EntityFireDragon dragon = new EntityFireDragon(egg.world);
                if(egg.hasCustomName()) dragon.setCustomNameTag(egg.getCustomNameTag());
                dragon.setVariant(egg.getType().meta % 4);
                dragon.setGender(egg.world.rand.nextBoolean());
                dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                dragon.setHunger(50);
                if(!egg.world.isRemote) egg.world.spawnEntity(dragon);
                dragon.setTamed(true);
                dragon.setOwnerId(egg.getOwnerId());
                egg.world.playSound(egg.posX, egg.posY + egg.getEyeHeight(), egg.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, egg.getSoundCategory(), 2.5F, 1.0F, false);
                egg.world.playSound(egg.posX, egg.posY + egg.getEyeHeight(), egg.posZ, IafSoundRegistry.DRAGON_HATCH, egg.getSoundCategory(), 2.5F, 1.0F, false);
                egg.setDead();
            }
        }

        @Override
        public boolean meetsEggCondition(EntityDragonEgg egg, BlockPos pos) {
            return egg.world.getBlockState(pos).getMaterial() == Material.FIRE;
        }
    },
    ICE("ice", true) {

        @Override
        public void updateEggCondition(EntityDragonEgg egg) {
            BlockPos pos = new BlockPos(egg);
            if(!meetsEggCondition(egg, pos)) return;
            egg.setDead();
            egg.world.setBlockState(pos, IafBlockRegistry.eggInIce.getDefaultState());
            egg.world.playSound(egg.posX, egg.posY + egg.getEyeHeight(), egg.posZ, SoundEvents.BLOCK_GLASS_BREAK, egg.getSoundCategory(), 2.5F, 1.0F, false);
            if(egg.world.getBlockState(pos).getBlock() instanceof BlockEggInIce) {
                ((TileEntityEggInIce)Objects.requireNonNull(egg.world.getTileEntity(pos))).type = egg.getType();
                ((TileEntityEggInIce)Objects.requireNonNull(egg.world.getTileEntity(pos))).ownerUUID = egg.getOwnerId();
            }
        }

        @Override
        public boolean meetsEggCondition(EntityDragonEgg egg, BlockPos pos) {
            return egg.world.getBlockState(pos).getMaterial() == Material.WATER && egg.world.rand.nextInt(500) == 0;
        }
    },
    LIGHTNING("lightning") {

        @Override
        public void updateEggCondition(EntityDragonEgg egg) {
            BlockPos pos = new BlockPos(egg);
            if(!meetsEggCondition(egg, pos)) return;
            egg.setDragonAge(egg.getDragonAge() + 1);
            if(egg.getDragonAge() > IceAndFireConfig.DRAGON_SETTINGS.dragonEggTime) {
                egg.world.setBlockToAir(pos);
                EntityLightningDragon dragon = new EntityLightningDragon(egg.world);
                if(egg.hasCustomName()) dragon.setCustomNameTag(egg.getCustomNameTag());
                dragon.setVariant(egg.getType().meta % 4);
                dragon.setGender(egg.world.rand.nextBoolean());
                dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                dragon.setHunger(50);
                if(!egg.world.isRemote) egg.world.spawnEntity(dragon);
                dragon.setTamed(true);
                dragon.setOwnerId(egg.getOwnerId());
                if(!egg.world.isRemote) {
                    EntityLightningBolt lightningBolt = new EntityLightningBolt(egg.world, egg.posX, egg.posY, egg.posZ, true);
                    egg.world.spawnEntity(lightningBolt);
                }
                egg.world.playSound(egg.posX, egg.posY + egg.getEyeHeight(), egg.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, egg.getSoundCategory(), 2.5F, 1.0F, false);
                egg.world.playSound(egg.posX, egg.posY + egg.getEyeHeight(), egg.posZ, IafSoundRegistry.DRAGON_HATCH, egg.getSoundCategory(), 2.5F, 1.0F, false);
                egg.setDead();
            }
        }

        @Override
        public boolean meetsEggCondition(EntityDragonEgg egg, BlockPos pos) {
            return egg.world.isRainingAt(pos) || egg.world.isRainingAt(pos.add(0, egg.height, 0));
        }
    };

    private final String name;
    private final boolean piscivore;

    EnumDragonType(String name) {
        this(name, false);
    }

    EnumDragonType(String name, boolean piscivore) {
        this.name = name;
        this.piscivore = piscivore;
    }

    public String getName() {
        return this.name;
    }

    public boolean isPiscivore() {
        return this.piscivore;
    }

    public void updateEggCondition(EntityDragonEgg egg) {
        //noop
    }

    public boolean meetsEggCondition(EntityDragonEgg egg, BlockPos pos) {
        //noop
        return false;
    }
}