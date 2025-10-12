package org.confluence.phase_journey.mixin.recipe.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(value = MillstoneBlockEntity.class, remap = false)
public class MillstoneBlockEntityMixin {
    //MILLING
    @Redirect(method = "tick",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/AllRecipeTypes;find(Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"))
    public <I extends RecipeInput> Optional<RecipeHolder<Recipe<I>>> phaseJourney$tick(AllRecipeTypes instance, I inv, Level world){
        return phaseJourney$find(instance, inv, world);
    }

    @Redirect(method = "process",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/AllRecipeTypes;find(Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"))
    public <I extends RecipeInput> Optional<RecipeHolder<Recipe<I>>> phaseJourney$process(AllRecipeTypes instance, I inv, Level world){
        return phaseJourney$find(instance, inv, world);
    }

    @Redirect(method = "canProcess",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/AllRecipeTypes;find(Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"))
    public <I extends RecipeInput> Optional<RecipeHolder<Recipe<I>>> phase_journey$canProcess(AllRecipeTypes instance, I inv, Level world){
        return phaseJourney$find(instance, inv, world);
    }

    private <I extends RecipeInput> Optional<RecipeHolder<Recipe<I>>> phaseJourney$find(AllRecipeTypes instance, I inv, Level world){
        MillstoneBlockEntity blockEntity = (MillstoneBlockEntity) (Object) this;
        Optional<RecipeHolder<Recipe<I>>> result = AllRecipeTypes.MILLING.find(inv, world);
        if (result.isPresent() && RecipePhaseManager.MANAGER.isRestricted(blockEntity, result.get())) {
            return Optional.empty();
        }
        return result;
    }
}
