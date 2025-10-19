package com.xiaohunao.phase_journey.common.phase.dimension;

import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DimensionPhaseManager extends PhaseManager<DimensionTravelRestrictedContext> {
    public static final DimensionPhaseManager MANAGER = new DimensionPhaseManager();
    private final Map<ResourceKey<Level>, DimensionTravelRestrictedContext> dimensionRestrictions = new HashMap<>();
    
    // 玩家访问次数记录 (实体UUID -> (维度 -> 访问次数))
    private final Map<UUID, Map<ResourceKey<Level>, Integer>> playerEnterVisitCounts = new HashMap<>();
    private final Map<UUID, Map<ResourceKey<Level>, Integer>> playerLeaveVisitCounts = new HashMap<>();

    @Override
    public void register(PhaseType type,ResourceLocation phase, DimensionTravelRestrictedContext phaseContext) {
        super.register(type, phase, phaseContext);
        dimensionRestrictions.put(phaseContext.getDimension(), phaseContext);
    }

    public boolean isRestricted(Level level, @Nullable Player player, ResourceKey<Level> targetDimension, Entity entity, boolean isEnter){
        return isRestricted(level,null,player,ctx ->{
            if(isEnter){
                return ctx.isEnterAllowed() && canEntityTravelToDimension(entity.getUUID(), targetDimension, ctx, true);
            }else {
                return ctx.isLeaveAllowed() && canEntityTravelToDimension(entity.getUUID(), targetDimension, ctx, false);
            }
        });
    }

    /**
     * 记录玩家进入维度
     */
    public void recordPlayerEnterDimension(Entity entity, ResourceKey<Level> dimension, boolean isEnter) {
        DimensionTravelRestrictedContext restriction = dimensionRestrictions.get(dimension);
        if (restriction != null && canEntityTravelToDimension(entity.getUUID(), dimension, restriction, isEnter)) {
            incrementPlayerVisitCount(entity.getUUID(), dimension,isEnter);
        }
    }

    /**
     * 增加玩家对指定维度的访问次数
     */
    private void incrementPlayerVisitCount(UUID uuid, ResourceKey<Level> dimension, boolean isEnter) {
        Map<UUID, Map<ResourceKey<Level>, Integer>> visitCounts = isEnter ? this.playerEnterVisitCounts : this.playerLeaveVisitCounts;

        visitCounts.computeIfAbsent(uuid, k -> new HashMap<>())
                .merge(dimension, 1, Integer::sum);
    }

    /**
     * 检查实体是否可以进入或离开指定维度
     */
    public boolean canEntityTravelToDimension(UUID uuid, ResourceKey<Level> targetDimension, DimensionTravelRestrictedContext dimensionTravelRestrictedContext, boolean isEnter) {
        Map<UUID, Map<ResourceKey<Level>, Integer>> visitCounts = isEnter ? this.playerEnterVisitCounts : this.playerLeaveVisitCounts;
        Integer maxVisits = isEnter ? dimensionTravelRestrictedContext.getMaxEnterVisits() : dimensionTravelRestrictedContext.getMaxLeaveVisits();

        Map<ResourceKey<Level>, Integer> visits = visitCounts.get(uuid);
        if (visits == null) {
            return true;
        }
        return visits.getOrDefault(targetDimension, 0) < maxVisits;
    }



    @SubscribeEvent
    public void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        Entity entity = event.getEntity();
        ResourceKey<Level> targetDimension = event.getDimension();

        Player player = entity instanceof Player ? (Player) entity : null;
        // 检查玩家是否可以离开当前维度
        if (isRestricted(entity.level(), player, entity.level().dimension(), entity, false)){
            recordPlayerEnterDimension(entity, entity.level().dimension(), false);
            return;
        }
        // 检查玩家是否可以进入目标维度
        if (isRestricted(entity.level(), player, targetDimension, entity, true)){
            recordPlayerEnterDimension(entity, targetDimension, true);
            return;
        }
        event.setCanceled(true);
    }
}

