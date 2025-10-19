package com.xiaohunao.phase_journey.integration;

import com.xiaohunao.phase_journey.integration.create.CreateHelper;
import com.xiaohunao.phase_journey.integration.curios.CuriosHelper;
import com.xiaohunao.phase_journey.integration.hostilenetworks.HNNHelper;
import com.xiaohunao.phase_journey.integration.immersiveengineering.IEHelper;
import com.xiaohunao.phase_journey.integration.industrialforegoing.IFHelper;
import com.xiaohunao.phase_journey.integration.ironsspellbooks.ISSHelper;
import com.xiaohunao.phase_journey.integration.kubejs.KJSHelper;
import com.xiaohunao.phase_journey.integration.mekanism.MEKHelper;
import com.xiaohunao.phase_journey.integration.projecte.ProjecteHelper;
import com.xiaohunao.phase_journey.integration.touhou_little_maid.TouhouLittleMaidHelper;
import com.xiaohunao.phase_journey.integration.waystones.WaystonesHelper;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public class LoadedCompat {
    public static final boolean KJS = isLoaded("kubejs");
    public static final boolean PROJECTE = isLoaded("projecte");
    public static final boolean IE = isLoaded("immersiveengineering");
    public static final boolean CURIOS = isLoaded("curios");
    public static final boolean ISS = isLoaded("irons_spellbooks");
    public static final boolean WAYSTONES = isLoaded("waystones");
    public static final boolean TOUHOU_LITTLE_MAID = isLoaded("touhou_little_maid");
    public static final boolean HOSTILE_NETWORKS = isLoaded("hostilenetworks");
    public static final boolean CREATE = isLoaded("create");
    public static final boolean IF = isLoaded("industrialforegoing");
    public static final boolean MEK = isLoaded("mekanism");


    public static void register(IEventBus modEventBus) {
        if (KJS){
            KJSHelper.init(modEventBus);
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

    public static void registerKubeJSBindings(BindingRegistry bindings) {
        if (CREATE){
            CreateHelper.registerKubeJSBindings(bindings);
        }

        if (CURIOS){
            CuriosHelper.registerKubeJSBindings(bindings);
        }

        if (HOSTILE_NETWORKS){
            HNNHelper.registerKubeJSBindings(bindings);
        }

        if (IE){
            IEHelper.registerKubeJSBindings(bindings);
        }

        if(ISS){
            ISSHelper.registerKubeJSBindings(bindings);
        }

        if(PROJECTE){
            ProjecteHelper.registerKubeJSBindings(bindings);
        }

        if(TOUHOU_LITTLE_MAID){
            TouhouLittleMaidHelper.registerKubeJSBindings(bindings);
        }

        if(WAYSTONES){
            WaystonesHelper.registerKubeJSBindings(bindings);
        }

        if (IF){
            IFHelper.registerKubeJSBindings(bindings);
        }

        if (MEK){
            MEKHelper.registerKubeJSBindings(bindings);
        }
    }
}
