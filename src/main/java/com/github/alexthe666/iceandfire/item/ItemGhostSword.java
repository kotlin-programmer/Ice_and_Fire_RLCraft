package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.List;

public class ItemGhostSword extends ItemSword {

    public ItemGhostSword() {
        super(ModItems.ghost_sword_tool_material);
        this.setTranslationKey("iceandfire.ghost_sword");
        this.setCreativeTab(IceAndFire.TAB);
        this.setRegistryName(IceAndFire.MODID, "ghost_sword");
    }

    public static void spawnGhostSwordEntity(ItemStack stack, EntityPlayer playerEntity) {
        if (playerEntity.getCooldownTracker().hasCooldown(stack.getItem()))
            return;
        if (playerEntity.getHeldItem(EnumHand.MAIN_HAND) != stack)
            return;
        final Multimap<String, AttributeModifier> dmg = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        double totalDmg = 0D;
        for (AttributeModifier modifier : dmg.get(String.valueOf(SharedMonsterAttributes.ATTACK_DAMAGE))) {
            totalDmg += modifier.getAmount();
        }
        playerEntity.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
        EntityGhostSword shot = new EntityGhostSword(playerEntity.world, playerEntity, totalDmg * 0.5F);
        Vec3d vector3d = playerEntity.getLook(1.0F);
        Vector3f vector3f = new Vector3f((float) vector3d.x, (float) vector3d.y, (float) vector3d.z);
        shot.shoot(vector3f.x, vector3f.y, vector3f.z, 1.0F, 0.5F);
        playerEntity.world.spawnEntity(shot);
        stack.damageItem(1, playerEntity);
        playerEntity.getCooldownTracker().setCooldown(stack.getItem(), 10);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + I18n.format("item.iceandfire.legendary_weapon.desc"));
        tooltip.add(TextFormatting.GRAY + I18n.format("item.iceandfire.ghost_sword.desc_0"));
    }
}
