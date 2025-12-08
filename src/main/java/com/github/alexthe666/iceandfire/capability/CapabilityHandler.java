package com.github.alexthe666.iceandfire.capability;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.api.IEntityPropertiesCapability;
import com.github.alexthe666.iceandfire.api.InFCapabilities;
import com.github.alexthe666.iceandfire.capability.entityeffect.EntityEffectProvider;
import com.github.alexthe666.iceandfire.capability.entityproperties.EntityPropertiesProvider;
import com.github.alexthe666.iceandfire.message.MessageEntityEffect;
import com.github.alexthe666.iceandfire.message.MessageEntityProperties;
import com.github.alexthe666.iceandfire.message.MessageResetEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {

    private static final ResourceLocation ENTITY_EFFECT = new ResourceLocation(IceAndFire.MODID, "entity_effect");
    private static final ResourceLocation ENTITY_PROPERTIES = new ResourceLocation(IceAndFire.MODID, "entity_properties");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityLivingBase) {
            event.addCapability(ENTITY_EFFECT, new EntityEffectProvider());
        }
        if (event.getObject() instanceof EntityLivingBase) {
            event.addCapability(ENTITY_PROPERTIES, new EntityPropertiesProvider());
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.world;

        IEntityEffectCapability entityEffectCapability = InFCapabilities.getEntityEffectCapability(entity);
        if (entityEffectCapability != null) {
            entityEffectCapability.tickUpdate(entity, world); //Tick both client and server

            // Send packet from server if a sync is necessary
            if (!world.isRemote && entityEffectCapability.isDirty()) {
                syncEntityEffectUpdate(entityEffectCapability, entity);
            }
        }
        IEntityPropertiesCapability entityPropertiesCapability = InFCapabilities.getEntityPropertiesCapability(entity);
        if (entityPropertiesCapability != null) {
            // Send packet from server if a sync is necessary
            if (!world.isRemote && entityPropertiesCapability.isDirty()) {
                syncEntityPropertiesUpdate(entityPropertiesCapability, entity);
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
            IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(player);
            if(capability == null) return;
            messageEntityEffectClient(capability, player);
        }
    }

    @SubscribeEvent
    public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (!event.getEntityPlayer().world.isRemote && event.getTarget() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getTarget();
            IEntityEffectCapability entityEffectCapability = InFCapabilities.getEntityEffectCapability(entity);
            if (entityEffectCapability != null) {
                syncEntityEffectDirect(entityEffectCapability, entity, (EntityPlayerMP) event.getEntityPlayer());
            }
            IEntityPropertiesCapability entityPropertiesCapability = InFCapabilities.getEntityPropertiesCapability(entity);
            if (entityPropertiesCapability != null) {
                syncEntityPropertiesDirect(entityPropertiesCapability, entity, (EntityPlayerMP) event.getEntityPlayer());
            }
        }
    }

    /**
     * Update or reset effect for entity and tracking entities if update is needed, then mark clean
     */
    public static void syncEntityEffectUpdate(IEntityEffectCapability capability, EntityLivingBase entity) {
        if (capability.getPreviousEffect().syncToAllTracking() || capability.getEffect().syncToAllTracking()) {
            messageEntityEffectTracking(capability, entity);
        }
        if (entity instanceof EntityPlayerMP) {
            if (capability.getPreviousEffect().syncToAffectedClient() || capability.getEffect().syncToAffectedClient()) {
                messageEntityEffectClient(capability, (EntityPlayerMP) entity);
            }
        }
        capability.markClean();
    }

    /**
     * Update or reset properties for entity and tracking entities if update is needed, then mark clean
     */
    public static void syncEntityPropertiesUpdate(IEntityPropertiesCapability capability, EntityLivingBase entity) {
        messageEntityPropertiesTracking(capability, entity);
        if (entity instanceof EntityPlayerMP) {
            messageEntityPropertiesClient(capability, (EntityPlayerMP) entity);
        }
        capability.markClean();
    }

    /**
     * Update or reset effect of entity for specific tracking entity
     */
    public static void syncEntityEffectDirect(IEntityEffectCapability capability, EntityLivingBase entity, EntityPlayerMP player) {
        messageEntityEffectDirectTracking(capability, entity, player);
    }

    /**
     * eset effect of entity for specific tracking entity
     */
    public static void syncEntityPropertiesDirect(IEntityPropertiesCapability capability, EntityLivingBase entity, EntityPlayerMP player) {
        messageEntityPropertiesDirectTracking(capability, entity, player);
    }

    /**
     * Update all players tracking entity with current entity effect, or set to NONE if tracking is not needed
     */
    private static void messageEntityEffectTracking(IEntityEffectCapability capability, EntityLivingBase entity) {
        if (!capability.getEffect().syncToAllTracking()) {
            IceAndFire.NETWORK_WRAPPER.sendToAllTracking(new MessageResetEntityEffect(entity.getEntityId()), entity);
        }
        else {
            IceAndFire.NETWORK_WRAPPER.sendToAllTracking(new MessageEntityEffect(EntityEffectProvider.writeNBT(capability, null), entity.getEntityId()), entity);
        }
    }

    /**
     * Update all players tracking entity with current entity properties
     */
    private static void messageEntityPropertiesTracking(IEntityPropertiesCapability capability, EntityLivingBase entity) {
        IceAndFire.NETWORK_WRAPPER.sendToAllTracking(new MessageEntityProperties(EntityPropertiesProvider.writeNBT(capability, null), entity.getEntityId()), entity);
    }

    /**
     * Update specific player tracking entity with current entity effect, or set to NONE if tracking is not needed
     */
    private static void messageEntityEffectDirectTracking(IEntityEffectCapability capability, EntityLivingBase entity, EntityPlayerMP player) {
        if (!capability.getEffect().syncToAllTracking()) {
            IceAndFire.NETWORK_WRAPPER.sendTo(new MessageResetEntityEffect(entity.getEntityId()), player);
        }
        else {
            IceAndFire.NETWORK_WRAPPER.sendTo(new MessageEntityEffect(EntityEffectProvider.writeNBT(capability, null), entity.getEntityId()), player);
        }
    }

    /**
     * Update specific player tracking entity with current entity properties
     */
    private static void messageEntityPropertiesDirectTracking(IEntityPropertiesCapability capability, EntityLivingBase entity, EntityPlayerMP player) {
        IceAndFire.NETWORK_WRAPPER.sendTo(new MessageEntityProperties(EntityPropertiesProvider.writeNBT(capability, null), entity.getEntityId()), player);
    }

    /**
     * Update client player with current player effect, or set to NONE if tracking is not needed
     */
    private static void messageEntityEffectClient(IEntityEffectCapability capability, EntityPlayerMP player) {
        if (!capability.getEffect().syncToAffectedClient()) {
            IceAndFire.NETWORK_WRAPPER.sendTo(new MessageResetEntityEffect(player.getEntityId()), player);
        }
        else {
            IceAndFire.NETWORK_WRAPPER.sendTo(new MessageEntityEffect(EntityEffectProvider.writeNBT(capability, null), player.getEntityId()), player);
        }
    }

    /**
     * Update client player with current player properties
     */
    private static void messageEntityPropertiesClient(IEntityPropertiesCapability capability, EntityPlayerMP player) {
        IceAndFire.NETWORK_WRAPPER.sendTo(new MessageEntityProperties(EntityPropertiesProvider.writeNBT(capability, null), player.getEntityId()), player);
    }
}