package org.confluence.phase_journey.integration;

import org.confluence.phase_journey.integration.curios.CuriosHelper;
import org.confluence.phase_journey.integration.hostilenetworks.HNNHelper;
import org.confluence.phase_journey.integration.ironsspellbooks.ISSHelper;
import org.confluence.phase_journey.integration.projecte.ProjecteHelper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public class LoadedCompat {

    public static final boolean PROJECTE = isLoaded("projecte");
    public static final boolean IE = isLoaded("immersiveengineering");
    public static final boolean KJS = isLoaded("kubejs");
    public static final boolean CURIOS = isLoaded("curios");
    public static final boolean ISS = isLoaded(ISSHelper.MODID);
    public static final boolean WAYSTONES = isLoaded(org.confluence.phase_journey.integration.waystones.WaystonesHelper.MODID);
    public static final boolean TOUHOU_LITTLE_MAID = isLoaded("touhou_little_maid");
    public static final boolean HOSTILE_NETWORKS = isLoaded("hostilenetworks");
    public static final boolean CREATE = isLoaded("create");


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

        if (ISS) {
            ISSHelper.register(modEventBus);
        }

        if (WAYSTONES) {
            org.confluence.phase_journey.integration.waystones.WaystonesHelper.register(modEventBus);
        }

        if (TOUHOU_LITTLE_MAID) {
            org.confluence.phase_journey.integration.touhou_little_maid.TouhouLittleMaidHelper.register(modEventBus);
        }

        if (CREATE){
            org.confluence.phase_journey.integration.create.CreateHelper.register(modEventBus);

        }
    }

    public static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
