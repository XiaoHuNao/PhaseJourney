package com.xiaohunao.phase_journey.mixin.recipe.create;

import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Pseudo
@Mixin(targets = "com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity", remap = false)
public abstract class DeployerBlockEntityMixin {
    //SANDPAPER_POLISHING
    @Inject(method = "getRecipe", at = @At("RETURN"), cancellable = true)
    public void phaseJourney$getRecipe(ItemStack item, CallbackInfoReturnable<Optional<RecipeHolder<PressingRecipe>>> cir) {
        DeployerBlockEntity blockEntity = (DeployerBlockEntity) (Object) this;
        Optional<RecipeHolder<PressingRecipe>> result = cir.getReturnValue();
        if (result.isPresent() && RecipePhaseManager.MANAGER.isRestricted(blockEntity, result.get())) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
