package org.confluence.phase_journey.integration.kubejs;

import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import net.minecraft.resources.ResourceLocation;
import org.confluence.phase_journey.api.PhaseJourneyEvent;
import org.confluence.phase_journey.common.phase.PhaseRegisterContext;

import java.util.function.Consumer;

public class RegisterEventJS implements KubeStartupEvent {
    private final PhaseJourneyEvent.Register event;

    public RegisterEventJS(PhaseJourneyEvent.Register event) {
        this.event = event;
    }

    public void phaseRegister(ResourceLocation phase, Consumer<PhaseRegisterContext> consumer) {
        event.phaseRegister(phase, consumer);
    }
}
