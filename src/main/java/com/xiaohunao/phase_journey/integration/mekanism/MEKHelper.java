package com.xiaohunao.phase_journey.integration.mekanism;

import mekanism.common.recipe.MekanismRecipeType;

public class MEKHelper {
    public static void registerKubeJSBindings(dev.latvian.mods.kubejs.script.BindingRegistry bindings) {
        bindings.add("MEKRecipeTypes", MekanismRecipeType.class);
    }
}
