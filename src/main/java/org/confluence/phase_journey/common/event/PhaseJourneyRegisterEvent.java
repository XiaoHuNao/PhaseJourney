package org.confluence.phase_journey.common.event;

import net.neoforged.bus.api.Event;
import org.confluence.phase_journey.common.phase.PhaseRegisterContext;

import java.util.function.Consumer;

public class PhaseJourneyRegisterEvent extends Event {
    public void phaseRegister(Consumer<PhaseRegisterContext> consumer) {
        consumer.accept(PhaseRegisterContext.INSTANCE);
    }
}
