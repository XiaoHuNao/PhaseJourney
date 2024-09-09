package org.confluence.phase_journey.common.event.subscriber;

import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.event.PhaseJourneyRegisterEvent;

@EventBusSubscriber
public class PhaseRegisterEventSubscriber {
    @SubscribeEvent
    public static void onPhaseJourneyRegister(PhaseJourneyRegisterEvent event) {
        event.phaseRegister(context -> {
            context.blockPhase(PhaseJourney.asResource("phase_journey:phase_1"), Blocks.DIAMOND_BLOCK, Blocks.STONE).register();
        });
    }

}