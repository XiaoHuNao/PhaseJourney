package com.xiaohunao.phase_journey.common.network;

import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import com.xiaohunao.phase_journey.mixed.ILevelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class PJClientPacketHandler {
    public static void handleSync(PhaseType phaseType, List<ResourceLocation> phases, boolean add, Player player) {
        switch (phaseType){
            case LEVEL -> {
                for (ResourceLocation phase : phases) {
                    PhaseType.LEVEL.applyOrRevokePhase(player.level(), phase, add);
                }
            }
            case PLAYER -> {
                for (ResourceLocation phase : phases) {
                    PhaseType.PLAYER.applyOrRevokePhase(player, phase, add);
                }
            }
        }
    }
}
