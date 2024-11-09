package org.confluence.phase_journey.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.network.SyncPhasePacketS2C;

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
        PhaseAttachment levelPhase = player.level().getData(PJAttachments.PHASE);
        if (add) {
            playerPhase.addPhase(phase);
            levelPhase.addPhase(phase);
        } else {
            playerPhase.removePhase(phase);
            levelPhase.removePhase(phase);
        }
        PacketDistributor.sendToPlayer(player, new SyncPhasePacketS2C(phase, add));
    }
}
