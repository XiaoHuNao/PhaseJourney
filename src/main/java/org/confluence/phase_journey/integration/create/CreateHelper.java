package org.confluence.phase_journey.integration.create;

import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import net.neoforged.bus.api.IEventBus;
import org.confluence.phase_journey.common.init.PJRegistries;
import org.confluence.phase_journey.common.phase.PhaseContextType;
import org.confluence.phase_journey.integration.create.phase.InfiniteFluidPoolContext;
import org.confluence.phase_journey.integration.create.phase.InfiniteFluidPoolManager;

public class CreateHelper {
    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, "create");

    public static final FlexibleHolder<PhaseContextType<?>,PhaseContextType<InfiniteFluidPoolContext>> INFINITE_FLUID_POOL = CONDITION_CODEC.registerStatic("infinite_fluid_pool", () -> new PhaseContextType<>(InfiniteFluidPoolContext.class, InfiniteFluidPoolManager.MANAGER));

    public static void register(IEventBus modEventBus) {
//        NeoForge.EVENT_BUS.register(InfiniteFluidPoolManager.MANAGER);
        CONDITION_CODEC.register(modEventBus);
    }
}
