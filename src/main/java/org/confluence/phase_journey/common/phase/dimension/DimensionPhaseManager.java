package org.confluence.phase_journey.common.phase.dimension;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.confluence.phase_journey.common.network.SyncPhasePacketS2C;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 维度阶段管理器
 * 负责管理玩家对维度的访问限制
 */
public class DimensionPhaseManager extends PhaseManager<DimensionPhaseContext> {
    public static final DimensionPhaseManager MANAGER = new DimensionPhaseManager();


    private final Map<ResourceKey<Level>, DimensionPhaseContext> dimensionRestrictions = new HashMap<>();
    
    // 玩家访问次数记录 (玩家UUID -> (维度 -> 访问次数))
    private final Map<UUID, Map<ResourceKey<Level>, Integer>> playerVisitCounts = new HashMap<>();

    /**
     * 注册维度限制规则
     */

    @Override
    public void register(ResourceLocation phase, DimensionPhaseContext phaseContext) {
        super.register(phase, phaseContext);

        dimensionRestrictions.put(phaseContext.getDimension(), phaseContext);
    }


    /**
     * 检查玩家是否可以进入指定维度（同时考虑玩家阶段和世界阶段）
     */
    public boolean canPlayerEnterDimension(ServerPlayer player, ResourceKey<Level> targetDimension) {
        DimensionPhaseContext restriction = dimensionRestrictions.get(targetDimension);
        if (restriction == null) {
            return true; // 没有限制，允许进入
        }

        // 检查是否已达到阶段要求（玩家或世界阶段）
        if (PhaseUtils.hadPlayerOrLevelAchievedPhase(restriction.getPhase(), player)) {
            return true; // 已达到阶段要求，允许进入
        }
        // 检查进入权限
        if (!restriction.isEnterAllowed()) {
            if (!restriction.getEnterMessage().isEmpty()) {
                player.sendSystemMessage(Component.literal(restriction.getEnterMessage()));
            }
            return false;
        }

        // 检查访问次数限制
        if (restriction.hasVisitLimit()) {
            int currentVisits = getPlayerVisitCount(player.getUUID(), targetDimension);
            if (currentVisits >= restriction.getMaxVisits()) {
                if (!restriction.getVisitLimitMessage().isEmpty()) {
                    player.sendSystemMessage(Component.literal(restriction.getVisitLimitMessage()));
                }
                return false;
            }
        }

        return true;
    }

    /**
     * 检查玩家是否可以离开指定维度（同时考虑玩家阶段和世界阶段）
     */
    public boolean canPlayerLeaveDimension(ServerPlayer player, ResourceKey<Level> currentDimension) {
        DimensionPhaseContext restriction = dimensionRestrictions.get(currentDimension);
        if (restriction == null) {
            return true; // 没有限制，允许离开
        }

        // 检查是否已达到阶段要求（玩家或世界阶段）
        if (PhaseUtils.hadPlayerOrLevelAchievedPhase(restriction.getPhase(), player)) {
            return true; // 已达到阶段要求，允许离开
        }

        // 检查离开权限
        if (!restriction.isLeaveAllowed()) {
            if (!restriction.getLeaveMessage().isEmpty()) {
                player.sendSystemMessage(Component.literal(restriction.getLeaveMessage()));
            }
            return false;
        }

        return true;
    }

    /**
     * 记录玩家进入维度
     */
    public void recordPlayerEnterDimension(ServerPlayer player, ResourceKey<Level> dimension) {
        DimensionPhaseContext restriction = dimensionRestrictions.get(dimension);
        if (restriction != null && restriction.hasVisitLimit()) {
            incrementPlayerVisitCount(player.getUUID(), dimension);
        }
    }

    /**
     * 获取玩家对指定维度的访问次数
     */
    public int getPlayerVisitCount(UUID playerId, ResourceKey<Level> dimension) {
        Map<ResourceKey<Level>, Integer> playerVisits = playerVisitCounts.get(playerId);
        if (playerVisits == null) {
            return 0;
        }
        return playerVisits.getOrDefault(dimension, 0);
    }

    /**
     * 增加玩家对指定维度的访问次数
     */
    private void incrementPlayerVisitCount(UUID playerId, ResourceKey<Level> dimension) {
        playerVisitCounts.computeIfAbsent(playerId, k -> new HashMap<>())
                .merge(dimension, 1, Integer::sum);
    }

    /**
     * 设置玩家对指定维度的访问次数
     */
    public void setPlayerVisitCount(UUID playerId, ResourceKey<Level> dimension, int count) {
        playerVisitCounts.computeIfAbsent(playerId, k -> new HashMap<>())
                .put(dimension, Math.max(0, count));
    }

    /**
     * 重置玩家对指定维度的访问次数
     */
    public void resetPlayerVisitCount(UUID playerId, ResourceKey<Level> dimension) {
        Map<ResourceKey<Level>, Integer> playerVisits = playerVisitCounts.get(playerId);
        if (playerVisits != null) {
            playerVisits.remove(dimension);
        }
    }

