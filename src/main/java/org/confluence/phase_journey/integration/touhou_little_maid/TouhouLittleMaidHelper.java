package org.confluence.phase_journey.integration.touhou_little_maid;

import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.phase_journey.common.init.PJRegistries;
import org.confluence.phase_journey.common.phase.PhaseContextType;
import org.confluence.phase_journey.integration.touhou_little_maid.phase.TouhouLittleMaidPhaseContext;
import org.confluence.phase_journey.integration.touhou_little_maid.phase.TouhouLittleMaidPhaseManager;

public class TouhouLittleMaidHelper {

    public static final FlexibleRegister<PhaseContextType<?>> PHASE_CONTEXT_TYPE = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, "touhou_little_maid");

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<TouhouLittleMaidPhaseContext>> MAID_TASK_CONTEXT = PHASE_CONTEXT_TYPE.registerStatic("touhou_little_maid", () -> new PhaseContextType<>(TouhouLittleMaidPhaseContext.class, TouhouLittleMaidPhaseManager.MANAGER));

    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(TouhouLittleMaidPhaseManager.MANAGER);
        PHASE_CONTEXT_TYPE.register(modEventBus);
    }
}
