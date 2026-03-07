package com.github.alexthe666.iceandfire.core;

//import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLiving;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.pathfinding.Path;
//import net.minecraft.pathfinding.PathPoint;
//import net.minecraft.util.math.Vec3d;
//import net.minecraftforge.client.event.RenderWorldLastEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import org.lwjgl.opengl.GL11;
//
//import java.util.ConcurrentModificationException;

//@Mod.EventBusSubscriber
//@SideOnly(Side.CLIENT)
//public class PathVisualiser {
//    @SubscribeEvent
//    public static void onRender(RenderWorldLastEvent event) {
////        try {
////            Field f = DebugRenderer.class.getDeclaredField("field_190080_f");
////            f.setAccessible(true);
////            f.setBoolean(Minecraft.getMinecraft().debugRenderer, true);
////            return;
////        } catch (ReflectiveOperationException e) {
////            e.printStackTrace();
////        }
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferBuilder = tessellator.getBuffer();
//        float partialTicks = event.getPartialTicks();
//        EntityPlayer player = Minecraft.getMinecraft().player;
//        bufferBuilder.setTranslation(-interpolate(player.lastTickPosX, player.posX, partialTicks), -interpolate(player.lastTickPosY, player.posY, partialTicks), -interpolate(player.lastTickPosZ, player.posZ, partialTicks));
//
//        GlStateManager.disableTexture2D();
//        GlStateManager.disableDepth();
//        GlStateManager.shadeModel(GL11.GL_SMOOTH);
//        GL11.glEnable(GL11.GL_LINE_SMOOTH);
//        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
//        GlStateManager.glLineWidth(4.0F);
//
//        try {
//            for (Entity entity : Minecraft.getMinecraft().getIntegratedServer().getEntityWorld().loadedEntityList) {
//                if (!(entity instanceof EntityMyrmexBase))
//                    continue;
//                Path path = ((EntityLiving) entity).getNavigator().getPath();
//                if (path == null)
//                    continue;
//
//                bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
//                Vec3d v0 = new Vec3d(interpolate(entity.lastTickPosX, entity.posX, partialTicks), interpolate(entity.lastTickPosY, entity.posY, partialTicks) + 0.25, interpolate(entity.lastTickPosZ, entity.posZ, partialTicks));
//                for (int i = path.getCurrentPathIndex(); i < path.getCurrentPathLength(); i++) {
//                    PathPoint pathPoint = path.getPathPointFromIndex(i);
//                    Vec3d v1 = new Vec3d(pathPoint.x + 0.5, pathPoint.y + 0.25, pathPoint.z + 0.5);
//                    bufferBuilder.pos(v0.x, v0.y, v0.z);
//                    bufferBuilder.color(1.0F, 1.0F, 1.0F, 1.0F);
//                    bufferBuilder.endVertex();
//                    bufferBuilder.pos(v1.x, v1.y, v1.z);
//                    bufferBuilder.color(1.0F, 1.0F, 1.0F, 1.0F);
//                    bufferBuilder.endVertex();
//                    v0 = v1;
//                }
//                tessellator.draw();
//            }
//        } catch (ConcurrentModificationException e) {
//            // ignore
//        }
//
//        GlStateManager.enableDepth();
//        GlStateManager.enableTexture2D();
//
//        bufferBuilder.setTranslation(0.0D, 0.0D, 0.0D);
//    }
//
//    static double interpolate(double previous, double current, double partialTicks) {
//        return previous + (current - previous) * partialTicks;
//    }
//}
