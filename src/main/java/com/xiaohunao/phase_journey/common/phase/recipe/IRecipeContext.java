package com.xiaohunao.phase_journey.common.phase.recipe;

import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public interface IRecipeContext extends IPhaseContext {
   boolean isRestricted(RecipeType<?> recipeType, ResourceLocation recipeID);
}
