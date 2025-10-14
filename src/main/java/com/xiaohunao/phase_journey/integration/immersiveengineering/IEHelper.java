package com.xiaohunao.phase_journey.integration.immersiveengineering;

import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.integration.immersiveengineering.phase.IEMultiblockPhaseContext;
import com.xiaohunao.phase_journey.integration.immersiveengineering.phase.IEMultiblockPhaseManager;
import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class IEHelper {

    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, "immersiveengineering");

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<IEMultiblockPhaseContext>> MULTIBLOCK = CONDITION_CODEC.registerStatic("multiblock", () -> new PhaseContextType<>(IEMultiblockPhaseContext.class, IEMultiblockPhaseManager.MANAGER));

    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(IEMultiblockPhaseManager.MANAGER);
        CONDITION_CODEC.register(modEventBus);
    }
}
