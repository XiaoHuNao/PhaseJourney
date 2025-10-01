package org.confluence.phase_journey.common.phase.interaction;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.level.PistonEvent;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class EntityInteractionPhaseManager extends PhaseManager<EntityInteractionPhaseContext> {
	public static final EntityInteractionPhaseManager MANAGER = new EntityInteractionPhaseManager();

    private final BiMap<EntityType<?>, EntityInteractionPhaseContext> rules = HashBiMap.create();

    @Override
    public void register(ResourceLocation phase, EntityInteractionPhaseContext phaseContext) {
        super.register(phase, phaseContext);
        rules.put(phaseContext.entityType(), phaseContext);
    }

    private boolean denied(Player player, EntityType<?> entityType, Function<EntityInteractionPhaseContext, Boolean> allowExtractor) {
        EntityInteractionPhaseContext rule = rules.get(entityType);
        if (rule == null) return false;
        for (Map.Entry<ResourceLocation, Collection<EntityInteractionPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(rule)) {
                return !allowExtractor.apply(rule);
            }
        }
        return false;
    }

    /**
     * 针对方块/物品相关的全局限制（不依赖具体实体键）。
     * 规则解释：当玩家尚未达成某阶段，且该阶段中的任一交互规则将对应“允许项”设为 false，则拒绝该行为。
     */
    private boolean deniedWithoutEntity(Player player, Function<EntityInteractionPhaseContext, Boolean> allowExtractor) {
        for (Map.Entry<ResourceLocation, Collection<EntityInteractionPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            for (EntityInteractionPhaseContext ctx : entry.getValue()) {
                if (!allowExtractor.apply(ctx)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 非玩家导致的世界级别方块变化时的限制检查。
     * 当世界尚未达成某阶段，且该阶段中的任一交互规则将对应“允许项”设为 false，则拒绝该行为。
     */
    private boolean deniedForLevel(Level level, Function<EntityInteractionPhaseContext, Boolean> allowExtractor) {
        for (Map.Entry<ResourceLocation, Collection<EntityInteractionPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadLevelFinishedPhase(entry.getKey(), level)) continue;
            for (EntityInteractionPhaseContext ctx : entry.getValue()) {
                if (!allowExtractor.apply(ctx)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onAnimalTame(AnimalTameEvent event) {
        Player player = event.getTamer();
		EntityType<?> entityType = event.getAnimal().getType();
        if (denied(player, entityType, EntityInteractionPhaseContext::allowTame)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBreed(BabyEntitySpawnEvent event) {
        if (!(event.getCausedByPlayer() instanceof Player player)) return;
        LivingEntity parent = event.getParentA();
		EntityType<?> entityType = parent.getType();
        if (denied(player, entityType, EntityInteractionPhaseContext::allowBreed)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMount(EntityMountEvent event) {
        if (!(event.getEntityMounting() instanceof Player player)) return;
        Entity entityBeingMounted = event.getEntityBeingMounted();
		EntityType<?> entityType = entityBeingMounted.getType();
        if (denied(player, entityType, EntityInteractionPhaseContext::allowRide)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
		EntityType<?> entityType = target.getType();
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && denied(player, entityType, EntityInteractionPhaseContext::allowFeed)) {
            event.setCanceled(true);
            return;
        }
        // 一般实体右键交互
        if (denied(player, entityType, EntityInteractionPhaseContext::allowInteract)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
		EntityType<?> entityType = target.getType();
        if (denied(player, entityType, EntityInteractionPhaseContext::allowEntityAttack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (deniedWithoutEntity(player, EntityInteractionPhaseContext::allowBlockPlace)) {
                event.setCanceled(true);
            }
        } else {
            Level level = (Level) event.getLevel();
            if (deniedForLevel(level, EntityInteractionPhaseContext::allowBlockPlace)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (deniedWithoutEntity(player, EntityInteractionPhaseContext::allowBlockBreak)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (deniedWithoutEntity(player, EntityInteractionPhaseContext::allowBlockInteract)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (deniedWithoutEntity(player, EntityInteractionPhaseContext::allowItemUse)) {
            event.setCanceled(true);
        }
    }

}


