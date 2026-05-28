package com.github.alexthe666.iceandfire.entity.projectile;

import com.github.alexthe666.iceandfire.entity.explosion.FireExplosion;
import com.github.alexthe666.iceandfire.entity.explosion.IceExplosion;
import com.github.alexthe666.iceandfire.entity.explosion.LightningExplosion;
import com.github.alexthe666.iceandfire.enums.EnumDragonType;
import com.github.alexthe666.iceandfire.item.ItemDragonBreath;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityDragonBreath extends EntityThrowable
{
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityDragonBreath.class, DataSerializers.ITEM_STACK);

    public EntityDragonBreath(World worldIn) {
        super(worldIn);
    }

    public EntityDragonBreath(World worldIn, EntityLivingBase throwerIn, ItemStack breathIn) {
        super(worldIn, throwerIn);
        this.setItem(breathIn);
        this.ignoreEntity = throwerIn;
    }

    public EntityDragonBreath(World worldIn, double x, double y, double z, ItemStack breathIn) {
        super(worldIn, x, y, z);
        this.setItem(breathIn);
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    public ItemStack getBreath() {
        ItemStack stack = this.getDataManager().get(ITEM);
        if (stack.getItem() instanceof ItemDragonBreath) {
            return stack;
        } else {
            return new ItemStack(Items.AIR);
        }
    }

    public void setItem(ItemStack stack) {
        this.getDataManager().set(ITEM, stack);
        this.getDataManager().setDirty(ITEM);
    }

    @Override
    protected float getGravityVelocity()
    {
        return 0.05F;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            ItemStack stack = this.getBreath();
            if (stack.isEmpty() ) {
                this.setDead();
            } else {
                ItemDragonBreath breath = (ItemDragonBreath) stack.getItem();
                this.createExplosion(breath, this.world.getGameRules().getBoolean("mobGriefing"));
                this.world.playEvent(2002, new BlockPos(this), getEffectColor(breath));
                this.setDead();
            }
        }
    }

    private int getEffectColor(ItemDragonBreath breath) {
        switch (breath.type) {
            case FIRE:
                return 0XFF7636;
            case ICE:
                return 0x4FADEF;
            default:
                return 0xEA98FF;
        }
    }

    private void createExplosion(ItemDragonBreath breath, boolean flag) {
        Explosion explosion;
        if (breath.type == EnumDragonType.ICE) {
           explosion = new IceExplosion(this.world, this.getThrower(), this.posX, this.posY, this.posZ, 2F, flag);
        } else if (breath.type == EnumDragonType.LIGHTNING) {
           explosion = new LightningExplosion(this.world, this.getThrower(), this.posX, this.posY, this.posZ, 2F, flag);
        } else {
           explosion = new FireExplosion(this.world, this.getThrower(), posX, posY, posZ, 2F, flag);
        }
        explosion.doExplosionA();
        explosion.doExplosionB(true);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        ItemStack stack = new ItemStack(compound.getCompoundTag("Breath"));
        if (stack.isEmpty()) {
            this.setDead();
        } else {
            this.setItem(stack);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        ItemStack stack = this.getBreath();
        if (!stack.isEmpty()) {
            compound.setTag("Breath", stack.writeToNBT(new NBTTagCompound()));
        }
    }
}