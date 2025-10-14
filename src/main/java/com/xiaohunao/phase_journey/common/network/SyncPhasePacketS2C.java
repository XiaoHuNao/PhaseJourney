package com.xiaohunao.phase_journey.common.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SyncPhasePacketS2C(List<ResourceLocation> phases, boolean add) implements CustomPacketPayload {
    public static final Type<SyncPhasePacketS2C> TYPE = new Type<>(PhaseJourney.asResource("sync_phase"));
    public static final StreamCodec<ByteBuf, SyncPhasePacketS2C> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncPhasePacketS2C::phases,
            ByteBufCodecs.BOOL, SyncPhasePacketS2C::add,
            SyncPhasePacketS2C::new
    );

    @Override
    public @NotNull Type<SyncPhasePacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                PJClientPacketHandler.handleSync(phases, add, context.player());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sync2Player4All(ServerPlayer player, boolean add) {
        PacketDistributor.sendToPlayer(player, new SyncPhasePacketS2C(Streams.concat(
                PhaseAttachment.of(player).getPhases().stream(),
                PhaseAttachment.of(player.level()).getPhases().stream()
        ).distinct().toList(), add));
    }

    public static void sync2Player(ServerPlayer player, boolean add, ResourceLocation... phases) {
        PacketDistributor.sendToPlayer(player, new SyncPhasePacketS2C(Lists.newArrayList(phases), add));
    }

    public static void sync2All(boolean add, ResourceLocation... phases) {
        PacketDistributor.sendToAllPlayers(new SyncPhasePacketS2C(Lists.newArrayList(phases), add));
    }
}
