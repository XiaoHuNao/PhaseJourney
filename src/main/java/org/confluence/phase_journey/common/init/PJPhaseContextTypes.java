package org.confluence.phase_journey.common.init;


import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.phase.PhaseContextType;
import org.confluence.phase_journey.common.phase.block.BlockPhaseContext;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.phase.dimension.DimensionPhaseContext;
import org.confluence.phase_journey.common.phase.dimension.DimensionPhaseManager;
import org.confluence.phase_journey.common.phase.item.ItemPhaseContext;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;

public class PJPhaseContextTypes {
    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, PhaseJourney.MODID);

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<BlockPhaseContext>> BLOCK = CONDITION_CODEC.registerStatic("block", () -> new PhaseContextType<>(BlockPhaseContext.class, BlockPhaseManager.MANAGER));
    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<ItemPhaseContext>> ITEM = CONDITION_CODEC.registerStatic("item", () -> new PhaseContextType<>(ItemPhaseContext.class, ItemPhaseManager.MANAGER));
    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<DimensionPhaseContext>> DIMENSION = CONDITION_CODEC.registerStatic("dimension", () -> new PhaseContextType<>(DimensionPhaseContext.class, DimensionPhaseManager.MANAGER));
}
