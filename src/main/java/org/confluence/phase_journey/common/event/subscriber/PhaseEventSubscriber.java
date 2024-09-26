package org.confluence.phase_journey.common.event.subscriber;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.event.PhaseJourneyEvent;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;

@Mod.EventBusSubscriber
public class PhaseEventSubscriber {
    @SubscribeEvent
    public static void onPhaseJourneyRegister(PhaseJourneyEvent.Register event) {
        event.phaseRegister(context -> {
            context.blockPhase(PhaseJourney.asResource("phase_1"), Blocks.STONE, Blocks.CRAFTING_TABLE).register();
        });
    }

    @SubscribeEvent
    public void onPhaseJourneyAdd(PhaseJourneyEvent.Add event) {
        BlockPhaseManager.INSTANCE.ReplaceBlockBlockBehaviour(event.phase,false);
    }


}
