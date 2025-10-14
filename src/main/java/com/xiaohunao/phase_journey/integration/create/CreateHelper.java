package com.xiaohunao.phase_journey.integration.create;

import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.integration.create.phase.InfiniteFluidPoolContext;
import com.xiaohunao.phase_journey.integration.create.phase.InfiniteFluidPoolManager;
import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import net.neoforged.bus.api.IEventBus;

public class CreateHelper {
    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, "create");

    public static final FlexibleHolder<PhaseContextType<?>,PhaseContextType<InfiniteFluidPoolContext>> INFINITE_FLUID_POOL = CONDITION_CODEC.registerStatic("infinite_fluid_pool", () -> new PhaseContextType<>(InfiniteFluidPoolContext.class, InfiniteFluidPoolManager.MANAGER));

    public static void register(IEventBus modEventBus) {
//        NeoForge.EVENT_BUS.register(InfiniteFluidPoolManager.MANAGER);
        CONDITION_CODEC.register(modEventBus);
    }
}
