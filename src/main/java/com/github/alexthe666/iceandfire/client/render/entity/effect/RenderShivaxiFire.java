/*
 * Scaling Health
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.alexthe666.iceandfire.client.render.entity.effect;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;

public class RenderShivaxiFire {
    private static final ResourceLocation SHIVAXI_FIRE_TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/shivaxi_fire.png");

    public static void render(RenderLivingEvent.Post<EntityLivingBase> event) {
        EntityLivingBase entity = event.getEntity();

        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        RenderManager renderManager = event.getRenderer().getRenderManager();
        GlStateManager.translate(entity.posX - renderManager.viewerPosX, entity.posY - renderManager.viewerPosY, entity.posZ - renderManager.viewerPosZ);
        float f = entity.width * 1.8F;
        GlStateManager.scale(f, f, f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();

        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = entity.height / f;
        float f4 = (float) (entity.posY - entity.getEntityBoundingBox().minY);

        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, (float) ((int) f3) * 0.02F);

        GlStateManager.color(1f, 1f, 1f, 1f);

        float f5 = 0.0F;
        int i = 0;

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        event.getRenderer().bindTexture(SHIVAXI_FIRE_TEXTURE);

        while (f3 > 0.0F) {
            boolean flag = i % 2 == 0;
            int frame = entity.ticksExisted % 32;
            float minU = flag ? 0.5f : 0.0f;
            float minV = frame / 32f;
            float maxU = flag ? 1.0f : 0.5f;
            float maxV = (frame + 1) / 32f;

            if (flag) {
                float f10 = maxU;
                maxU = minU;
                minU = f10;
            }

            vertexbuffer.pos(f1 - f2, 0.0F - f4, f5).tex(maxU, maxV).endVertex();
            vertexbuffer.pos(-f1 - f2, 0.0F - f4, f5).tex(minU, maxV).endVertex();
            vertexbuffer.pos(-f1 - f2, 1.4F - f4, f5).tex(minU, minV).endVertex();
            vertexbuffer.pos(f1 - f2, 1.4F - f4, f5).tex(maxU, minV).endVertex();

            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 += 0.03F;
            ++i;
        }

        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
}
