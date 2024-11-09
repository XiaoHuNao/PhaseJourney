package org.confluence.phase_journey.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.network.SyncPhasePacketS2C;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;

import java.util.List;

public class PhaseUtils {
    public static boolean hadPlayerReachedPhase(ResourceLocation phase, Player player) {
        return player.getData(PJAttachments.PHASE).getPhases().contains(phase);
    }

    public static boolean hadLevelFinishedPhase(ResourceLocation phase, Level level) {
        return level.getData(PJAttachments.PHASE).getPhases().contains(phase);
    }

    public static boolean hadPlayerOrLevelAchievedPhase(ResourceLocation phase, Player player) {
        return hadPlayerReachedPhase(phase, player) || hadLevelFinishedPhase(phase, player.level());
    }

    public static void achievePhase(ServerPlayer player, ResourceLocation phase, boolean add) {
        PhaseAttachment playerPhase = player.getData(PJAttachments.PHASE);
        List<ServerPlayer> players = player.server.getPlayerList().getPlayers();
        if (add) {
            playerPhase.addPhase(phase);
            if (players.stream().allMatch(serverPlayer -> serverPlayer.getData(PJAttachments.PHASE).getPhases().contains(phase))) {
                player.level().getData(PJAttachments.PHASE).addPhase(phase); // 当所有玩家抵达该阶段时，才更新服务端世界阶段
                BlockPhaseManager.INSTANCE.rollbackBlockProperties(phase);
            }
        } else {
            playerPhase.removePhase(phase);
            if (players.stream().noneMatch(serverPlayer -> serverPlayer.getData(PJAttachments.PHASE).getPhases().contains(phase))) {
                player.level().getData(PJAttachments.PHASE).removePhase(phase); // 当没有玩家抵达该阶段时，才更新服务端世界阶段
                BlockPhaseManager.INSTANCE.replaceBlockProperties(phase);
            }
        }
        PacketDistributor.sendToPlayer(player, new SyncPhasePacketS2C(phase, add));
    }
}
