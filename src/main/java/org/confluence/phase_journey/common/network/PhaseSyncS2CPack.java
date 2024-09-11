package org.confluence.phase_journey.common.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.phase_journey.api.IPhaseCapability;
import org.confluence.phase_journey.common.accessor.LevelRendererAccessor;

import java.util.function.Supplier;

public record PhaseSyncS2CPack(ResourceLocation phase, boolean add) {
    public static final Codec<PhaseSyncS2CPack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(PhaseSyncS2CPack::phase),
            Codec.BOOL.fieldOf("add").forGetter(PhaseSyncS2CPack::add)
    ).apply(instance, PhaseSyncS2CPack::new));

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeJsonWithCodec(CODEC, this);
    }

    public static PhaseSyncS2CPack decode(FriendlyByteBuf friendlyByteBuf) {
        return friendlyByteBuf.readJsonWithCodec(CODEC);
    }

    public static void handle(PhaseSyncS2CPack packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Client.handle(packet, ctx)));
        ctx.get().setPacketHandled(true);
    }
    public static class Client {
        public static void handle(PhaseSyncS2CPack packet, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer player = minecraft.player;
                IPhaseCapability.get(player).ifPresent(phaseCap -> {
                    if (packet.add()) {
                        phaseCap.addPhase(packet.phase());
                    } else {
                        phaseCap.removePhase(packet.phase());
                    }
                    ((LevelRendererAccessor)minecraft.levelRenderer).rebuildAllChunks();
                });
            });
        }
    }
}
