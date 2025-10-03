package org.confluence.phase_journey.integration.projecte;

import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import moze_intel.projecte.api.ProjectEAPI;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.phase_journey.common.init.PJRegistries;
import org.confluence.phase_journey.common.phase.PhaseContextType;
import org.confluence.phase_journey.integration.projecte.phase.TransmutationPhaseContext;
import org.confluence.phase_journey.integration.projecte.phase.TransmutationPhaseManager;

public class ProjecteHelper {
    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, ProjectEAPI.PROJECTE_MODID);

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<TransmutationPhaseContext>> TRANSMUTATION = CONDITION_CODEC.registerStatic("transmutation", () -> new PhaseContextType<>(TransmutationPhaseContext.class, TransmutationPhaseManager.MANAGER));


    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(TransmutationPhaseManager.MANAGER);
        CONDITION_CODEC.register(modEventBus);
    }
}
