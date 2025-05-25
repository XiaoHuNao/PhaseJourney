package org.confluence.phase_journey.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.network.SyncLevelPhasePacketS2C;
import org.confluence.phase_journey.common.network.SyncPlayerPhasePacketS2C;
import org.confluence.phase_journey.common.phase.PhaseManager;

import java.util.List;

public class PhaseUtils {

    /**
     * 检查实体或对象是否拥有特定阶段
     * @param phase 要检查的阶段标识符
     * @param holder 要检查的附件持有者
     * @return 持有者是否拥有该阶段
     */
    public static boolean hasPhase(ResourceLocation phase, AttachmentHolder holder) {
        return holder.getData(PJAttachments.PHASE).getPhases().contains(phase);
    }

    /**
     * 检查世界是否拥有特定阶段
     * @param phase 要检查的阶段标识符
     * @param level 要检查的世界
     * @return 世界是否拥有该阶段
     */
    public static boolean hadLevelFinishedPhase(ResourceLocation phase, Level level) {
        return hasPhase(phase, level);
    }

    /**
     * 检查玩家是否拥有特定阶段
     * @param phase 要检查的阶段标识符
     * @param player 要检查的玩家
     * @return 玩家是否拥有该阶段
     */
    public static boolean hadPlayerReachedPhase(ResourceLocation phase, Player player) {
        return hasPhase(phase, player);
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
     * 为附件持有者添加阶段（如果不存在）
     * @param phase 要添加的阶段标识符
     * @param holder 目标附件持有者
     * @return 如果阶段被添加则返回true，如果已存在则返回false
     */
    public static boolean addPhaseIfAbsent(ResourceLocation phase, AttachmentHolder holder) {
        return holder.getData(PJAttachments.PHASE).addPhaseIfAbsent(phase);
    }

    /**
     * 为附件持有者移除阶段（如果存在）
     * @param phase 要移除的阶段标识符
     * @param holder 目标附件持有者
     * @return 如果阶段被移除则返回true，如果不存在则返回false
     */
    public static boolean removePhaseIfPresent(ResourceLocation phase, AttachmentHolder holder) {
        return holder.getData(PJAttachments.PHASE).removePhaseIfPresent(phase);
    }

    /**
     * 根据阶段的存在与否返回对应的值
     * @param phase 要检查的阶段标识符
     * @param holder 要检查的附件持有者
     * @param ifPresent 阶段存在时返回的值
     * @param ifAbsent 阶段不存在时返回的值
     * @param <T> 返回值类型
     * @return 基于阶段检查结果的值
     */
    public static <T> T getValueBasedOnPhase(ResourceLocation phase, AttachmentHolder holder, T ifPresent, T ifAbsent) {
        return hasPhase(phase, holder) ? ifPresent : ifAbsent;
    }

    /**
     * 为玩家添加或移除阶段，并在必要时更新世界阶段状态
     * @param player 目标玩家
     * @param phase 要添加或移除的阶段
     * @param add true表示添加阶段，false表示移除阶段
     */
    public static void achievePlayerPhase(ServerPlayer player, ResourceLocation phase, boolean add) {
        List<ServerPlayer> players = player.server.getPlayerList().getPlayers();

        if (add) {
            addPhaseIfAbsent(phase, player);
            if (players.stream().allMatch(serverPlayer -> hasPhase(phase, serverPlayer))) {
                achieveLevelPhase((ServerLevel) player.level(), phase, true);
            }
        } else {
            removePhaseIfPresent(phase, player);
            if (players.stream().noneMatch(serverPlayer -> hasPhase(phase, serverPlayer))) {
                achieveLevelPhase((ServerLevel) player.level(), phase, false);
            }
        }

        PacketDistributor.sendToPlayer(player, new SyncPlayerPhasePacketS2C(phase, add));
    }


    /**
     * 为服务器世界添加或移除阶段，并处理相关副作用
     * @param level 目标服务器世界
     * @param phase 要添加或移除的阶段
     * @param add true表示添加阶段，false表示移除阶段
     */
    public static void achieveLevelPhase(ServerLevel level, ResourceLocation phase, boolean add) {
        if (add) {
            if (addPhaseIfAbsent(phase, level)) {
                PhaseManager.BLOCK.rollbackBlockProperties(phase);
            }
        } else {
            if (removePhaseIfPresent(phase, level)) {
                PhaseManager.BLOCK.replaceBlockProperties(phase);

            }
        }
        PacketDistributor.sendToAllPlayers(new SyncLevelPhasePacketS2C(phase, add));


    }


}