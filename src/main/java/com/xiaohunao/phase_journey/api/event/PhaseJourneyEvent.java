package com.xiaohunao.phase_journey.api.event;

import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;

public abstract class PhaseJourneyEvent extends Event {
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

        public <T extends IPhaseContext> void register(PhaseType phaseType, PhaseContextType<T> type, T  context) {
            PhaseManager<T> manager = type.manager();
            manager.register(phaseType,context.getPhase(),context);
        }

//        @SafeVarargs
//        public final <T extends IPhaseContext> void register(PhaseType phaseType, PhaseContextType<T> type, T... contexts) {
//            PhaseManager<T> manager = type.manager();
//            for (T ctx : contexts) {
//                manager.register(phaseType,ctx.getPhase(),ctx);
//            }
//        }
    }
}
