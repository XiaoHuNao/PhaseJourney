package com.xiaohunao.phase_journey.integration.kubejs;


import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import dev.latvian.mods.kubejs.event.KubeEvent;

public abstract class PhaseJourneyEventJS implements KubeEvent {
    public static class AddJS extends PhaseJourneyEventJS {
        public final PhaseJourneyEvent.Add event;

        public AddJS(PhaseJourneyEvent.Add event) {
            this.event = event;
        }
    }

    public static class RemoveJS extends PhaseJourneyEventJS {
        public final PhaseJourneyEvent.Remove event;

        public RemoveJS(PhaseJourneyEvent.Remove event) {
            this.event = event;
        }
    }


    public static class RegisterJS extends PhaseJourneyEventJS {
        public <T extends IPhaseContext> void register(PhaseType phaseType, PhaseContextType<T> type, T  context) {
            PhaseManager<T> manager = type.manager();
            manager.register(phaseType,context.getPhase(),context);
        }

        @SafeVarargs
        public final <T extends IPhaseContext> void registers(PhaseType phaseType, PhaseContextType<T> type, T... contexts) {
            PhaseManager<T> manager = type.manager();
            for (T ctx : contexts) {
                manager.register(phaseType,ctx.getPhase(),ctx);
            }
        }
    }
}


