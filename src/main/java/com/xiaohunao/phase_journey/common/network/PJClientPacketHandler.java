package com.xiaohunao.phase_journey.common.network;

import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.mixed.ILevelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class PJClientPacketHandler {
    public static void handleSync(List<ResourceLocation> phases, boolean add, Player player) {
        PhaseAttachment p = PhaseAttachment.of(player);
        PhaseAttachment l = PhaseAttachment.of(player.level());
        if (add) {
            for (ResourceLocation phase : phases) {
                p.addPhase(phase);
                l.addPhase(phase);
                for (PhaseContextType<?> type : PJRegistries.PHASE_CONTEXT_TYPE) {
                    type.manager().broadcastPhaseChangeToClient(phase, true);
                }
            }
        } else {
            for (ResourceLocation phase : phases) {
                p.removePhase(phase);
                l.removePhase(phase);
                for (PhaseContextType<?> type : PJRegistries.PHASE_CONTEXT_TYPE) {
                    type.manager().broadcastPhaseChangeToClient(phase, false);
                }
            }
        }
        ((ILevelRenderer) Minecraft.getInstance().levelRenderer).phase_journey$rebuildAllChunks();
    }
}
