package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticle;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class MessageParticleFX extends AbstractMessage<MessageParticleFX> {

	private List<EnumParticle> types;
	private List<Particle> particles;

	public MessageParticleFX(EnumParticle type, List<Particle> particles) {
		this.types = singletonList(type);
		this.particles = particles;
	}

	public MessageParticleFX(List<EnumParticle> types, List<Particle> particles) {
		this.types = types;
		this.particles = particles;
	}

	public MessageParticleFX() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int numTypes = buf.readInt();
		int numParticles = buf.readInt();

		types = new ArrayList<>(numTypes);
		for (int i = 0; i < numTypes; i++) {
			int ordinal = buf.readInt();
			types.add(EnumParticle.values()[ordinal]);
		}
		particles = new ArrayList<>(numParticles);
		for (int i = 0; i < numParticles; i++) {
			particles.add(
					createParticle(
							buf.readDouble(),
							buf.readDouble(),
							buf.readDouble(),
							buf.readDouble(),
							buf.readDouble(),
							buf.readDouble()
					)
			);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(types.size());
		buf.writeInt(particles.size());

		for (EnumParticle type : types) {
			buf.writeInt(type.ordinal());
		}
		for (Particle particle : particles) {
			buf.writeDouble(particle.x);
			buf.writeDouble(particle.y);
			buf.writeDouble(particle.z);
			buf.writeDouble(particle.motX);
			buf.writeDouble(particle.motY);
			buf.writeDouble(particle.motZ);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientReceived(Minecraft client, MessageParticleFX message, EntityPlayer player, MessageContext messageContext) {
		if (particles.isEmpty()) {
			return;
		}
		for (EnumParticle type : types) {
			for (Particle particle : particles) {
				IceAndFire.PROXY.spawnParticle(type, player.world, particle.x, particle.y, particle.z, particle.motX, particle.motY, particle.motZ);
			}
		}
	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageParticleFX message, EntityPlayer player, MessageContext messageContext) {
	}

	public static class Particle {
		public final double x;
		public final double y;
		public final double z;
		public final double motX;
		public final double motY;
		public final double motZ;

		private Particle(double x, double y, double z, double motX, double motY, double motZ) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.motX = motX;
			this.motY = motY;
			this.motZ = motZ;
		}
	}

	public static Particle createParticle(double x, double y, double z, double motX, double motY, double motZ) {
		return new Particle(x, y, z, motX, motY, motZ);
	}

	public static Particle createParticle(BlockPos pos, double motX, double motY, double motZ) {
		return new Particle(pos.getX(), pos.getY(), pos.getZ(), motX, motY, motZ);
	}
}