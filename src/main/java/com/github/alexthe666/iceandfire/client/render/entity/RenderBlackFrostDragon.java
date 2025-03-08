package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBlackFrostDragon extends RenderLiving<EntityDragonBase> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/black_frost.png");
    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/black_frost_eyes.png");

    public RenderBlackFrostDragon(RenderManager renderManager, ModelBase model) {
        super(renderManager, model, 0.8F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
        this.addLayer(new RenderDragonBase.LayerDragonRider(this));
    }

    public boolean shouldRender(EntityDragonBase dragon, ICamera camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected void preRenderCallback(EntityDragonBase entity, float f) {
        this.shadowSize = entity.getRenderSize() / 3;
        GL11.glScalef(shadowSize, shadowSize, shadowSize);
    }


    protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
        return TEXTURE;
    }
}
