package com.xiaohunao.phase_journey.integration.kubejs;

import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public class ModKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(PhaseJourneyEvents.GROUP);
    }

    @Override
    public void initStartup() {
        ModList.get().getModContainerById(PhaseJourney.MODID).ifPresent(container -> {
            IEventBus eventBus = container.getEventBus();
            if (eventBus != null) eventBus.addListener(PhaseJourneyEvent.Register.class, event -> {
                if (PhaseJourneyEvents.REGISTER.hasListeners()) {
                    PhaseJourneyEvents.REGISTER.post(new RegisterEventJS(event));
                }
            });
        });
    }
}