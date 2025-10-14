package com.xiaohunao.phase_journey.mixin.recipe.create;


import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = SandPaperItem.class, remap = false)
public class SandPaperItemMixin {
    //SANDPAPER_POLISHING
    @Inject(method = "use",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/equipment/sandPaper/SandPaperPolishingRecipe;canPolish(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Z"), cancellable = true)
    private void phaseJourney$checkSandPaperRecipePhase(Level worldIn, Player playerIn, InteractionHand handIn, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        List<RecipeHolder<Recipe<SingleRecipeInput>>> matchingRecipes = SandPaperPolishingRecipe.getMatchingRecipes(worldIn, playerIn.getItemInHand(handIn));
        for (RecipeHolder<Recipe<SingleRecipeInput>> recipeHolder : matchingRecipes) {
            if (RecipePhaseManager.MANAGER.isRestricted(worldIn, playerIn, recipeHolder)) {
                cir.setReturnValue(InteractionResultHolder.fail(playerIn.getItemInHand(handIn)));
            }
        }
    }
}
