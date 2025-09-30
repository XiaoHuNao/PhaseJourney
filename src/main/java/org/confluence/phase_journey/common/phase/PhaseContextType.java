package org.confluence.phase_journey.common.phase;

public record PhaseContextType<T extends PhaseContext>(Class<T> clazz, PhaseManager<T> manager) {
}