    /**
     * 重置玩家对所有维度的访问次数
     */
    public void resetAllPlayerVisitCounts(UUID playerId) {
        playerVisitCounts.remove(playerId);
    }

    /**
     * 获取指定维度的限制规则
     */
    public DimensionPhaseContext getDimensionRestriction(ResourceKey<Level> dimension) {
        return dimensionRestrictions.get(dimension);
    }

    /**
     * 检查指定维度是否有访问限制
     */
    public boolean hasDimensionRestriction(ResourceKey<Level> dimension) {
        return dimensionRestrictions.containsKey(dimension);
    }

    /**
     * 获取玩家剩余访问次数
     */
    public int getRemainingVisits(UUID playerId, ResourceKey<Level> dimension) {
        DimensionPhaseContext restriction = dimensionRestrictions.get(dimension);
        if (restriction == null || !restriction.hasVisitLimit()) {
            return -1; // 无限制
        }

        int currentVisits = getPlayerVisitCount(playerId, dimension);
        return Math.max(0, restriction.getMaxVisits() - currentVisits);
    }

    /**
     * 获取所有维度限制规则
     */
    public Collection<DimensionPhaseContext> getAllRestrictions() {
        return dimensionRestrictions.values();
    }

    /**
     * 获取指定阶段的所有维度限制规则
     */
    public Collection<DimensionPhaseContext> getRestrictionsForPhase(ResourceLocation phase) {
        return phaseContexts.get(phase);
    }

    /**
     * 清除指定阶段的所有维度限制规则
     */
    public void clearRestrictionsForPhase(ResourceLocation phase) {
        Collection<DimensionPhaseContext> restrictions = phaseContexts.removeAll(phase);
        for (DimensionPhaseContext restriction : restrictions) {
            dimensionRestrictions.remove(restriction.getDimension());
        }
    }

    /**
     * 清除所有维度限制规则
     */
    public void clearAllRestrictions() {
        phaseContexts.clear();
        dimensionRestrictions.clear();
        playerVisitCounts.clear();
    }

    /**
     * 检查指定维度是否对玩家开放（同时考虑玩家阶段和世界阶段）
     */
    public boolean isDimensionAccessibleWithWorldPhase(ServerPlayer player, ResourceKey<Level> dimension) {
        DimensionPhaseContext restriction = dimensionRestrictions.get(dimension);
        if (restriction == null) {
            return true; // 没有限制，可以访问
        }

        // 检查是否已达到阶段要求（玩家或世界阶段）
        return PhaseUtils.hadPlayerOrLevelAchievedPhase(restriction.getPhase(), player);
    }

    /**
     * 检查指定维度是否只对世界阶段开放（不考虑玩家个人阶段）
     */
    public boolean isDimensionAccessibleByWorldPhaseOnly(ServerPlayer player, ResourceKey<Level> dimension) {
        DimensionPhaseContext restriction = dimensionRestrictions.get(dimension);
        if (restriction == null) {
            return true; // 没有限制，可以访问
        }

        // 只检查世界阶段
        return PhaseUtils.hadLevelFinishedPhase(restriction.getPhase(), player.level());
    }

    /**
     * 获取玩家可以访问的维度列表（同时考虑玩家阶段和世界阶段）
     */
    public Collection<ResourceKey<Level>> getAccessibleDimensionsWithWorldPhase(ServerPlayer player) {
        return dimensionRestrictions.keySet().stream()
                .filter(dimension -> isDimensionAccessibleWithWorldPhase(player, dimension))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取玩家可以访问的维度列表（只考虑玩家个人阶段）
     */
    public Collection<ResourceKey<Level>> getAccessibleDimensionsByPlayerPhase(ServerPlayer player) {
        return dimensionRestrictions.keySet().stream()
                .filter(dimension -> {
                    DimensionPhaseContext restriction = dimensionRestrictions.get(dimension);
                    return restriction == null || PhaseUtils.hadPlayerReachedPhase(restriction.getPhase(), player);
                })
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取玩家可以访问的维度列表（只考虑世界阶段）
     */
    public Collection<ResourceKey<Level>> getAccessibleDimensionsByWorldPhase(ServerPlayer player) {
        return dimensionRestrictions.keySet().stream()
                .filter(dimension -> isDimensionAccessibleByWorldPhaseOnly(player, dimension))
                .collect(java.util.stream.Collectors.toList());
    }

    @SubscribeEvent
    public void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        Entity entity = event.getEntity();
        ResourceKey<Level> targetDimension = event.getDimension();

        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
        // 检查玩家是否可以离开当前维度
        if (!canPlayerLeaveDimension(player, entity.level().dimension())){
            event.setCanceled(true);
        }
        // 检查玩家是否可以进入目标维度
        if (!canPlayerEnterDimension(player, targetDimension)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        ResourceKey<Level> fromDimension = event.getFrom();
        ResourceKey<Level> toDimension = event.getTo();

        // 记录玩家进入新维度（用于访问次数统计）
        recordPlayerEnterDimension(player, toDimension);

        // 同步阶段数据到客户端
        SyncPhasePacketS2C.sync2Player4All(player, true);
    }
}

