package com.xiaohunao.phase_journey.common.util;

import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class PhaseUtils {
    /**
     * 检查世界是否拥有特定阶段
     *
     * @param phase 要检查的阶段标识符
     * @param level 要检查的世界
     * @return 世界是否拥有该阶段
     */
    public static boolean hadLevelFinishedPhase(ResourceLocation phase, Level level) {
        return PhaseAttachment.of(level).getPhases().contains(phase);
    }

    /**
     * 检查玩家是否拥有特定阶段
     *
     * @param phase  要检查的阶段标识符
     * @param player 要检查的玩家
     * @return 玩家是否拥有该阶段
     */
    public static boolean hadPlayerReachedPhase(ResourceLocation phase, Player player) {
        return PhaseAttachment.of(player).getPhases().contains(phase);
    }

    /**
     * 检查玩家或其所在世界是否拥有特定阶段
     *
     * @param phase  要检查的阶段标识符
     * @param player 要检查的玩家
     * @return 如果玩家或其所在世界拥有该阶段则返回true
     */
    public static boolean hadPlayerOrLevelAchievedPhase(ResourceLocation phase, Player player) {
        return hadPlayerReachedPhase(phase, player) || hadLevelFinishedPhase(phase, player.level());
    }

    /**
     * 根据阶段的存在与否返回对应的值
     *
     * @param phase     要检查的阶段标识符
     * @param level     要检查的附件持有者
     * @param ifPresent 阶段存在时返回的值
     * @param ifAbsent  阶段不存在时返回的值
     * @param <T>       返回值类型
     * @return 基于阶段检查结果的值
     */
    public static <T> T getValueBasedOnPhase(ResourceLocation phase, Level level, T ifPresent, T ifAbsent) {
        return hadLevelFinishedPhase(phase, level) ? ifPresent : ifAbsent;
    }

    private static <T extends IPhaseContext, M extends PhaseManager<T>> boolean isPhaseIncluded(M phaseManager, @Nullable Level level, @Nullable Player player, @Nullable BlockPos pos) {
        Multimap<PhaseType, Pair<ResourceLocation, T>> phaseContexts = phaseManager.getPhaseContexts();
        for (Map.Entry<PhaseType, Pair<ResourceLocation, T>> entry : phaseContexts.entries()) {
            PhaseAttachment phaseAttachment = entry.getKey().getPhaseAttachment(level, pos, player);
            if (phaseAttachment != null && phaseAttachment.getPhases().contains(entry.getValue().getFirst())) {
                return true;
            }
        }
        return false;
    }

    public static <T extends IPhaseContext, M extends PhaseManager<T>> boolean anyContextMatches(M phaseManager, @Nullable Level level, @Nullable Player player, @Nullable BlockPos pos, BiFunction<T, M, Boolean> ctx) {
        if (!isPhaseIncluded(phaseManager, level, player, pos)) {
            return false;
        }
        return phaseManager.getPhaseContexts().values().stream()
                .map(Pair::getSecond)
                .anyMatch(context -> ctx.apply(context, phaseManager));
    }

    public static <R, T extends IPhaseContext, M extends PhaseManager<T>> R findFirstContextOrReturnDefault(M phaseManager, @Nullable Level level, @Nullable Player player, @Nullable BlockPos pos, BiFunction<T, M, R> ctx, R defaultValue) {
        if (isPhaseIncluded(phaseManager, level, player, pos)) {
            return defaultValue;
        }
        return phaseManager.getPhaseContexts().values().stream()
                .map(Pair::getSecond)
                .map(context -> ctx.apply(context, phaseManager))
                .findFirst().orElse(defaultValue);
    }

    public static <T extends IPhaseContext, M extends PhaseManager<T>> void applyActionToMatchingContexts(M phaseManager, @Nullable Level level, @Nullable Player player, @Nullable BlockPos pos, BiFunction<T, M, Boolean> ctx, BiConsumer<T, M> actuator) {
        if (!isPhaseIncluded(phaseManager, level, player, pos)) {
            phaseManager.getPhaseContexts().values().stream()
                    .map(Pair::getSecond)
                    .filter(context -> ctx.apply(context, phaseManager))
                    .forEach(context -> actuator.accept(context, phaseManager));
        }
    }
}
