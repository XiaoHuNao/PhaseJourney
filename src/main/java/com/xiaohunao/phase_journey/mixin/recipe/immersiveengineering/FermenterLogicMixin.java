package com.xiaohunao.phase_journey.mixin.recipe.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FermenterLogic;
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

@Mixin(value = FermenterLogic.class, remap = false)
public class FermenterLogicMixin {

    @Unique
    private BlockPos phasejourney$AbsoluteOrigin;

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    public void phasejourney$tickServer(IMultiblockContext<FermenterLogic.State> context, CallbackInfo ci) {
        phasejourney$AbsoluteOrigin = context.getLevel().getAbsoluteOrigin();
    }

    @Redirect(
            method = "enqueueNewProcesses",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/immersiveengineering/api/crafting/FermenterRecipe;findRecipe(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/crafting/RecipeHolder;"
            )
    )
    public RecipeHolder<FermenterRecipe> phasejourney$guardFindRecipe(
            Level level,
            ItemStack stack,
            IMultiblockContext<FermenterLogic.State> ctx
    ) {
        for (RecipeHolder<FermenterRecipe> holder : FermenterRecipe.RECIPES.getRecipes(level)) {
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

