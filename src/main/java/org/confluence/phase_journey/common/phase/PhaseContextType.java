package org.confluence.phase_journey.common.phase;

import org.confluence.phase_journey.api.phase.IPhaseContext;

public record PhaseContextType<T extends IPhaseContext>(Class<T> clazz, PhaseManager<T> manager) {
}
