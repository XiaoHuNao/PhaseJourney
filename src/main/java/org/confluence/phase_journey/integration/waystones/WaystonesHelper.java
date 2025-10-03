package org.confluence.phase_journey.integration.waystones;

import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.phase_journey.common.init.PJRegistries;
import org.confluence.phase_journey.common.phase.PhaseContextType;
import org.confluence.phase_journey.integration.waystones.phase.WaystoneTeleportPhaseContext;
import org.confluence.phase_journey.integration.waystones.phase.WaystoneTeleportPhaseManager;

public class WaystonesHelper {
    public static final String MODID = "waystones";

    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, MODID);

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<WaystoneTeleportPhaseContext>> TELEPORT = CONDITION_CODEC.registerStatic(
            "teleport",
            () -> new PhaseContextType<>(WaystoneTeleportPhaseContext.class, WaystoneTeleportPhaseManager.MANAGER)
    );

    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(WaystoneTeleportPhaseManager.MANAGER);
        CONDITION_CODEC.register(modEventBus);
    }
}


