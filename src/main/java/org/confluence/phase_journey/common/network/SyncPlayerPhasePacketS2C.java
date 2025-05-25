package org.confluence.phase_journey.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.mixed.ILevelRenderer;
import org.jetbrains.annotations.NotNull;

public record SyncPlayerPhasePacketS2C(ResourceLocation phase, boolean add) implements CustomPacketPayload {
    public static final Type<SyncPlayerPhasePacketS2C> TYPE = new Type<>(PhaseJourney.asResource("sync_player_phase"));
    public static final StreamCodec<ByteBuf, SyncPlayerPhasePacketS2C> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, p -> p.phase,
            ByteBufCodecs.BOOL, p -> p.add,
            SyncPlayerPhasePacketS2C::new
    );

    @Override
    public @NotNull Type<SyncPlayerPhasePacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                PJClientPacketHandler.handleSync(this, context.player());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static class PJClientPacketHandler {
        public static void handleSync(SyncPlayerPhasePacketS2C packet, Player player) {
            Minecraft minecraft = Minecraft.getInstance();
            PhaseAttachment attachment = player.getData(PJAttachments.PHASE);
            if (packet.add()) {
                attachment.addPhase(packet.phase());
                PhaseManager.BLOCK.rollbackBlockProperties(packet.phase()); // 更新客户端世界
            } else {
                attachment.removePhase(packet.phase());
                PhaseManager.BLOCK.replaceBlockProperties(packet.phase()); // 更新客户端世界
            }
            ((ILevelRenderer) minecraft.levelRenderer).phase_journey$rebuildAllChunks();
        }
    }
}
