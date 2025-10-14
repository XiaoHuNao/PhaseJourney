package org.confluence.phase_journey.mixin.recipe.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = CokeOvenLogic.class, remap = false)
public abstract class CokeOvenLogicMixin {
    @Shadow @Nullable public abstract CokeOvenRecipe getRecipe(IMultiblockContext<CokeOvenLogic.State> context);

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    public void phasejourney$tickServer(IMultiblockContext<CokeOvenLogic.State> context, CallbackInfo ci) {
        CokeOvenRecipe recipe = getRecipe(context);
        RecipeManager recipeManager = context.getLevel().getRawLevel().getRecipeManager();

        if (recipe != null) {
            for (RecipeHolder<?> holder : recipeManager.byType.get(recipe.getType())) {
                if (holder.value().equals(recipe)) {
                    if (RecipePhaseManager.MANAGER.isRestricted(context.getLevel().getRawLevel(),context.getLevel().getAbsoluteOrigin(),recipe.getType(),holder.id())){
                        ci.cancel();
                    }
                }
            }

        }
    }


}
