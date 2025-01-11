package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.capability.entityeffect.EntityEffectCapability;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIRestrictSunFlying;
import com.github.alexthe666.iceandfire.entity.ai.GhostAICharge;
import com.github.alexthe666.iceandfire.entity.ai.GhostPathNavigator;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IHumanoid;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.List;


public class EntityGhost extends EntityMob implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHumanoid, IBlacklistedFromStatues {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "ghost"));
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityGhost.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> CHARGING = EntityDataManager.createKey(EntityGhost.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DAYTIME_MODE = EntityDataManager.createKey(EntityGhost.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WAS_FROM_CHEST = EntityDataManager.createKey(EntityGhost.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DAYTIME_COUNTER = EntityDataManager.createKey(EntityGhost.class, DataSerializers.VARINT);
    public static Animation ANIMATION_SCARE;
    public static Animation ANIMATION_HIT;
    private int animationTick;
    private Animation currentAnimation;


    public EntityGhost(World worldIn) {
        super(worldIn);
        ANIMATION_SCARE = Animation.create(30);
        ANIMATION_HIT = Animation.create(10);
        this.moveHelper = new MoveHelper(this);
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        if (this.wasFromChest()) {
            return 0;
        } else {
            return super.getExperiencePoints(player);
        }
    }

    protected ResourceLocation getLootTable() {
        return this.wasFromChest() ? LootTableList.EMPTY : LOOT;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.GHOST_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSoundRegistry.GHOST_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.GHOST_DIE;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFireConfig.ENTITY_SETTINGS.ghostMaxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFireConfig.ENTITY_SETTINGS.ghostAttackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1D);
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        return potioneffectIn.getPotion() != MobEffects.POISON && potioneffectIn.getPotion() != MobEffects.WITHER && super.isPotionApplicable(potioneffectIn);
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        return super.isEntityInvulnerable(source) || source.isFireDamage() || source == DamageSource.IN_WALL || source == DamageSource.CACTUS
                || source == DamageSource.DROWN || source == DamageSource.FALLING_BLOCK || source == DamageSource.ANVIL;
    }

    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new GhostPathNavigator(this, worldIn);
    }

    public boolean isCharging() {
        return this.dataManager.get(CHARGING);
    }

    public void setCharging(boolean moving) {
        this.dataManager.set(CHARGING, moving);
    }

    public boolean isDaytimeMode() {
        return this.dataManager.get(IS_DAYTIME_MODE);
    }

    public void setDaytimeMode(boolean moving) {
        this.dataManager.set(IS_DAYTIME_MODE, moving);
    }

    public boolean wasFromChest() {
        return this.dataManager.get(WAS_FROM_CHEST);
    }

    public void setFromChest(boolean moving) {
        this.dataManager.set(WAS_FROM_CHEST, moving);
    }


    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSunFlying(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new GhostAICharge(this));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(5, new EntityAIWanderAvoidWaterFlying(this, 0.6D) {
            public boolean shouldExecute() {
                executionChance = 60;
                return super.shouldExecute();
            }
        });
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 10, false, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity != null && !entity.isDead;
            }
        }));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 10, false, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof EntityLivingBase && DragonUtils.isAlive((EntityLivingBase) entity) && DragonUtils.isVillager(entity);
            }
        }));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.noClip = true;
        if (!world.isRemote) {
            boolean day = isInDaylight() && !this.wasFromChest();
            if (day) {
                if (!this.isDaytimeMode()) {
                    this.setAnimation(ANIMATION_SCARE);
                }
                this.setDaytimeMode(true);
            } else {
                this.setDaytimeMode(false);
                this.setDaytimeCounter(0);
            }
            if (isDaytimeMode()) {
                this.setMoveForward(0);
                this.setMoveVertical(0);
                this.setMoveStrafing(0);
                this.setDaytimeCounter(this.getDaytimeCounter() + 1);
                if (getDaytimeCounter() >= 100) {
                    this.setInvisible(true);
                }
            } else {
                this.setInvisible(this.isPotionActive(MobEffects.INVISIBILITY));
                this.setDaytimeCounter(0);
                if (isAIDisabled())
                    this.setNoAI(false);
            }
        }
        if (!world.isRemote && !EntityGorgon.isStoneMob(this)) {
            updateGhost();
        }
        if (this.getAnimation() == ANIMATION_HIT && this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) < 1.4D && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.GHOST_ATTACK, this.getSoundVolume(), this.getSoundPitch());
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public void updateGhost() {
        if (this.ticksExisted % 20 == 0) {
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(50, 12, 50));
            for (EntityLivingBase entity : entities) {
                IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(entity);
                if (capability != null && !capability.isSpooked()) {
                    capability.setSpooked(20, this.getEntityId());
                }
            }
        }
    }

    public boolean isAIDisabled() {
        return this.isDaytimeMode() || super.isAIDisabled();
    }

    public boolean isSilent() {
        return this.isDaytimeMode() || super.isSilent();
    }

    protected boolean isInDaylight() {
        if (this.world.isDaytime() && !this.world.isRemote) {
            float f = this.getBrightness();
            BlockPos blockpos = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double) Math.round(this.posY + 4), this.posZ);
            return f > 0.5F && this.world.canSeeSky(blockpos);
        }

        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!itemstack.isEmpty() && itemstack.getItem() == IafItemRegistry.manuscript && !this.isHauntedShoppingList()) {
            this.setColor(-1);
            this.playSound(IafSoundRegistry.BESTIARY_PAGE, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.isDaytimeMode()) {
            super.travel(0, 0, 0);
            return;
        }
        super.travel(strafe, vertical, forward);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setColor(this.rand.nextInt(3));
        if (rand.nextInt(200) == 0) {
            this.setColor(-1);
        }

        return livingdata;
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(COLOR, 0);
        this.getDataManager().register(CHARGING, false);
        this.getDataManager().register(IS_DAYTIME_MODE, false);
        this.getDataManager().register(WAS_FROM_CHEST, false);
        this.getDataManager().register(DAYTIME_COUNTER, 0);
    }

    public int getColor() {
        return MathHelper.clamp(this.getDataManager().get(COLOR), -1, 2);
    }

    public void setColor(int color) {
        this.getDataManager().set(COLOR, color);
    }

    public int getDaytimeCounter() {
        return this.getDataManager().get(DAYTIME_COUNTER);
    }

    public void setDaytimeCounter(int counter) {
        this.getDataManager().set(DAYTIME_COUNTER, counter);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setColor(compound.getInteger("Color"));
        this.setDaytimeMode(compound.getBoolean("DaytimeMode"));
        this.setDaytimeCounter(compound.getInteger("DaytimeCounter"));
        this.setFromChest(compound.getBoolean("FromChest"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Color", this.getColor());
        compound.setBoolean("DaytimeMode", this.isDaytimeMode());
        compound.setInteger("DaytimeCounter", this.getDaytimeCounter());
        compound.setBoolean("FromChest", this.wasFromChest());
    }

    public boolean isHauntedShoppingList() {
        return this.getColor() == -1;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_SCARE, ANIMATION_HIT};
    }


    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return false;
    }

    class MoveHelper extends EntityMoveHelper {
        EntityGhost ghost;

        public MoveHelper(EntityGhost ghost) {
            super(ghost);
            this.ghost = ghost;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO) {
                Vec3d vec3d = new Vec3d(this.getX() - ghost.posX, this.getY() - ghost.posY, this.getZ() - ghost.posZ);
                double d0 = vec3d.length();
                double edgeLength = ghost.getEntityBoundingBox().getAverageEdgeLength();
                if (d0 < edgeLength) {
                    this.action = Action.WAIT;
                    ghost.motionX *= 0.5D;
                    ghost.motionY *= 0.5D;
                    ghost.motionZ *= 0.5D;
                } else {
                    ghost.motionX += vec3d.scale(this.speed * 0.5D * 0.05D / d0).x;
                    ghost.motionY += vec3d.scale(this.speed * 0.5D * 0.05D / d0).y;
                    ghost.motionZ += vec3d.scale(this.speed * 0.5D * 0.05D / d0).z;
                    if (ghost.getAttackTarget() == null) {
                        Vec3d vec3d1 = new Vec3d(ghost.motionX, ghost.motionY, ghost.motionZ);
                        ghost.rotationYaw = -((float) MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float) Math.PI);
                        ghost.renderYawOffset = ghost.rotationYaw;
                    } else {
                        double d4 = ghost.getAttackTarget().posX - ghost.posX;
                        double d5 = ghost.getAttackTarget().posZ - ghost.posZ;
                        ghost.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        ghost.renderYawOffset = ghost.rotationYaw;
                    }
                }
            }
        }
    }
}
