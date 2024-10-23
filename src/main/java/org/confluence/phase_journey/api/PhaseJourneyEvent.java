package org.confluence.phase_journey.api;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import org.confluence.phase_journey.common.phase.PhaseRegisterContext;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;

import java.util.function.Consumer;

public class PhaseJourneyEvent extends Event {
    public static class Add extends PhaseJourneyEvent {
        public final ResourceLocation phase;

        public Add(ResourceLocation phase) {
            this.phase = phase;
        }
    }

    public static class Remove extends PhaseJourneyEvent {
        public final ResourceLocation phase;

        public Remove(ResourceLocation phase) {
            this.phase = phase;
        }
    }

    public static class Register extends PhaseJourneyEvent {
        public void phaseRegister(Consumer<PhaseRegisterContext> consumer) {
            consumer.accept(PhaseRegisterContext.INSTANCE);
            BlockPhaseManager.INSTANCE.ReplaceBlockBlockBehaviour(null, true);
        }
    }
}
