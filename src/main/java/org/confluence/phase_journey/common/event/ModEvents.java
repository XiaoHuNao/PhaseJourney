package org.confluence.phase_journey.common.event;

import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.api.PhaseJourneyEvent;
import org.confluence.phase_journey.common.network.PhaseSyncS2CPack;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = PhaseJourney.MODID)
public final class ModEvents {
    @SubscribeEvent
    public static void onPhaseJourneyRegister(PhaseJourneyEvent.Register event) {
        event.phaseRegister(context -> {
            context.blockPhase(PhaseJourney.asResource("phase_1"), Blocks.STONE, Blocks.CRAFTING_TABLE).register();
        });
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                PhaseSyncS2CPack.TYPE,
                PhaseSyncS2CPack.STREAM_CODEC,
                PhaseSyncS2CPack::handle
        );
    }
}
