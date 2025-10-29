package com.xiaohunao.phase_journey.mixin.recipe.create;

import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(targets = "com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity", remap = false)
public abstract class BasinOperatingBlockEntityMixin {
    //MIXING
    //PRESSING
    @Inject(method = "getMatchingRecipes", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/recipe/RecipeFinder;get(Ljava/lang/Object;Lnet/minecraft/world/level/Level;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private void phaseJourney$filterRecipes(CallbackInfoReturnable<List<RecipeHolder<? extends Recipe<?>>>> cir) {
        BasinOperatingBlockEntity blockEntity = (BasinOperatingBlockEntity) (Object) this;
        List<RecipeHolder<? extends Recipe<?>>> returnValue = cir.getReturnValue();

        returnValue.removeIf(holder -> RecipePhaseManager.MANAGER.isRestricted(blockEntity, holder));
    }
}
