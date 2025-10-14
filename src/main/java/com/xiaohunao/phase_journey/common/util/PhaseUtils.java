package com.xiaohunao.phase_journey.common.util;

import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.network.SyncPhasePacketS2C;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.stream.Stream;

public class PhaseUtils {
    /**
     * 检查世界是否拥有特定阶段
     * @param phase 要检查的阶段标识符
     * @param level 要检查的世界
     * @return 世界是否拥有该阶段
     */
    public static boolean hadLevelFinishedPhase(ResourceLocation phase, Level level) {
        return PhaseAttachment.of(level).getPhases().contains(phase);
    }

    /**
     * 检查玩家是否拥有特定阶段
     * @param phase 要检查的阶段标识符
     * @param player 要检查的玩家
     * @return 玩家是否拥有该阶段
     */
    public static boolean hadPlayerReachedPhase(ResourceLocation phase, Player player) {
        return PhaseAttachment.of(player).getPhases().contains(phase);
    }

    /**
     * 检查玩家或其所在世界是否拥有特定阶段
     * @param phase 要检查的阶段标识符
     * @param player 要检查的玩家
     * @return 如果玩家或其所在世界拥有该阶段则返回true
     */
    public static boolean hadPlayerOrLevelAchievedPhase(ResourceLocation phase, Player player) {
        return hadPlayerReachedPhase(phase, player) || hadLevelFinishedPhase(phase, player.level());
    }

    /**
     * 根据阶段的存在与否返回对应的值
     * @param phase 要检查的阶段标识符
     * @param level 要检查的附件持有者
     * @param ifPresent 阶段存在时返回的值
     * @param ifAbsent 阶段不存在时返回的值
     * @param <T> 返回值类型
     * @return 基于阶段检查结果的值
     */
    public static <T> T getValueBasedOnPhase(ResourceLocation phase, Level level, T ifPresent, T ifAbsent) {
        return hadLevelFinishedPhase(phase, level) ? ifPresent : ifAbsent;
    }

    /**
     * 为玩家添加或移除阶段，并在必要时更新世界阶段状态
     * @param player 目标玩家
     * @param phase 要添加或移除的阶段
     * @param add true表示添加阶段，false表示移除阶段
     */
    public static void achievePlayerPhase(ServerPlayer player, ResourceLocation phase, boolean add) {
        Stream<ServerPlayer> players = player.server.getPlayerList().getPlayers().stream();
        if (add) {
            PhaseAttachment.of(player).addPhase(phase);
            if (players.allMatch(serverPlayer -> hadPlayerReachedPhase(phase, serverPlayer))) {
                PhaseAttachment.of(player.level()).addPhase(phase);
                for (PhaseContextType<?> type : PJRegistries.PHASE_CONTEXT_TYPE) {
                    type.manager().broadcastPhaseChangeToClient(phase, true);
                }
            }
        } else {
            PhaseAttachment.of(player).removePhase(phase);
            if (players.noneMatch(serverPlayer -> hadPlayerReachedPhase(phase, serverPlayer))) {
                PhaseAttachment.of(player.level()).removePhase(phase);
                for (PhaseContextType<?> type : PJRegistries.PHASE_CONTEXT_TYPE) {
                    type.manager().broadcastPhaseChangeToClient(phase, false);
                }
            }
        }
        SyncPhasePacketS2C.sync2Player(player, add, phase);
    }

    /**
     * 为服务器世界添加或移除阶段，并处理相关副作用
     * @param level 目标服务器世界
     * @param phase 要添加或移除的阶段
     * @param add true表示添加阶段，false表示移除阶段
     */
    public static void achieveLevelPhase(ServerLevel level, ResourceLocation phase, boolean add) {
        if (add) {
            PhaseAttachment.of(level).addPhase(phase);
            for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                PhaseAttachment.of(player).addPhase(phase);
            }

            for (PhaseContextType<?> type : PJRegistries.PHASE_CONTEXT_TYPE) {
                type.manager().achieveLevelPhase(level,phase, true);
            }
        } else {
            PhaseAttachment.of(level).removePhase(phase);
            for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                PhaseAttachment.of(player).removePhase(phase);
            }
            for (PhaseContextType<?> type : PJRegistries.PHASE_CONTEXT_TYPE) {
                type.manager().achieveLevelPhase(level,phase, false);
            }
        }
        SyncPhasePacketS2C.sync2All(add, phase);
    }
}
