package org.confluence.phase_journey.common.event;

import net.minecraftforge.eventbus.api.Event;
import org.confluence.phase_journey.common.phase.PhaseRegisterContext;

import java.util.function.Consumer;

public class PhaseJourneyRegisterEvent extends Event {
    public void phaseRegister(Consumer<PhaseRegisterContext> consumer) {
        consumer.accept(PhaseRegisterContext.INSTANCE);
    }
}
