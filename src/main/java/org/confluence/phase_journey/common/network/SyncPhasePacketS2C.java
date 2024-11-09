package org.confluence.phase_journey.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.phase_journey.PhaseJourney;
import org.jetbrains.annotations.NotNull;

public record SyncPhasePacketS2C(ResourceLocation phase, boolean add) implements CustomPacketPayload {
    public static final Type<SyncPhasePacketS2C> TYPE = new Type<>(PhaseJourney.asResource("sync_phase"));
    public static final StreamCodec<ByteBuf, SyncPhasePacketS2C> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, p -> p.phase,
            ByteBufCodecs.BOOL, p -> p.add,
            SyncPhasePacketS2C::new
    );

    @Override
    public @NotNull Type<SyncPhasePacketS2C> type() {
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
}
