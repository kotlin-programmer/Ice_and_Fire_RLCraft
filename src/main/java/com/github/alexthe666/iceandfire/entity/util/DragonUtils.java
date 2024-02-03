package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.entity.*;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DragonUtils {

	public static void destroyBlock(World world, BlockPos pos, IBlockState state) {
		if (world.isRemote) {
			return;
		}

		Block block = state.getBlock();
		if (block.isAir(state, world, pos)) {
			return;
		}

		Integer effectChance = IceAndFireConfig.getDragonGriefingEffectChance().get(block);
		if (effectChance == null || world.rand.nextInt(100) < effectChance) {
			world.playEvent(2001, pos, Block.getStateId(state));
		}

		Integer blockChance = IceAndFireConfig.getDragonGriefingBlockChance().get(block);
		if (blockChance == null || world.rand.nextInt(100) < blockChance) {
			block.dropBlockAsItem(world, pos, state, 0);
		}

		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
	}

	public static BlockPos getBlockInView(EntityDragonBase dragon) {
		float radius = 0.75F * (0.7F * dragon.getRenderSize() / 3) * - 7 - dragon.getRNG().nextInt(dragon.getDragonStage() * 6);
		float neg = dragon.getRNG().nextBoolean() ? 1 : -1;
		float renderYawOffset = dragon.renderYawOffset;
		if(dragon.hasHomePosition && dragon.homePos != null){
			BlockPos dragonPos = new BlockPos(dragon);
			BlockPos ground = dragon.world.getHeight(dragonPos);
			int distFromGround = (int) dragon.posY - ground.getY();
			for(int i = 0; i < 10; i++){
				BlockPos pos = new BlockPos(dragon.homePos.getX() + dragon.getRNG().nextInt(IceAndFireConfig.DRAGON_SETTINGS.dragonWanderFromHomeDistance) - IceAndFireConfig.DRAGON_SETTINGS.dragonWanderFromHomeDistance, (distFromGround > 16 ? (int) Math.min(IceAndFireConfig.DRAGON_SETTINGS.maxDragonFlight, dragon.posY + dragon.getRNG().nextInt(16) - 8) : (int) dragon.posY + dragon.getRNG().nextInt(16) + 1), (dragon.homePos.getZ() + dragon.getRNG().nextInt(IceAndFireConfig.DRAGON_SETTINGS.dragonWanderFromHomeDistance * 2) - IceAndFireConfig.DRAGON_SETTINGS.dragonWanderFromHomeDistance));
				if (!dragon.isTargetBlocked(new Vec3d(pos)) && dragon.getDistanceSqToCenter(pos) > 6) {
					return pos;
				}
			}
		}
		float angle = (0.01745329251F * renderYawOffset) + 3.15F + (dragon.getRNG().nextFloat() * neg);

		double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
		double extraZ = radius * MathHelper.cos(angle);
		BlockPos radialPos = new BlockPos(dragon.posX + extraX, 0, dragon.posZ + extraZ);
		BlockPos ground = dragon.world.getHeight(radialPos);
		int distFromGround = (int) dragon.posY - ground.getY();
		BlockPos newPos = radialPos.up(distFromGround > 16 ? (int) Math.min(IceAndFireConfig.DRAGON_SETTINGS.maxDragonFlight, dragon.posY + dragon.getRNG().nextInt(16) - 8) : (int) dragon.posY + dragon.getRNG().nextInt(16) + 1);
		if (!dragon.isTargetBlocked(new Vec3d(newPos)) && dragon.getDistanceSqToCenter(newPos) > 6) {
			return newPos;
		}
		return null;
	}

	public static BlockPos getWaterBlockInView(EntityDragonBase dragon) {
		float radius = 0.75F * (0.7F * dragon.getRenderSize() / 3) * - 7 - dragon.getRNG().nextInt(dragon.getDragonStage() * 6);
		float neg = dragon.getRNG().nextBoolean() ? 1 : -1;
		float angle = (0.01745329251F * dragon.renderYawOffset) + 3.15F + (dragon.getRNG().nextFloat() * neg);
		double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
		double extraZ = radius * MathHelper.cos(angle);
		BlockPos radialPos = new BlockPos(dragon.posX + extraX, 0, dragon.posZ + extraZ);
		BlockPos ground = dragon.world.getHeight(radialPos);
		int distFromGround = (int) dragon.posY - ground.getY();
		BlockPos newPos = radialPos.up(distFromGround > 16 ? (int) Math.min(IceAndFireConfig.DRAGON_SETTINGS.maxDragonFlight, dragon.posY + dragon.getRNG().nextInt(16) - 8) : (int) dragon.posY + dragon.getRNG().nextInt(16) + 1);
		BlockPos surface = dragon.world.getBlockState(newPos.down(2)).getMaterial() != Material.WATER ? newPos.down(dragon.getRNG().nextInt(10) + 1) : newPos;
		if ( dragon.getDistanceSqToCenter(surface) > 6 && dragon.world.getBlockState(surface).getMaterial() == Material.WATER) {
			return surface;
		}
		return null;
	}

	public static EntityLivingBase riderLookingAtEntity(EntityLivingBase dragon, EntityLivingBase rider, double dist) {
		Vec3d vec3d = rider.getPositionEyes(1.0F);
		Vec3d vec3d1 = rider.getLook(1.0F);
		Vec3d vec3d2 = vec3d.add(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist);
		double d1 = dist;
		Entity pointedEntity = null;
		List<Entity> list = rider.world.getEntitiesInAABBexcluding(rider, rider.getEntityBoundingBox().expand(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
			public boolean apply(@Nullable Entity entity) {
				return entity != null && entity.canBeCollidedWith() && entity instanceof EntityLivingBase && !entity.isEntityEqual(dragon) && !entity.isOnSameTeam(dragon) &&  (!(entity instanceof IDeadMob) || !((IDeadMob) entity).isMobDead());
			}
		}));
		double d2 = d1;
		for (Entity entity : list) {
			AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow((double) entity.getCollisionBorderSize() + 2F);
			RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

			if (axisalignedbb.contains(vec3d)) {
				if (d2 >= 0.0D) {
					pointedEntity = entity;
					d2 = 0.0D;
				}
			} else if (raytraceresult != null) {
				double d3 = vec3d.distanceTo(raytraceresult.hitVec);

				if (d3 < d2 || d2 == 0.0D) {
					if (entity.getLowestRidingEntity() == rider.getLowestRidingEntity() && !rider.canRiderInteract()) {
						if (d2 == 0.0D) {
							pointedEntity = entity;
						}
					} else {
						pointedEntity = entity;
						d2 = d3;
					}
				}
			}
		}
		return (EntityLivingBase) pointedEntity;
	}

	public static BlockPos getBlockInViewHippogryph(EntityHippogryph hippo) {
		float radius = 0.75F * (0.7F * 8) * -3 - hippo.getRNG().nextInt(48);
		float neg = hippo.getRNG().nextBoolean() ? 1 : -1;
		float angle = (0.01745329251F * hippo.renderYawOffset) + 3.15F + (hippo.getRNG().nextFloat() * neg);
		double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
		double extraZ = radius * MathHelper.cos(angle);
		if(hippo.hasHomePosition && hippo.homePos != null){
			BlockPos dragonPos = new BlockPos(hippo);
			BlockPos ground = hippo.world.getHeight(dragonPos);
			int distFromGround = (int) hippo.posY - ground.getY();
			for(int i = 0; i < 10; i++){
				BlockPos pos = new BlockPos(hippo.homePos.getX() + hippo.getRNG().nextInt(IceAndFireConfig.DRAGON_SETTINGS.dragonWanderFromHomeDistance) - IceAndFireConfig.DRAGON_SETTINGS.dragonWanderFromHomeDistance, (distFromGround > 16 ? (int) Math.min(IceAndFireConfig.DRAGON_SETTINGS.maxDragonFlight, hippo.posY + hippo.getRNG().nextInt(16) - 8) : (int) hippo.posY + hippo.getRNG().nextInt(16) + 1), (hippo.homePos.getZ() + hippo.getRNG().nextInt(IceAndFireConfig.DRAGON_SETTINGS.dragonWanderFromHomeDistance * 2) - IceAndFireConfig.DRAGON_SETTINGS.dragonWanderFromHomeDistance));
				if (!hippo.isTargetBlocked(new Vec3d(pos)) && hippo.getDistanceSqToCenter(pos) > 6) {
					return pos;
				}
			}
		}
		BlockPos radialPos = new BlockPos(hippo.posX + extraX, 0, hippo.posZ + extraZ);
		BlockPos ground = hippo.world.getHeight(radialPos);
		int distFromGround = (int) hippo.posY - ground.getY();
		BlockPos newPos = radialPos.up(distFromGround > 16 ? (int) Math.min(IceAndFireConfig.DRAGON_SETTINGS.maxDragonFlight, hippo.posY + hippo.getRNG().nextInt(16) - 8) : (int) hippo.posY + hippo.getRNG().nextInt(16) + 1);
		if (!hippo.isTargetBlocked(new Vec3d(newPos)) && hippo.getDistanceSqToCenter(newPos) > 6) {
			return newPos;
		}
		return null;
	}

	public static BlockPos getBlockInViewStymphalian(EntityStymphalianBird bird) {
		float radius = 0.75F * (0.7F * 6) * -3 - bird.getRNG().nextInt(24);
		float neg = bird.getRNG().nextBoolean() ? 1 : -1;
		float renderYawOffset = bird.flock != null && !bird.flock.isLeader(bird) ? getStymphalianFlockDirection(bird) : bird.renderYawOffset;
		float angle = (0.01745329251F * renderYawOffset) + 3.15F + (bird.getRNG().nextFloat() * neg);
		double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
		double extraZ = radius * MathHelper.cos(angle);
		BlockPos radialPos = getStymphalianFearPos(bird, new BlockPos(bird.posX + extraX, 0, bird.posZ + extraZ));
		BlockPos ground = bird.world.getHeight(radialPos);
		int distFromGround = (int) bird.posY - ground.getY();
		int flightHeight = Math.min(IceAndFireConfig.ENTITY_SETTINGS.stymphalianBirdFlightHeight, bird.flock != null && !bird.flock.isLeader(bird) ? ground.getY() + bird.getRNG().nextInt(16): ground.getY() + bird.getRNG().nextInt(16));
		BlockPos newPos = radialPos.up(distFromGround > 16 ? flightHeight : (int) bird.posY + bird.getRNG().nextInt(16) + 1);
		if (!bird.isTargetBlocked(new Vec3d(newPos)) && bird.getDistanceSqToCenter(newPos) > 6) {
			return newPos;
		}
		return null;
	}

	private static BlockPos getStymphalianFearPos(EntityStymphalianBird bird, BlockPos fallback){
		if(bird.getVictor() != null && bird.getVictor() instanceof EntityCreature){
			Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom((EntityCreature)bird.getVictor(), 16, IceAndFireConfig.ENTITY_SETTINGS.stymphalianBirdFlightHeight, new Vec3d(bird.getVictor().posX, bird.getVictor().posY, bird.getVictor().posZ));
			if(vec3d != null){
				BlockPos pos = new BlockPos(vec3d);
				return new BlockPos(pos.getX(), 0, pos.getZ());
			}
		}
		return fallback;
	}

	private static float getStymphalianFlockDirection(EntityStymphalianBird bird){
		EntityStymphalianBird leader = bird.flock.getLeader();
		if(bird.getDistanceSq(leader) > 2){
			double d0 = leader.posX - bird.posX;
			double d2 = leader.posZ - bird.posZ;
			float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
			float degrees = MathHelper.wrapDegrees(f - bird.rotationYaw);

			return bird.rotationYaw + degrees;
		}else{
			return leader.renderYawOffset;
		}
	}

	public static BlockPos getBlockInTargetsViewCockatrice(EntityCockatrice cockatrice, EntityLivingBase target) {
		float radius = 10 + cockatrice.getRNG().nextInt(10);
		float angle = (0.01745329251F * target.rotationYawHead);
		double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
		double extraZ = radius * MathHelper.cos(angle);
		BlockPos radialPos = new BlockPos(target.posX + extraX, 0, target.posZ + extraZ);
		BlockPos ground = target.world.getHeight(radialPos);
		if (!cockatrice.isTargetBlocked(new Vec3d(ground)) && cockatrice.getDistanceSqToCenter(ground) > 30) {
			return ground;
		}
		return target.getPosition();
	}

	public static boolean canTameDragonAttack(EntityTameable dragon, Entity entity){
		String className = entity.getClass().getSimpleName();
		if(className.contains("VillagerMCA") || className.contains("MillVillager") || className.contains("Citizen")){
			return false;
		}
		if(entity instanceof EntityVillager || entity instanceof EntityGolem || entity instanceof EntityPlayer){
			return false;
		}
		if(entity instanceof EntityTameable){
			return !((EntityTameable) entity).isTamed();
		}
		return true;
	}

	public static boolean isVillager(Entity entity){
		String className = entity.getClass().getSimpleName();
		return entity instanceof INpc || className.contains("VillagerMCA") || className.contains("MillVillager") || className.contains("Citizen");
	}

	public static boolean isAnimaniaMob(Entity entity){
		String className = entity.getClass().getCanonicalName().toLowerCase();
		return className.contains("animania");
	}

	public static boolean isLivestock(Entity entity){
		String className = entity.getClass().getSimpleName();
		return entity instanceof EntityCow || entity instanceof EntitySheep || entity instanceof EntityPig || entity instanceof EntityChicken
				|| entity instanceof EntityRabbit || entity instanceof AbstractHorse
				|| className.contains("Cow") || className.contains("Sheep") || className.contains("Pig") || className.contains("Chicken")
				|| className.contains("Rabbit") || className.contains("Peacock") || className.contains("Goat") || className.contains("Ferret")
				|| className.contains("Hedgehog") || className.contains("Peahen") || className.contains("Peafowl") || className.contains("Sow")
				|| className.contains("Hog");
	}

	public static boolean canDragonBreak(Block block){
		return block != net.minecraft.init.Blocks.BARRIER &&
				block != net.minecraft.init.Blocks.OBSIDIAN &&
				block != net.minecraft.init.Blocks.END_STONE &&
				block != net.minecraft.init.Blocks.BEDROCK &&
				block != net.minecraft.init.Blocks.END_PORTAL &&
				block != net.minecraft.init.Blocks.END_PORTAL_FRAME &&
				block != net.minecraft.init.Blocks.COMMAND_BLOCK &&
				block != net.minecraft.init.Blocks.REPEATING_COMMAND_BLOCK &&
				block != net.minecraft.init.Blocks.CHAIN_COMMAND_BLOCK &&
				block != net.minecraft.init.Blocks.IRON_BARS &&
				block != net.minecraft.init.Blocks.END_GATEWAY;
	}

	public static boolean hasSameOwner(Entity entity1, Entity entity2) {
		if (!(entity1 instanceof IEntityOwnable && entity2 instanceof IEntityOwnable)) {
			return false;
		}
		Entity owner = ((IEntityOwnable) entity1).getOwner();
		Entity owner2 = ((IEntityOwnable) entity2).getOwner();
		if (owner == null || owner2 == null) {
			return false;
		}
		return owner.equals(owner2);
	}

	public static boolean isOwner(Entity owner, Entity entity) {
		if (!(entity instanceof IEntityOwnable)) {
			return false;
		}
		Entity owner2 = ((IEntityOwnable) entity).getOwner();
		if (owner == null || owner2 == null) {
			return false;
		}
		return owner.isEntityEqual(((IEntityOwnable) entity).getOwner());
	}

    public static boolean isAlive(EntityLivingBase entity) {
		if (!entity.isEntityAlive()) {
			return false;
		}
		if (entity instanceof IDeadMob && ((IDeadMob) entity).isMobDead()) {
			return false;
		}
		if (!entity.attackable()) {
			return false;
		}
		if (entity instanceof EntityLiving && ((EntityLiving)entity).isAIDisabled()) {
			return false;
		}
		return entity instanceof EntityLiving || entity instanceof EntityPlayer;
	}

	public static boolean canGrief(boolean weak) {
		if (weak) {
			return IceAndFireConfig.DRAGON_SETTINGS.dragonGriefing == 0;
		}
		return IceAndFireConfig.DRAGON_SETTINGS.dragonGriefing < 2;
	}

	public static EntityEquipmentSlot getEquipmentSlotFromDragonInvSlot(int slot) {
		switch(slot) {
			default:
				return EntityEquipmentSlot.HEAD;
			case 1:
				return EntityEquipmentSlot.CHEST;
			case 2:
				return EntityEquipmentSlot.LEGS;
			case 3:
				return EntityEquipmentSlot.FEET;
		}
	}

	public static int getDragonInvSlotFromEquipmentSlot(EntityEquipmentSlot slot) {
		switch(slot) {
			default:
				return 0;
			case CHEST:
				return 1;
			case LEGS:
				return 2;
			case FEET:
				return 3;
		}
	}

	public static boolean canDismount(Entity entity) {
		if (entity == null) {
			return true;
		}
		if (entity.isDead || (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0.0f)) {
			return true;
		}
		if (entity instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase) entity;
			return dragon.canDismount();
		} else if (entity instanceof EntityCyclops) {
			EntityCyclops cyclops = (EntityCyclops) entity;
			return cyclops.canDismount();
		}
		return true;
	}

	public static boolean canHostilesTarget(Entity entity) {
		if (entity instanceof EntityPlayer && entity.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			return false;
		} else {
			return entity instanceof EntityLivingBase && isAlive((EntityLivingBase) entity);
		}
	}

	public static BlockPos getBlockInTargetsViewGhost(EntityGhost ghost, EntityLivingBase target) {
		float radius = 4 + ghost.getRNG().nextInt(5);
		float angle = (0.01745329251F * (target.rotationYawHead + 90F + ghost.getRNG().nextInt(180)));
		double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
		double extraZ = radius * MathHelper.cos(angle);
		BlockPos radialPos = new BlockPos(target.posX + extraX, target.posY, target.posZ + extraZ);
		BlockPos ground = radialPos;
		if (ghost.getDistanceSq(ground) > 30) {
			return ground;
		}
		return ghost.getPosition();
	}
}
