package org.confluence.phase_journey.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.mixed.ILevelRenderer;

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
                PhaseManager.BLOCK.rollbackBlockProperties(phase); // 更新客户端世界
            }
        } else {
            for (ResourceLocation phase : phases) {
                p.removePhase(phase);
                l.removePhase(phase);
                PhaseManager.BLOCK.replaceBlockProperties(phase); // 更新客户端世界
            }
        }
        ((ILevelRenderer) Minecraft.getInstance().levelRenderer).phase_journey$rebuildAllChunks();
    }
}
