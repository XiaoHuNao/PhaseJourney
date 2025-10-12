package org.confluence.phase_journey.mixin.recipe.create;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = CrushingWheelControllerBlockEntity.class, remap = false)
public class CrushingWheelControllerBlockEntityMixin {
    //CUTTING
    @Inject(method = "findRecipe", at = @At("RETURN"), cancellable = true)
    private void phaseJourney$checkCrushingRecipePhase(CallbackInfoReturnable<Optional<RecipeHolder<ProcessingRecipe<RecipeWrapper>>>> cir) {
        CrushingWheelControllerBlockEntity blockEntity = (CrushingWheelControllerBlockEntity) (Object) this;
        Optional<RecipeHolder<ProcessingRecipe<RecipeWrapper>>> result = cir.getReturnValue();
        if (result.isPresent() && RecipePhaseManager.MANAGER.isRestricted(blockEntity, result.get())) {
            cir.setReturnValue(Optional.empty());
        }
    }
}