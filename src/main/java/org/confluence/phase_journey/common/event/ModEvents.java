package org.confluence.phase_journey.common.event;

import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.api.PhaseJourneyEvent;
import org.confluence.phase_journey.common.network.SyncPhasePacketS2C;

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
                SyncPhasePacketS2C.TYPE,
                SyncPhasePacketS2C.STREAM_CODEC,
                SyncPhasePacketS2C::handle
        );
    }

    @SubscribeEvent
    public static void phaseJourney$register(PhaseJourneyEvent.Register event) {
        event.phaseRegister(PhaseJourney.asResource("phase_1"), context -> {
            context.blockReplacement(Blocks.STONE, Blocks.DIRT);
        });
    }
}
