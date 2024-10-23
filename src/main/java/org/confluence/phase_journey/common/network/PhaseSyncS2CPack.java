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
import org.confluence.phase_journey.common.attachment.PhaseCapability;
import org.confluence.phase_journey.common.init.ModAttachments;
import org.confluence.phase_journey.common.mixinauxi.ILevelRenderer;
import org.jetbrains.annotations.NotNull;

public record PhaseSyncS2CPack(ResourceLocation phase, boolean add) implements CustomPacketPayload {
    public static final Type<PhaseSyncS2CPack> TYPE = new Type<>(PhaseJourney.asResource("phase_sync_s2c"));
    public static final StreamCodec<ByteBuf, PhaseSyncS2CPack> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, p -> p.phase,
            ByteBufCodecs.BOOL, p -> p.add,
            PhaseSyncS2CPack::new
    );

    @Override
    public @NotNull Type<PhaseSyncS2CPack> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                Client.handle(this, context.player());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client {
        public static void handle(PhaseSyncS2CPack packet, Player player) {
            Minecraft minecraft = Minecraft.getInstance();
            PhaseCapability phaseCap = player.getData(ModAttachments.PHASE_CAPABILITY.get());
            if (packet.add()) {
                phaseCap.addPhase(packet.phase());
            } else {
                phaseCap.removePhase(packet.phase());
            }
            ((ILevelRenderer) minecraft.levelRenderer).phase_journey$rebuildAllChunks();
        }
    }
}
