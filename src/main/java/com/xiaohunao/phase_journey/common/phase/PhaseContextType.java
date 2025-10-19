package com.xiaohunao.phase_journey.common.phase;

import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.api.phase.PhaseManager;

public record PhaseContextType<T extends IPhaseContext>(Class<T> clazz, PhaseManager<T> manager) {

}
