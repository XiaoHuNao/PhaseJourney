package org.confluence.phase_journey.api;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.confluence.phase_journey.common.phase.*;

public class PhaseJourneyEvent extends Event {
    public static class Add extends PhaseJourneyEvent implements ICancellableEvent {
        public final ResourceLocation phase;

        public Add(ResourceLocation phase) {
            this.phase = phase;
        }
    }

    public static class Remove extends PhaseJourneyEvent implements ICancellableEvent {
        public final ResourceLocation phase;

        public Remove(ResourceLocation phase) {
            this.phase = phase;
        }
    }

    public static class Register extends PhaseJourneyEvent implements IModBusEvent {

        public <T extends PhaseContext> void register(PhaseContextType<T> type, T  context) {
            PhaseManager<T> manager = type.manager();
            manager.register(context.getPhase(), context);
        }

    }
}
