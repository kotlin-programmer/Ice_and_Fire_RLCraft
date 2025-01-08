package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.client.particle.lightning.ParticleLightningBoltPoint;
import com.github.alexthe666.iceandfire.client.particle.lightning.ParticleLightningRenderer;
import com.github.alexthe666.iceandfire.client.particle.lightning.ParticleLightningSegment;
import com.github.alexthe666.iceandfire.client.particle.lightning.ParticleLightningVector;
import gnu.trove.map.hash.TIntIntHashMap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/*
 * This class was created by <ChickenBones>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
public class ParticleLightning extends Particle {

    final public static int DEFAULT_COLOR_OUTER = 0xA929EE;
    final public static int DEFAULT_COLOR_INNER = 0xFFFFFF;
    final public static float TICKS_PER_METER = 1F;

    public static final int DEFAULT_FADE_TIME = 20;
    private final TIntIntHashMap splitParents = new TIntIntHashMap();
    private final double length;
    private final Random rand;
    private final int colorOuter;
    private final int colorInner;
    private final boolean isProjectile;
    private List<ParticleLightningSegment> segments = new ArrayList<>();
    private int segmentCount = 1;
    private int splitCount;
    final private float speed;

    public ParticleLightning(World world, ParticleLightningVector sourceVec, ParticleLightningVector targetVec, boolean isProjectile) {
        this(world, sourceVec, targetVec, DEFAULT_COLOR_OUTER, DEFAULT_COLOR_INNER, isProjectile);
    }

    public ParticleLightning(World world, ParticleLightningVector sourceVec, ParticleLightningVector targetVec, int colorOuter, int colorInner, boolean isProjectile) {
        super(world, sourceVec.x, sourceVec.y, sourceVec.z);
        rand = new Random(world.rand.nextLong());
        this.speed = TICKS_PER_METER;
        this.colorOuter = colorOuter;
        this.colorInner = colorInner;
        this.isProjectile = isProjectile;
        length = targetVec.subtract(sourceVec).mag();
        if (isProjectile) {
            particleMaxAge = DEFAULT_FADE_TIME / 2;
        } else {
            particleMaxAge = (DEFAULT_FADE_TIME + rand.nextInt(DEFAULT_FADE_TIME)) / 2;
        }
        particleAge = -(int) (length * this.speed);

        segments.add(new ParticleLightningSegment(sourceVec, targetVec));

        fractal(2, length / 1.5, 0.7F, 0.7F, 45);
        fractal(2, length / 4, 0.5F, 0.8F, 50);

        if (!isProjectile) {
            fractal(2, length / 15, 0.5F, 0.9F, 55);
            fractal(2, length / 30, 0.5F, 1.0F, 60);
            fractal(2, length / 60, 0, 0, 0);
        }

        calculateCollisionAndDiffs();

        segments.sort((o1, o2) -> Float.compare(o2.light, o1.light));
    }

    @Override
    public void renderParticle(BufferBuilder wr, Entity entity, float partialTicks, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ) {
        ParticleLightningRenderer.queuedLightningBolts.offer(this);
    }

    public void renderBolt(int pass, boolean inner) {
        BufferBuilder wr = Tessellator.getInstance().getBuffer();

        float boltAge = particleAge < 0 ? 0 : (float) particleAge / (float) particleMaxAge;
        float mainAlpha;
        if(pass == 0)
            mainAlpha = (1 - boltAge) * 0.4F;
        else mainAlpha = 1 - boltAge * 0.5F;

        int expandTime = (int) (length * speed);

        int renderStart = (int) ((expandTime / 2 - particleMaxAge + particleAge) / (float) (expandTime / 2) * segmentCount);
        int renderEnd = (int) ((particleAge + expandTime) / (float) expandTime * segmentCount);

        for(ParticleLightningSegment renderSegment : segments) {
            if(renderSegment.segmentNo < renderStart || renderSegment.segmentNo > renderEnd)
                continue;

            ParticleLightningVector playerVec = getRelativeViewVector(renderSegment.startPoint.point).multiply(-1);

            double width = 0.025F * (playerVec.mag() / 5 + 1) * (1 + renderSegment.light) * 0.5F;

            ParticleLightningVector diff1 = playerVec.crossProduct(renderSegment.prevDiff).normalize().multiply(width / renderSegment.sinPrev);
            ParticleLightningVector diff2 = playerVec.crossProduct(renderSegment.nextDiff).normalize().multiply(width / renderSegment.sinNext);

            ParticleLightningVector startVec = renderSegment.startPoint.point;
            ParticleLightningVector endVec = renderSegment.endPoint.point;

            int color = inner ? colorInner : colorOuter;
            int r = (color & 0xFF0000) >> 16;
            int g = (color & 0xFF00) >> 8;
            int b = color & 0xFF;
            int a = (int) (mainAlpha * renderSegment.light * 0xFF);

            wr.pos(endVec.x - diff2.x, endVec.y - diff2.y, endVec.z - diff2.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
            wr.pos(startVec.x - diff1.x, startVec.y - diff1.y, startVec.z - diff1.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
            wr.pos(startVec.x + diff1.x, startVec.y + diff1.y, startVec.z + diff1.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
            wr.pos(endVec.x + diff2.x, endVec.y + diff2.y, endVec.z + diff2.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();

            if (!isProjectile) {
                if (renderSegment.next == null) {
                    ParticleLightningVector roundEnd = renderSegment.endPoint.point.add(renderSegment.diff.normalize().multiply(width));

                    wr.pos(roundEnd.x - diff2.x, roundEnd.y - diff2.y, roundEnd.z - diff2.z).tex(0, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
                    wr.pos(endVec.x - diff2.x, endVec.y - diff2.y, endVec.z - diff2.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
                    wr.pos(endVec.x + diff2.x, endVec.y + diff2.y, endVec.z + diff2.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
                    wr.pos(roundEnd.x + diff2.x, roundEnd.y + diff2.y, roundEnd.z + diff2.z).tex(0, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
                }

                if (renderSegment.prev == null) {
                    ParticleLightningVector roundEnd = renderSegment.startPoint.point.subtract(renderSegment.diff.normalize().multiply(width));

                    wr.pos(startVec.x - diff1.x, startVec.y - diff1.y, startVec.z - diff1.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
                    wr.pos(roundEnd.x - diff1.x, roundEnd.y - diff1.y, roundEnd.z - diff1.z).tex(0, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
                    wr.pos(roundEnd.x + diff1.x, roundEnd.y + diff1.y, roundEnd.z + diff1.z).tex(0, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
                    wr.pos(startVec.x + diff1.x, startVec.y + diff1.y, startVec.z + diff1.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
                }
            }
        }
    }

    private void fractal(int splits, double amount, double splitChance, double splitLength, double splitAngle) {
        List<ParticleLightningSegment> oldSegments = segments;
        segments = new ArrayList<>();

        ParticleLightningSegment prev;

        for(ParticleLightningSegment segment : oldSegments) {
            prev = segment.prev;

            ParticleLightningVector subsegment = segment.diff.multiply(1F / splits);

            ParticleLightningBoltPoint[] newPoints = new ParticleLightningBoltPoint[splits + 1];

            ParticleLightningVector startPoint = segment.startPoint.point;
            newPoints[0] = segment.startPoint;
            newPoints[splits] = segment.endPoint;

            for(int i = 1; i < splits; i++) {
                ParticleLightningVector randOff = segment.diff.perpendicular().normalize().rotate(rand.nextFloat() * 360, segment.diff);
                randOff = randOff.multiply((rand.nextFloat() - 0.5F) * amount * 2);

                ParticleLightningVector basePoint = startPoint.add(subsegment.multiply(i));

                newPoints[i] = new ParticleLightningBoltPoint(basePoint, randOff);
            }

            for(int i = 0; i < splits; i++) {
                ParticleLightningSegment next = new ParticleLightningSegment(newPoints[i], newPoints[i + 1], segment.light, segment.segmentNo * splits + i, segment.splitNo);
                next.prev = prev;
                if (prev != null)
                    prev.next = next;

                if(i != 0 && rand.nextFloat() < splitChance) {
                    ParticleLightningVector splitrot = next.diff.xCrossProduct().rotate(rand.nextFloat() * 360, next.diff);
                    ParticleLightningVector diff = next.diff.rotate((rand.nextFloat() * 0.66F + 0.33F) * splitAngle, splitrot).multiply(splitLength);

                    splitCount++;
                    splitParents.put(splitCount, next.splitNo);

                    ParticleLightningSegment split = new ParticleLightningSegment(newPoints[i], new ParticleLightningBoltPoint(newPoints[i + 1].basepoint, newPoints[i + 1].offsetvec.add(diff)), segment.light / 2F, next.segmentNo, splitCount);
                    split.prev = prev;

                    segments.add(split);
                }

                prev = next;
                segments.add(next);
            }

            if(segment.next != null)
                segment.next.prev = prev;
        }

        segmentCount *= splits;
    }

    private float rayTraceResistance(ParticleLightningVector start, ParticleLightningVector end, float prevresistance) {
        RayTraceResult mop = world.rayTraceBlocks(start.toVec3D(), end.toVec3D());

        if(mop == null)
            return prevresistance;

        if(mop.typeOfHit == RayTraceResult.Type.BLOCK) {
            Block block = world.getBlockState(mop.getBlockPos()).getBlock();

            if(world.isAirBlock(mop.getBlockPos()))
                return prevresistance;

            return prevresistance + block.getExplosionResistance(null) + 0.3F;
        } else return prevresistance;
    }

    private void calculateCollisionAndDiffs() {
        TIntIntHashMap lastactivesegment = new TIntIntHashMap();

        segments.sort((o1, o2) -> {
            int comp = Integer.compare(o1.splitNo, o2.splitNo);
            if (comp == 0)
                return Integer.compare(o1.segmentNo, o2.segmentNo);
            else return comp;
        });

        int lastSplitCalc = 0;
        int lastActiveSegment = 0;// unterminated
        float splitResistance = 0;

        for(ParticleLightningSegment segment : segments) {
            if(segment.splitNo > lastSplitCalc) {
                lastactivesegment.put(lastSplitCalc, lastActiveSegment);
                lastSplitCalc = segment.splitNo;
                lastActiveSegment = lastactivesegment.get(splitParents.get(segment.splitNo));
                splitResistance = lastActiveSegment < segment.segmentNo ? 50 : 0;
            }

            if(splitResistance >= 40 * segment.light)
                continue;

            splitResistance = rayTraceResistance(segment.startPoint.point, segment.endPoint.point, splitResistance);
            lastActiveSegment = segment.segmentNo;
        }
        lastactivesegment.put(lastSplitCalc, lastActiveSegment);

        lastSplitCalc = 0;
        lastActiveSegment = lastactivesegment.get(0);
        for(Iterator<ParticleLightningSegment> iterator = segments.iterator(); iterator.hasNext();) {
            ParticleLightningSegment segment = iterator.next();
            if(lastSplitCalc != segment.splitNo) {
                lastSplitCalc = segment.splitNo;
                lastActiveSegment = lastactivesegment.get(segment.splitNo);
            }

            if(segment.segmentNo > lastActiveSegment)
                iterator.remove();
            segment.calcEndDiffs();
        }
    }

    private static ParticleLightningVector getRelativeViewVector(ParticleLightningVector pos) {
        Entity renderEntity = Minecraft.getMinecraft().getRenderViewEntity();
        return new ParticleLightningVector((float) renderEntity.posX - pos.x, (float) renderEntity.posY + renderEntity.getEyeHeight() - pos.y, (float) renderEntity.posZ - pos.z);
    }

}
