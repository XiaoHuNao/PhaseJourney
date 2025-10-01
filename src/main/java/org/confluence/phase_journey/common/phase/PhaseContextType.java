package org.confluence.phase_journey.common.phase;

import org.confluence.phase_journey.api.IPhaseContext;
import org.confluence.phase_journey.common.phase.item.ItemPhaseContext;

public record PhaseContextType<T extends IPhaseContext>(Class<T> clazz, PhaseManager<T> manager) {
}
