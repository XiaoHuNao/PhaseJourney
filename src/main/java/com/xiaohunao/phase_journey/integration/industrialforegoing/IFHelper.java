package com.xiaohunao.phase_journey.integration.industrialforegoing;

import com.buuz135.industrial.module.ModuleCore;
import dev.latvian.mods.kubejs.script.BindingRegistry;

public class IFHelper {

    public static void registerKubeJSBindings(BindingRegistry bindings) {
        bindings.add("IFRecipeTypes", ModuleCore.class);
    }
}
