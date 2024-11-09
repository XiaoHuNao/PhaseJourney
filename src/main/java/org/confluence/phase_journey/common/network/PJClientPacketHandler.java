package org.confluence.phase_journey.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.mixed.ILevelRenderer;

@OnlyIn(Dist.CLIENT)
public final class PJClientPacketHandler {
    public static void handleSync(SyncPhasePacketS2C packet, Player player) {
        Minecraft minecraft = Minecraft.getInstance();
        PhaseAttachment attachment = player.getData(PJAttachments.PHASE);
        if (packet.add()) {
            attachment.addPhase(packet.phase());
        } else {
            attachment.removePhase(packet.phase());
        }
        ((ILevelRenderer) minecraft.levelRenderer).phase_journey$rebuildAllChunks();
    }
}
