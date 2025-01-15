package org.confluence.phase_journey.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.api.PhaseJourneyEvent;

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