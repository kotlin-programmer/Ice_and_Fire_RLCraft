package com.github.alexthe666.iceandfire.integration.baubles.client.model.layer;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.common.Config;
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
        if(!Config.renderBaubles || player.getActivePotionEffect(MobEffects.INVISIBILITY) != null) return;

        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();

        GlStateManager.pushMatrix();
        renderLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();
    }

    protected void renderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale){
        if(!this.shouldRenderInSlot(player, EntityEquipmentSlot.HEAD)) return;
        if(BaublesApi.isBaubleEquipped(player, IafItemRegistry.blindfold) == -1 && BaublesApi.isBaubleEquipped(player, IafItemRegistry.earplugs) == -1) return;

        ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(BaubleType.HEAD.getValidSlots()[0]);
        if(!this.shouldItemStackRender(player, stack)) return;
        else if(stack.getItem() == IafItemRegistry.blindfold) Minecraft.getMinecraft().getTextureManager().bindTexture(BLINDFOLD);
        else if(stack.getItem() == IafItemRegistry.earplugs) Minecraft.getMinecraft().getTextureManager().bindTexture(EAR_PLUGS);

        if(player.isSneaking()) GlStateManager.translate(0, 0.2F, 0);
        modelPlayer.bipedHead.postRender(scale);
        new ModelHeadBauble().render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    // https://github.com/fonnymunkey/RLArtifacts/blob/1.12/src/main/java/artifacts/common/util/RenderHelper.java
    public boolean shouldRenderInSlot(EntityPlayer player, EntityEquipmentSlot slot) {
        ItemStack stack = player.getItemStackFromSlot(slot);
        return stack.isEmpty() ||
                (stack.getTagCompound() != null &&
                        stack.getTagCompound().getBoolean("classy_hat_invisible") &&
                        stack.getTagCompound().getCompoundTag("classy_hat_disguise").isEmpty());
    }

    public boolean shouldItemStackRender(EntityPlayer player, ItemStack stack) {
        return stack.getTagCompound() == null || !stack.getTagCompound().getBoolean("phantom_thread_invisible");
    }
}
