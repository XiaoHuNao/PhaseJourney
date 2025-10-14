package com.xiaohunao.phase_journey.mixin.recipe.create;

import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

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
