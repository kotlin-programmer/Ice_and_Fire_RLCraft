package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.enums.EnumParticle;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouseModel;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieJar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TileEntityJar extends TileEntity implements ITickable {

	private static final float PARTICLE_WIDTH = 0.3F;
	private static final float PARTICLE_HEIGHT = 0.6F;
	public boolean hasPixie;
	public boolean hasProduced;
	public boolean tamedPixie;
	public UUID pixieOwnerUUID;
	public int pixieType;
	public int ticksExisted;
	public float rotationYaw;
	public float prevRotationYaw;

	public TileEntityJar() {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("HasPixie", hasPixie);
		compound.setInteger("PixieType", pixieType);
		compound.setBoolean("HasProduced", hasProduced);
		compound.setBoolean("TamedPixie", tamedPixie);
		if(pixieOwnerUUID != null) compound.setUniqueId("PixieOwnerUUID", pixieOwnerUUID);
		compound.setInteger("TicksExisted", ticksExisted);
		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		handleUpdateTag(packet.getNbtCompound());
		if (!world.isRemote) {
			IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageUpdatePixieHouseModel(pos.toLong(), packet.getNbtCompound().getInteger("PixieType")));
		}
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tagCompound) {
		this.readFromNBT(tagCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		hasPixie = compound.getBoolean("HasPixie");
		pixieType = compound.getInteger("PixieType");
		hasProduced = compound.getBoolean("HasProduced");
		ticksExisted = compound.getInteger("TicksExisted");
		tamedPixie = compound.getBoolean("TamedPixie");
		if (compound.hasKey("PixieOwnerUUID")) {
			pixieOwnerUUID = compound.getUniqueId("PixieOwnerUUID");
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		if (newState.getBlock() != Blocks.AIR) {
			return false;
		}
		return super.shouldRefresh(world, pos, oldState, newState);
	}

	@Override
	public void update() {
		ticksExisted++;
		if(this.hasPixie && this.world.isRemote) IceAndFire.PROXY.spawnParticle(
				EnumParticle.PIXIE_DUST,
				this.world,
				this.pos.getX() + 0.5F + (double)(this.world.rand.nextFloat() * PARTICLE_WIDTH * 2F) - (double) PARTICLE_WIDTH,
				this.pos.getY() + (double)(this.world.rand.nextFloat() * PARTICLE_HEIGHT),
				this.pos.getZ() + 0.5F + (double)(this.world.rand.nextFloat() * PARTICLE_WIDTH * 2F) - (double) PARTICLE_WIDTH,
				EntityPixie.PARTICLE_RGB[this.pixieType][0],
				EntityPixie.PARTICLE_RGB[this.pixieType][1],
				EntityPixie.PARTICLE_RGB[this.pixieType][2]);
		if(this.hasPixie && !this.hasProduced && !this.getWorld().isRemote && ticksExisted % IceAndFireConfig.ENTITY_SETTINGS.pixieCooldown == 0) {
			this.hasProduced = true;
			IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageUpdatePixieJar(pos.toLong(), true));
		}
		prevRotationYaw = rotationYaw;
		if(this.world.rand.nextInt(30) == 0) this.rotationYaw = (this.world.rand.nextFloat() * 360F) - 180F;
		if(this.hasPixie && ticksExisted % 40 == 0 && this.world.rand.nextInt(6) == 0) this.world.playSound(
				this.pos.getX() + 0.5D,
				this.pos.getY() + 0.5D,
				this.pos.getZ() + 0.5,
				IafSoundRegistry.PIXIE_IDLE,
				SoundCategory.NEUTRAL,
				1,
				1,
				false);
	}

	public void releasePixie() {
		if(this.hasPixie) {
			EntityPixie pixie = new EntityPixie(this.world);
			pixie.setPositionAndRotation(
					this.pos.getX() + 0.5F,
					this.pos.getY() + 1F,
					this.pos.getZ() + 0.5F,
					this.world.rand.nextInt(360),
					0);
			pixie.setColor(this.pixieType);
			if(!world.isRemote) world.spawnEntity(pixie);
			this.hasPixie = false;
			this.pixieType = 0;
			pixie.ticksUntilHouseAI = 500;
			pixie.setTamed(this.tamedPixie);
			pixie.setOwnerId(this.pixieOwnerUUID);
			if(!world.isRemote) IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageUpdatePixieHouse(
					pos.toLong(),
					false,
					0));
		}
	}
}