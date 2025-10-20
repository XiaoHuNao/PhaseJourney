package com.xiaohunao.phase_journey.common.network;

import org.jetbrains.annotations.NotNull;

import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.mixed.ILevelRenderer;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record RebuildChunksS2C() implements CustomPacketPayload {
    public static final Type<RebuildChunksS2C> TYPE = new Type<>(PhaseJourney.asResource("rebuild_chunks"));

    public static final StreamCodec<ByteBuf, RebuildChunksS2C> STREAM_CODEC = StreamCodec.unit(new RebuildChunksS2C());

    @Override
    public @NotNull Type<RebuildChunksS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null && context.player().isLocalPlayer()) {
                ((ILevelRenderer) Minecraft.getInstance().levelRenderer).phase_journey$rebuildAllChunks();
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}


