package com.xiaohunao.phase_journey.mixin.recipe.create;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Pseudo
@Mixin(targets = "com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity", remap = false)
public abstract class CrushingWheelControllerBlockEntityMixin {
    //CUTTING
    @Inject(method = "findRecipe", at = @At("RETURN"), cancellable = true)
    private void phaseJourney$checkCrushingRecipePhase(CallbackInfoReturnable<Optional<RecipeHolder<StandardProcessingRecipe<RecipeWrapper>>>> cir) {
        CrushingWheelControllerBlockEntity blockEntity = (CrushingWheelControllerBlockEntity) (Object) this;
        Optional<RecipeHolder<StandardProcessingRecipe<RecipeWrapper>>> result = cir.getReturnValue();
        if (result.isPresent() && RecipePhaseManager.MANAGER.isRestricted(blockEntity, result.get())) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
