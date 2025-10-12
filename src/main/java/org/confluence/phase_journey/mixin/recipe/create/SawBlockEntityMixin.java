package org.confluence.phase_journey.mixin.recipe.create;

import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(value = SawBlockEntity.class,remap = false)
public class SawBlockEntityMixin {
    @Inject(method = "getRecipes", at = @At("RETURN"), cancellable = true)
    public void phaseJourney$getRecipes(CallbackInfoReturnable<List<RecipeHolder<? extends Recipe<?>>>> cir) {
        SawBlockEntity blockEntity = (SawBlockEntity) (Object) this;
        List<RecipeHolder<? extends Recipe<?>>> returnValue = cir.getReturnValue();
        returnValue.removeIf(holder -> RecipePhaseManager.MANAGER.isRestricted(blockEntity, holder));
        cir.setReturnValue(returnValue);
    }
}
