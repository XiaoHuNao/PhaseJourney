package com.xiaohunao.phase_journey.integration.kubejs;

import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import dev.latvian.mods.kubejs.event.KubeStartupEvent;

public class RegisterEventJS implements KubeStartupEvent {
    public RegisterEventJS() {
    }

    public <T extends IPhaseContext> void register(PhaseType phaseType, PhaseContextType<T> type, T  context) {
        PhaseManager<T> manager = type.manager();
        manager.register(phaseType,context.getPhase(),context);
    }

    @SafeVarargs
    public final <T extends IPhaseContext> void register(PhaseType phaseType, PhaseContextType<T> type, T... contexts) {
        PhaseManager<T> manager = type.manager();
        for (T ctx : contexts) {
            manager.register(phaseType,ctx.getPhase(),ctx);
        }
    }


}
