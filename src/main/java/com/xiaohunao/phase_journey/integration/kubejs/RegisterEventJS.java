package com.xiaohunao.phase_journey.integration.kubejs;

import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import dev.latvian.mods.kubejs.event.KubeStartupEvent;

public class RegisterEventJS implements KubeStartupEvent {
    private final PhaseJourneyEvent.Register event;

    public RegisterEventJS(PhaseJourneyEvent.Register event) {
        this.event = event;
    }

//    public void phaseRegister(ResourceLocation phase, Consumer<PhaseManager> consumer) {
//        event.phaseRegister(phase, consumer);
//    }
}
