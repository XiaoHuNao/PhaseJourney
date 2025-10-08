package org.confluence.phase_journey.integration.kubejs;

import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import org.confluence.phase_journey.api.event.PhaseJourneyEvent;

public class RegisterEventJS implements KubeStartupEvent {
    private final PhaseJourneyEvent.Register event;

    public RegisterEventJS(PhaseJourneyEvent.Register event) {
        this.event = event;
    }

//    public void phaseRegister(ResourceLocation phase, Consumer<PhaseManager> consumer) {
//        event.phaseRegister(phase, consumer);
//    }
}
