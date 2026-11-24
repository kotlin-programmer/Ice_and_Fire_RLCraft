package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class MessageParticleVanillaFX extends AbstractMessage<MessageParticleVanillaFX> {

	private boolean longDistance;
	private List<EnumParticleTypes> types;
	private List<Particle> particles;

	public MessageParticleVanillaFX(List<EnumParticleTypes> types, boolean longDistance, List<Particle> particles) {
		this.types = types;
		this.longDistance = longDistance;
		this.particles = particles;
	}

	public MessageParticleVanillaFX() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		longDistance = buf.readBoolean();

		int numTypes = buf.readInt();
		int numParticles = buf.readInt();

		types = new ArrayList<>();
		for (int i = 0; i < numTypes; i++) {
			int ordinal = buf.readInt();
			if (ordinal >= 0 && ordinal < EnumParticleTypes.values().length) {
				types.add(EnumParticleTypes.values()[ordinal]);
			}
		}
		particles = new ArrayList<>();
		for (int i = 0; i < numParticles; i++) {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			double motX = buf.readDouble();
			double motY = buf.readDouble();
			double motZ = buf.readDouble();
			int[] parameters = new int[Math.max(0, Math.min(buf.readInt(), buf.readableBytes() / 4))];
			for (int j = 0; j < parameters.length; j++) {
				parameters[j] = buf.readInt();
			}
			particles.add(createParticle(x, y, z, motX, motY, motZ, parameters));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(longDistance);

		buf.writeInt(types.size());
		buf.writeInt(particles.size());

		for (EnumParticleTypes type : types) {
			buf.writeInt(type.ordinal());
		}
		for (Particle particle : particles) {
			buf.writeDouble(particle.x);
			buf.writeDouble(particle.y);
			buf.writeDouble(particle.z);
			buf.writeDouble(particle.motX);
			buf.writeDouble(particle.motY);
			buf.writeDouble(particle.motZ);
			buf.writeInt(particle.parameters.length);
			for (int i = 0; i < particle.parameters.length; i++) {
				buf.writeInt(particle.parameters[i]);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientReceived(Minecraft client, MessageParticleVanillaFX message, EntityPlayer player, MessageContext messageContext) {
		if (particles.isEmpty()) {
			return;
		}
		for (EnumParticleTypes type : types) {
			for (Particle particle : particles) {
				player.world.spawnParticle(type, longDistance, particle.x, particle.y, particle.z, particle.motX, particle.motY, particle.motZ, particle.parameters);
			}
		}
	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageParticleVanillaFX message, EntityPlayer player, MessageContext messageContext) {}

	public static class Particle {
		public final double x;
		public final double y;
		public final double z;
		public final double motX;
		public final double motY;
		public final double motZ;
		public int[] parameters;
		private Particle(double x, double y, double z, double motX, double motY, double motZ, int... parameters) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.motX = motX;
			this.motY = motY;
			this.motZ = motZ;
			this.parameters = parameters;
		}
	}

	public static Particle createParticle(double x, double y, double z, double motX, double motY, double motZ, int... parameters) {
		return new Particle(x, y, z, motX, motY, motZ, parameters);
	}
}