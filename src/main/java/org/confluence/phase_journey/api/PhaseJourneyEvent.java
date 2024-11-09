package org.confluence.phase_journey.api;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.confluence.phase_journey.common.phase.PhaseRegisterContext;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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
        private final Set<ResourceLocation> contexts = new HashSet<>();

        public void phaseRegister(ResourceLocation phase, Consumer<PhaseRegisterContext> consumer) {
            consumer.accept(new PhaseRegisterContext(phase));
            contexts.add(phase);
        }

        @ApiStatus.Internal
        public void replaceBlockProperties() {
            contexts.forEach(BlockPhaseManager.INSTANCE::replaceBlockProperties);
        }
    }
}
