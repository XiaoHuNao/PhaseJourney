package com.xiaohunao.phase_journey.common.init;

import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class PJRegistries {
    public static final Registry<PhaseContextType<?>> PHASE_CONTEXT_TYPE = new RegistryBuilder<>(Keys.PHASE_CONTEXT_TYPE).create();

    public static final class Keys {
        public static final ResourceKey<Registry<PhaseContextType<?>>> PHASE_CONTEXT_TYPE = PhaseJourney.asResourceKey("phase_context_type");
    }

    public static void registerRegistries(NewRegistryEvent event) {
        event.register(PHASE_CONTEXT_TYPE);
    }
}
