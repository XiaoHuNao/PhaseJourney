package com.xiaohunao.phase_journey.mixin.recipe.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceLogic;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ArcFurnaceLogic.class, remap = false)
public class ArcFurnaceLogicMixin {

    @Unique
    private BlockPos phasejourney$AbsoluteOrigin;


    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    public void phasejourney$tickServer(IMultiblockContext<ArcFurnaceLogic.State> context, CallbackInfo ci) {
        phasejourney$AbsoluteOrigin = context.getLevel().getAbsoluteOrigin();
    }

    @Redirect(method = "enqueueProcesses",at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/crafting/ArcFurnaceRecipe;findRecipe(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/NonNullList;)Lnet/minecraft/world/item/crafting/RecipeHolder;"))
    public RecipeHolder<ArcFurnaceRecipe> phasejourney$enqueueProcesses(Level level, ItemStack input, NonNullList<ItemStack> additives){
        RecipeHolder<ArcFurnaceRecipe> recipe = null;
        for(RecipeHolder<ArcFurnaceRecipe> recipeHolder : ArcFurnaceRecipe.RECIPES.getRecipes(level)) {
            if (recipeHolder.value().matches(input, additives)) {
                recipe = recipeHolder;
                if (phasejourney$AbsoluteOrigin != null && RecipePhaseManager.MANAGER.isRestricted(level,phasejourney$AbsoluteOrigin,recipeHolder.value().getType(),recipeHolder.id())){
                    return null;
                }
            }
        }
        return recipe;
    }
}
