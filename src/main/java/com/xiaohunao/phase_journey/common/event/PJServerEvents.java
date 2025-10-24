package com.xiaohunao.phase_journey.common.event;

import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import com.xiaohunao.phase_journey.api.event.ResourceManagerReloadEvent;
import com.xiaohunao.phase_journey.common.command.PhaseJourneyCommands;
import com.xiaohunao.phase_journey.common.init.PJPhaseContextTypes;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.network.RebuildChunksS2C;
import com.xiaohunao.phase_journey.common.network.SyncPhasePacketS2C;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;

import com.xiaohunao.phase_journey.common.phase.PhaseType;
import com.xiaohunao.phase_journey.common.phase.effect.MobEffectApplicableContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;

@EventBusSubscriber(modid = PhaseJourney.MODID)
public class PJServerEvents {
    @SubscribeEvent
    public static void onReloadableServerResources(ResourceManagerReloadEvent event) {
        if (event.getSide() != LogicalSide.SERVER) return;

        for (PhaseContextType<?> contextType : PJRegistries.PHASE_CONTEXT_TYPE) {
            contextType.manager().clear();
        }

        PhaseJourneyEvent.Register register = new PhaseJourneyEvent.Register();
        NeoForge.EVENT_BUS.post(register);
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener((ResourceManagerReloadListener) res -> NeoForge.EVENT_BUS.post(new ResourceManagerReloadEvent(res, LogicalSide.SERVER)));

        for (PhaseContextType<?> contextType : PJRegistries.PHASE_CONTEXT_TYPE) {
            event.addListener(contextType.manager());
        }
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        SyncPhasePacketS2C.sync2Player4All((ServerPlayer) event.getEntity(), true);
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        SyncPhasePacketS2C.sync2Player4All((ServerPlayer) event.getEntity(), true);
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        SyncPhasePacketS2C.sync2Player4All((ServerPlayer) event.getEntity(), true);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent
    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                SyncPhasePacketS2C.TYPE,
                SyncPhasePacketS2C.STREAM_CODEC,
                SyncPhasePacketS2C::handle
        );
        registrar.playToClient(
                RebuildChunksS2C.TYPE,
                RebuildChunksS2C.STREAM_CODEC,
                RebuildChunksS2C::handle
        );
    }

    @SubscribeEvent
    public static void onPhaseJourneyRegister(PhaseJourneyEvent.Register event) {
        event.register(PhaseType.LEVEL,PJPhaseContextTypes.MOB_EFFECT.get(),new MobEffectApplicableContext(
                ResourceLocation.parse("phase_journey:test"),
                MobEffects.MOVEMENT_SPEED
        ));

        event.register(PhaseType.LEVEL,PJPhaseContextTypes.MOB_EFFECT.get(),new MobEffectApplicableContext(
                ResourceLocation.parse("phase_journey:test"),
                MobEffects.MOVEMENT_SLOWDOWN
        ));
    }

}
