package com.github.alexthe666.iceandfire.integration.baubles.client.model.layer;

import baubles.api.BaublesApi;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.integration.baubles.client.model.ModelHeadBauble;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class LayerHeadBauble implements LayerRenderer<EntityPlayer> {
    private static final ResourceLocation BLINDFOLD = new ResourceLocation(IceAndFire.MODID, "textures/models/armor/blindfold_layer_1.png");
    private static final ResourceLocation EAR_PLUGS = new ResourceLocation(IceAndFire.MODID, "textures/models/armor/earplugs_layer_1.png");
    protected RenderPlayer renderPlayer;
    protected ModelPlayer modelPlayer;
    protected boolean slim;

    public LayerHeadBauble(RenderPlayer renderPlayer) {
        this(renderPlayer, false);
    }

    public LayerHeadBauble(RenderPlayer renderPlayer, boolean slim) {
        this.renderPlayer = renderPlayer;
        this.modelPlayer = renderPlayer.getMainModel();
        this.slim = slim;
    }

    @Override
    public final void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!shouldRenderBaubles() || player.getActivePotionEffect(MobEffects.INVISIBILITY) != null) return;

        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();

        GlStateManager.pushMatrix();
        renderLayer(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();
    }

    protected void renderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale){
        if (!this.shouldRenderInSlot(player, EntityEquipmentSlot.HEAD)) return;
        else if (BaublesApi.isBaubleEquipped(player, IafItemRegistry.blindfold) != -1) Minecraft.getMinecraft().getTextureManager().bindTexture(BLINDFOLD);
        else if (BaublesApi.isBaubleEquipped(player, IafItemRegistry.earplugs) != -1) Minecraft.getMinecraft().getTextureManager().bindTexture(EAR_PLUGS);
        else return;

        if(player.isSneaking()) GlStateManager.translate(0, 0.2F, 0);
        modelPlayer.bipedHead.postRender(scale);
        new ModelHeadBauble().render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private boolean shouldRenderInSlot(EntityPlayer player, EntityEquipmentSlot slot) {
        ItemStack stack = player.getItemStackFromSlot(slot);
        return stack.isEmpty() ||
                (stack.getTagCompound() != null &&
                        stack.getTagCompound().getBoolean("classy_hat_invisible") &&
                        stack.getTagCompound().getCompoundTag("classy_hat_disguise").isEmpty());
    }

    private boolean shouldRenderBaubles() {
        try {
            return baubles.common.Config.renderBaubles;
        } catch (NoClassDefFoundError e) {
            return true;
        }
    }
}
