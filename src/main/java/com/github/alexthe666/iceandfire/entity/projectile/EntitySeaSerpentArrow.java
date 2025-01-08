package com.github.alexthe666.iceandfire.entity.projectile;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.util.ParticleHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySeaSerpentArrow extends EntityArrow {

    public EntitySeaSerpentArrow(World worldIn) {
        super(worldIn);
        this.setDamage(3F);
    }

    public EntitySeaSerpentArrow(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.setDamage(3F);
    }

    public EntitySeaSerpentArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.setDamage(3F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(world.isRemote && !this.inGround){
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            ParticleHelper.spawnParticle(this.world, EnumParticleTypes.WATER_BUBBLE, this.posX  + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d0 * 10.0D, this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * 10.0D, this.posZ + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d2 * 10.0D, d0, d1, d2);
            ParticleHelper.spawnParticle(this.world, EnumParticleTypes.WATER_SPLASH, this.posX  + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d0 * 10.0D, this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * 10.0D, this.posZ + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d2 * 10.0D, d0, d1, d2);
        }
    }

    @Override
    public boolean isInWater(){
        return false;
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.entityHit != null && raytraceResultIn.entityHit instanceof EntityPlayer) {
            this.damageShield((EntityPlayer)raytraceResultIn.entityHit, (float)this.getDamage());
        }
        super.onHit(raytraceResultIn);
    }

    protected void damageShield(EntityPlayer player, float damage) {
        if (damage >= 3.0F && player.getActiveItemStack().getItem().isShield(player.getActiveItemStack(), player)) {
            ItemStack copyBeforeUse = player.getActiveItemStack().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getActiveItemStack().damageItem(i, player);

            if (player.getActiveItemStack().isEmpty()) {
                EnumHand enumhand = player.getActiveHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, enumhand);

                if (enumhand == EnumHand.MAIN_HAND) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                player.resetActiveHand();
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(IafItemRegistry.sea_serpent_arrow);
    }
}
