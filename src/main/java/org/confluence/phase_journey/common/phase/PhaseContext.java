package org.confluence.phase_journey.common.phase;

import net.minecraft.resources.ResourceLocation;
import org.confluence.phase_journey.api.IPhaseContext;

public class PhaseContext implements IPhaseContext {
    protected ResourceLocation phase;

    public PhaseContext(ResourceLocation phase) {
        this.phase = phase;
    }

    public ResourceLocation getPhase() {
        return phase;
    }
    public String getPhaseName() {
        return phase.toString();
    }
}
