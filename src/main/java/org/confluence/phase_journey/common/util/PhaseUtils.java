package org.confluence.phase_journey.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.init.PJAttachments;

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
}
