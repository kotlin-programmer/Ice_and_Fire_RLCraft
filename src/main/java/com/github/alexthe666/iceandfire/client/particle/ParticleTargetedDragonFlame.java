package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import scala.collection.parallel.ParSeqLike;
import thaumcraft.codechicken.lib.vec.Vector3;

import javax.annotation.Nullable;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.util.List;

public class ParticleTargetedDragonFlame extends ParticleFlame {
    private final float dragonSize;
    private final Vector3d initial;
    private final Vector3d target;
    private int touchedTime = 0;
    private final float speedBonus;
    @Nullable
    private final EntityDragonBase dragon;

    @SideOnly(Side.CLIENT)
    public ParticleTargetedDragonFlame(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, EntityDragonBase entityDragonBase) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0D, 0D, 0D);
        this.particleMaxAge = 30;
        this.dragon = entityDragonBase;
        this.initial = new Vector3d(xCoordIn, yCoordIn, zCoordIn);
        this.target = new Vector3d(
                dragon.burnParticleX + (double) ((this.rand.nextFloat() - this.rand.nextFloat())) * 3.5F,
                dragon.burnParticleY + (double) ((this.rand.nextFloat() - this.rand.nextFloat())) * 3.5F,
                dragon.burnParticleZ + (double) ((this.rand.nextFloat() - this.rand.nextFloat())) * 3.5F
        );
        this.speedBonus = rand.nextFloat() * 0.015F;
        this.dragonSize = MathHelper.clamp(entityDragonBase.getRenderSize() * 0.08F, 0.55F, 3F);
        this.setPosition(xCoordIn, yCoordIn, zCoordIn);
    }


    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (particleAge > (dragon == null ? 10 : 30)) {
            this.setExpired();
        }
        particleScale = 2F * dragonSize + 3F * dragonSize * Math.min(this.distance(initial) / this.distance(target), 1.0F);
        float f = (float) this.particleTextureIndexX / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = (float) this.particleTextureIndexY / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * this.particleScale;

        if (this.particleTexture != null) {
            f = this.particleTexture.getMinU();
            f1 = this.particleTexture.getMaxU();
            f2 = this.particleTexture.getMinV();
            f3 = this.particleTexture.getMaxV();
        }

        float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
        float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[]{new Vec3d(-rotationX * f4 - rotationXY * f4, -rotationZ * f4, -rotationYZ * f4 - rotationXZ * f4), new Vec3d((double) (-rotationX * f4 + rotationXY * f4), rotationZ * f4, -rotationYZ * f4 + rotationXZ * f4), new Vec3d(rotationX * f4 + rotationXY * f4, rotationZ * f4, rotationYZ * f4 + rotationXZ * f4), new Vec3d((double) (rotationX * f4 - rotationXY * f4), -rotationZ * f4, rotationYZ * f4 - rotationXZ * f4)};
        if (this.particleAngle != 0.0F) {
            float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
            float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
            float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
            Vec3d vec3d = new Vec3d(f10, f11, f12);

            for (int l = 0; l < 4; ++l) {
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
            }
        }
        GL11.glPushMatrix();
        buffer.pos((double) f5 + avec3d[0].x, (double) f6 + avec3d[0].y, (double) f7 + avec3d[0].z).tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double) f5 + avec3d[1].x, (double) f6 + avec3d[1].y, (double) f7 + avec3d[1].z).tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double) f5 + avec3d[2].x, (double) f6 + avec3d[2].y, (double) f7 + avec3d[2].z).tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double) f5 + avec3d[3].x, (double) f6 + avec3d[3].y, (double) f7 + avec3d[3].z).tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        GL11.glPopMatrix();
    }

    public int getBrightnessForRender(float partialTick) {
        return 240;
    }

    public void onUpdate() {
        super.onUpdate();

        if (dragon == null) {
            float distX = (float) (this.initial.x - this.posX);
            float distZ = (float) (this.initial.z - this.posZ);
            this.motionX += distX * -0.01F * dragonSize * rand.nextFloat();
            this.motionZ += distZ * -0.01F * dragonSize * rand.nextFloat();
            this.motionY += 0.015F * rand.nextFloat();
        } else {
            double d2 = this.target.x - initial.x;
            double d3 = this.target.y - initial.y;
            double d4 = this.target.z - initial.z;
            float speed = 0.015F + speedBonus;
            this.motionX += d2 * speed;
            this.motionY += d3 * speed;
            this.motionZ += d4 * speed;
            if (touchedTime > 3) {
                this.setExpired();
            }
        }
    }

    public void move(double x, double y, double z) {
        double d0 = y;
        double origX = x;
        double origZ = z;

        if (this.canCollide) {
            List<AxisAlignedBB> list = this.world.getCollisionBoxes(null, this.getBoundingBox().expand(x, y, z));

            for (AxisAlignedBB axisalignedbb : list) {
                y = axisalignedbb.calculateYOffset(this.getBoundingBox(), y);
            }

            this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));

            for (AxisAlignedBB axisalignedbb1 : list) {
                x = axisalignedbb1.calculateXOffset(this.getBoundingBox(), x);
            }

            this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));

            for (AxisAlignedBB axisalignedbb2 : list) {
                z = axisalignedbb2.calculateZOffset(this.getBoundingBox(), z);
            }

            this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));
            if (!list.isEmpty()) {
                touchedTime++;
            }
        } else {
            this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        }

        this.resetPositionToBB();
        this.onGround = d0 != y && d0 < 0.0D;

        if (origX != x) {
            this.motionX = 0.0D;
        }

        if (origZ != z) {
            this.motionZ = 0.0D;
        }
    }

    private float distance(Vector3d vector) {
        double d0 = this.posX - vector.x;
        double d1 = this.posY - vector.y;
        double d2 = this.posZ - vector.z;
        return (float) Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }
}
