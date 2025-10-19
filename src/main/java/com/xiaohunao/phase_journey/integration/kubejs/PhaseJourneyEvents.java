package com.xiaohunao.phase_journey.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface PhaseJourneyEvents {
    EventGroup GROUP = EventGroup.of("PhaseJourney");

    EventHandler REGISTER = GROUP.startup("register", () -> PhaseJourneyEventJS.RegisterJS.class);
    EventHandler ADD = GROUP.server("add", () -> PhaseJourneyEventJS.AddJS.class);
    EventHandler REMOVE = GROUP.server("remove", () -> PhaseJourneyEventJS.RemoveJS.class);
}
