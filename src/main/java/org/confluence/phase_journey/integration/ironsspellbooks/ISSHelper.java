package org.confluence.phase_journey.integration.ironsspellbooks;

import org.confluence.phase_journey.common.init.PJRegistries;
import org.confluence.phase_journey.common.phase.PhaseContextType;
import org.confluence.phase_journey.integration.ironsspellbooks.phase.SpellCastPhaseContext;
import org.confluence.phase_journey.integration.ironsspellbooks.phase.SpellCastPhaseManager;

import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class ISSHelper {

    public static final String MODID = "irons_spellbooks";

    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, MODID);

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<SpellCastPhaseContext>> SPELL_CAST = CONDITION_CODEC.registerStatic("spell_cast", () -> new PhaseContextType<>(SpellCastPhaseContext.class, SpellCastPhaseManager.MANAGER));

    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(SpellCastPhaseManager.MANAGER);
        CONDITION_CODEC.register(modEventBus);
    }
}
