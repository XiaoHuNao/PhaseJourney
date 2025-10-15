package com.xiaohunao.phase_journey.integration.kubejs;

import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import dev.latvian.mods.kubejs.event.KubeStartupEvent;

public class RegisterEventJS implements KubeStartupEvent {
    private final PhaseJourneyEvent.Register event;

    public RegisterEventJS(PhaseJourneyEvent.Register event) {
        this.event = event;
    }

    public <T extends IPhaseContext> void register(PhaseType phaseType, PhaseContextType<T> type, T  context) {
        event.register(phaseType,type,context);
    }

    @SafeVarargs
    public final <T extends IPhaseContext> void register(PhaseType phaseType, PhaseContextType<T> type, T... contexts) {
        event.register(phaseType,type,contexts);
    }
}
