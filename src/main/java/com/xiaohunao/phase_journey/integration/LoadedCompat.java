package com.xiaohunao.phase_journey.integration;

import com.xiaohunao.phase_journey.integration.create.CreateHelper;
import com.xiaohunao.phase_journey.integration.curios.CuriosHelper;
import com.xiaohunao.phase_journey.integration.immersiveengineering.IEHelper;
import com.xiaohunao.phase_journey.integration.ironsspellbooks.ISSHelper;
import com.xiaohunao.phase_journey.integration.projecte.ProjecteHelper;
import com.xiaohunao.phase_journey.integration.touhou_little_maid.TouhouLittleMaidHelper;
import com.xiaohunao.phase_journey.integration.waystones.WaystonesHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public class LoadedCompat {

    public static final boolean PROJECTE = isLoaded("projecte");
    public static final boolean IE = isLoaded("immersiveengineering");
    public static final boolean KJS = isLoaded("kubejs");
    public static final boolean CURIOS = isLoaded("curios");
    public static final boolean ISS = isLoaded(ISSHelper.MODID);
    public static final boolean WAYSTONES = isLoaded(WaystonesHelper.MODID);
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
            IEHelper.register(modEventBus);
        }

        if (CURIOS) {
            CuriosHelper.register(modEventBus);
        }

        if (ISS) {
            ISSHelper.register(modEventBus);
        }

        if (WAYSTONES) {
            WaystonesHelper.register(modEventBus);
        }

        if (TOUHOU_LITTLE_MAID) {
            TouhouLittleMaidHelper.register(modEventBus);
        }

        if (CREATE){
            CreateHelper.register(modEventBus);

        }
    }

    public static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
