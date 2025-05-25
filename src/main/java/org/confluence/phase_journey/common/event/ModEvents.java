package org.confluence.phase_journey.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.api.PhaseJourneyEvent;
import org.confluence.phase_journey.common.network.SyncLevelPhasePacketS2C;
import org.confluence.phase_journey.common.network.SyncPlayerPhasePacketS2C;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = PhaseJourney.MODID)
public final class ModEvents {
    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            PhaseJourneyEvent.Register register = new PhaseJourneyEvent.Register();
            ModLoader.postEvent(register);
            register.replaceBlockProperties();
        });
    }

    @SubscribeEvent
    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                SyncPlayerPhasePacketS2C.TYPE,
                SyncPlayerPhasePacketS2C.STREAM_CODEC,
                SyncPlayerPhasePacketS2C::handle
        );
        registrar.playToClient(
                SyncLevelPhasePacketS2C.TYPE,
                SyncLevelPhasePacketS2C.STREAM_CODEC,
                SyncLevelPhasePacketS2C::handle
        );
    }
}
