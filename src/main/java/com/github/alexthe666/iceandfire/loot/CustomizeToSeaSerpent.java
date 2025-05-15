package com.github.alexthe666.iceandfire.loot;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.item.*;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

public class CustomizeToSeaSerpent extends LootFunction {

    public CustomizeToSeaSerpent(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        if (!stack.isEmpty()  && context.getLootedEntity() instanceof EntitySeaSerpent) {
            EntitySeaSerpent seaSerpent = (EntitySeaSerpent)context.getLootedEntity();
            Random random = seaSerpent.getRNG();
            int sizeMultiplier = MathHelper.ceil(seaSerpent.getSeaSerpentScale() * (seaSerpent.isAncient() ? 2 : 1));
            if(stack.getItem() instanceof ItemSeaSerpentScales){
                stack.setCount(MathHelper.getInt(random, sizeMultiplier, sizeMultiplier * 3));
                return new ItemStack(seaSerpent.getEnum().scale, stack.getCount(), stack.getMetadata());
            }
            if(stack.getItem() == IafItemRegistry.sea_serpent_fang){
                stack.setCount(MathHelper.getInt(random, sizeMultiplier, sizeMultiplier * 2));
                return stack;
            }
        }
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<CustomizeToSeaSerpent> {
        public Serializer() {
            super(new ResourceLocation("iceandfire:customize_to_sea_serpent"), CustomizeToSeaSerpent.class);
        }

        public void serialize(JsonObject object, CustomizeToSeaSerpent functionClazz, JsonSerializationContext serializationContext) {
        }

        public CustomizeToSeaSerpent deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            return new CustomizeToSeaSerpent(conditionsIn);
        }
    }
}