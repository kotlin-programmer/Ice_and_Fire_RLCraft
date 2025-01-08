package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.projectile.EntityDeathWormEgg;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;

public class RenderDeathWormEgg extends RenderSnowball<EntityDeathWormEgg> {
    public RenderDeathWormEgg(RenderManager renderManager, RenderItem renderItem) {
        super(renderManager, IafItemRegistry.deathworm_egg, renderItem);
    }

    @Override
    public ItemStack getStackToRender(EntityDeathWormEgg entity) {
        if (entity.giant) {
            return new ItemStack(IafItemRegistry.deathworm_egg, 1, 1);
        }
        return new ItemStack(IafItemRegistry.deathworm_egg);
    }
}
