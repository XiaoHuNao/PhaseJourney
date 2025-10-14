package com.xiaohunao.phase_journey.integration.curios;

import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.integration.curios.phase.CuriosEquipPhaseContext;
import com.xiaohunao.phase_journey.integration.curios.phase.CuriosEquipPhaseManager;
import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class CuriosHelper {

    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, "curios");

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<CuriosEquipPhaseContext>> EQUIPPING = CONDITION_CODEC.registerStatic("equipping", () -> new PhaseContextType<>(CuriosEquipPhaseContext.class, CuriosEquipPhaseManager.MANAGER));

    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(CuriosEquipPhaseManager.MANAGER);
        CONDITION_CODEC.register(modEventBus);
    }
}
