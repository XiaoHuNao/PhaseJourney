package com.xiaohunao.phase_journey.integration.waystones;

import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.integration.waystones.phase.WaystoneTeleportPhaseContext;
import com.xiaohunao.phase_journey.integration.waystones.phase.WaystoneTeleportPhaseManager;
import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class WaystonesHelper {
    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, "waystones");

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<WaystoneTeleportPhaseContext>> TELEPORT = CONDITION_CODEC.registerStatic(
            "teleport",
            () -> new PhaseContextType<>(WaystoneTeleportPhaseContext.class, WaystoneTeleportPhaseManager.MANAGER)
    );

    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(WaystoneTeleportPhaseManager.MANAGER);
        CONDITION_CODEC.register(modEventBus);
    }

    public static void registerKubeJSBindings(BindingRegistry bindings) {
        bindings.add("WaystoneTeleportPhaseContext", WaystoneTeleportPhaseContext.class);
        bindings.add("WaystoneTeleportPhaseManager", WaystoneTeleportPhaseManager.MANAGER);
    }
}


