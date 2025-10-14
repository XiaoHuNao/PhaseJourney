package com.xiaohunao.phase_journey.mixin.recipe.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.SqueezerLogic;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SqueezerLogic.class, remap = false)
public class SqueezerLogicMixin {

    @Unique
    private BlockPos phasejourney$AbsoluteOrigin;

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    public void phasejourney$tickServer(IMultiblockContext<SqueezerLogic.State> context, CallbackInfo ci) {
        phasejourney$AbsoluteOrigin = context.getLevel().getAbsoluteOrigin();
    }

    @Redirect(
            method = "enqueueProcesses",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/immersiveengineering/api/crafting/SqueezerRecipe;findRecipe(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/crafting/RecipeHolder;"
            )
    )
    public RecipeHolder<SqueezerRecipe> phasejourney$guardFindRecipe(Level level, ItemStack stack) {
        for (RecipeHolder<SqueezerRecipe> holder : SqueezerRecipe.RECIPES.getRecipes(level)) {
            if (holder.value().input.test(stack)) {
                if (phasejourney$AbsoluteOrigin != null && RecipePhaseManager.MANAGER.isRestricted(
                        level,
                        phasejourney$AbsoluteOrigin,
                        holder.value().getType(),
                        holder.id()
                )) {
                    return null;
                }
                return holder;
            }
        }
        return null;
    }
}


