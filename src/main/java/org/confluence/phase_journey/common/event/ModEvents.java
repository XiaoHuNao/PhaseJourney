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
import org.confluence.phase_journey.common.network.PhaseSyncS2CPack;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = PhaseJourney.MODID)
public final class ModEvents {
    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> ModLoader.postEvent(new PhaseJourneyEvent.Register()));
    }

    @SubscribeEvent
    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                PhaseSyncS2CPack.TYPE,
                PhaseSyncS2CPack.STREAM_CODEC,
                PhaseSyncS2CPack::handle
        );
    }

    @SubscribeEvent
    public static void phaseJourney$register(PhaseJourneyEvent.Register event) {
        event.phaseRegister(context -> {
            context.blockReplacement(PhaseJourney.asResource("phase_1"), Blocks.STONE, Blocks.DIRT).register();
        });
    }
}
