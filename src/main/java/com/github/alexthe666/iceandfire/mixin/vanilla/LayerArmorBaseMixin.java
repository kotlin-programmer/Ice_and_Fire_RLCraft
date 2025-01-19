package com.github.alexthe666.iceandfire.mixin.vanilla;

import com.github.alexthe666.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class LayerArmorBaseMixin {
	
	@Inject(
			method = "renderEnchantedGlint",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", shift = At.Shift.BEFORE)
	)
	private static void iceAndFire_vanillaLayerArmorBase_renderEnchantedGlint_pre(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_, CallbackInfo ci) {
		((IEntityLivingBaseRenderContext)p_188364_1_).iceAndFire$setGlintContext(true);
	}
	
	@Inject(
			method = "renderEnchantedGlint",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", shift = At.Shift.AFTER)
	)
	private static void iceAndFire_vanillaLayerArmorBase_renderEnchantedGlint_post(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_, CallbackInfo ci) {
		((IEntityLivingBaseRenderContext)p_188364_1_).iceAndFire$setGlintContext(false);
	}
}