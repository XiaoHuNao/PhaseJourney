package com.xiaohunao.phase_journey.common.phase.interaction;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Map;
import java.util.function.Function;

public class EntityInteractionPhaseManager extends PhaseManager<EntityInteractionPhaseContext> {

    public static final EntityInteractionPhaseManager MANAGER = new EntityInteractionPhaseManager();

    private final BiMap<EntityType<?>, EntityInteractionPhaseContext> rules = HashBiMap.create();

    @Override
    public void register(PhaseType type, ResourceLocation phase, EntityInteractionPhaseContext phaseContext) {
        super.register(type, phase, phaseContext);
        rules.put(phaseContext.entityType(), phaseContext);
    }

    public boolean isRestricted(Level level, BlockPos pos, Player player, EntityType<?> entityType,
            Function<EntityInteractionPhaseContext, Boolean> allowExtractor) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, EntityInteractionPhaseContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            EntityInteractionPhaseContext phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = phaseContext.getPhase();

            if (!phaseContext.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            if (entityType != null && !phaseContext.entityType().equals(entityType)) {
                continue;
            }

            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, pos, player);
            if (phaseAttachment == null) {
                return false;
            }

            return phaseAttachment.ifPhaseAbsent(phase, () -> !allowExtractor.apply(phaseContext),false);
        }
        return false;
    }

    @SubscribeEvent
    public void onAnimalTame(AnimalTameEvent event) {
        Player player = event.getTamer();
        EntityType<?> entityType = event.getAnimal().getType();
        if (isRestricted(player.level(), null, player, entityType, EntityInteractionPhaseContext::allowTame)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBreed(BabyEntitySpawnEvent event) {
        Player causedByPlayer = event.getCausedByPlayer() == null ? null : event.getCausedByPlayer();
        Level level = causedByPlayer == null ? null : causedByPlayer.level();
        LivingEntity parent = event.getParentA();
        EntityType<?> entityType = parent.getType();
        if (isRestricted(level, null, causedByPlayer, entityType, EntityInteractionPhaseContext::allowBreed)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMount(EntityMountEvent event) {
        if (!(event.getEntityMounting() instanceof Player player)) {
            return;
        }
        Entity entityBeingMounted = event.getEntityBeingMounted();
        EntityType<?> entityType = entityBeingMounted.getType();
        if (isRestricted(player.level(), null, player, entityType, EntityInteractionPhaseContext::allowRide)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        EntityType<?> entityType = target.getType();
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && isRestricted(player.level(), null, player, entityType, EntityInteractionPhaseContext::allowFeed)) {
            event.setCanceled(true);
            return;
        }
        // 一般实体右键交互
        if (isRestricted(player.level(), null, player, entityType, EntityInteractionPhaseContext::allowInteract)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        EntityType<?> entityType = target.getType();
        if (isRestricted(player.level(), null, player, entityType, EntityInteractionPhaseContext::allowEntityAttack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (isRestricted(player.level(), event.getPos(), player, null, EntityInteractionPhaseContext::allowBlockPlace)) {
                event.setCanceled(true);
            }
        } else {
            Level level = (Level) event.getLevel();
            if (isRestricted(level, event.getPos(), null, null, EntityInteractionPhaseContext::allowBlockPlace)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (isRestricted(player.level(), event.getPos(), player, null, EntityInteractionPhaseContext::allowBlockBreak)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (isRestricted(player.level(), event.getPos(), player, null, EntityInteractionPhaseContext::allowBlockInteract)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (isRestricted(player.level(), null, player, null, EntityInteractionPhaseContext::allowItemUse)) {
            event.setCanceled(true);
        }
    }

}
