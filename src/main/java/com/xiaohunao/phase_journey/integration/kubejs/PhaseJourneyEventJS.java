package com.xiaohunao.phase_journey.integration.kubejs;


import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
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
        public final PhaseJourneyEvent.Register event;

        public RegisterJS(PhaseJourneyEvent.Register event) {
            this.event = event;
        }
    }
}


