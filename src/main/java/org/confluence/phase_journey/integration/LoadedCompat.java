package org.confluence.phase_journey.integration;

import org.confluence.phase_journey.integration.curios.CuriosHelper;
import org.confluence.phase_journey.integration.projecte.ProjecteHelper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public class LoadedCompat {

    public static final boolean PROJECTE = isLoaded("projecte");
    public static final boolean IE = isLoaded("immersiveengineering");
    public static final boolean KJS = isLoaded("kubejs");
    public static final boolean CURIOS = isLoaded("curios");

    public static void register(IEventBus modEventBus) {
        if (KJS) {
//            com.xiaohunao.heaven_destiny_moment.compat.kubejs.MomentRegister.MOMENT_TYPE.register(modEventBus);
//            com.xiaohunao.heaven_destiny_moment.compat.kubejs.MomentRegister.MOMENT_CODEC.register(modEventBus);
        }

        if (PROJECTE) {
            ProjecteHelper.register(modEventBus);
        }

        if (IE) {
            org.confluence.phase_journey.integration.immersiveengineering.IEHelper.register(modEventBus);
        }

        if (CURIOS) {
            CuriosHelper.register(modEventBus);
        }
    }

    public static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
