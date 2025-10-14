package org.confluence.phase_journey.mixin.recipe.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.mixer.MixerLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MixerLogic.class, remap = false)
public class MixerLogicMixin {

    @Unique
    private BlockPos phasejourney$AbsoluteOrigin;

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    public void phasejourney$tickServer(IMultiblockContext<MixerLogic.State> context, CallbackInfo ci) {
        phasejourney$AbsoluteOrigin = context.getLevel().getAbsoluteOrigin();
    }

    @Redirect(
            method = "enqueueNewRecipes",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/immersiveengineering/api/crafting/MixerRecipe;findRecipe(Lnet/minecraft/world/level/Level;Lnet/neoforged/neoforge/fluids/FluidStack;Lnet/minecraft/core/NonNullList;)Lnet/minecraft/world/item/crafting/RecipeHolder;"
            )
    )
    public RecipeHolder<MixerRecipe> phasejourney$guardFindRecipe(
            Level level,
            FluidStack fluid,
            NonNullList<ItemStack> components
    ) {
        for (RecipeHolder<MixerRecipe> holder : MixerRecipe.RECIPES.getRecipes(level)) {
            if (holder.value().matches(fluid, components)) {
                if (phasejourney$AbsoluteOrigin != null && RecipePhaseManager.MANAGER.isRestricted(
                        level,
                        phasejourney$AbsoluteOrigin,
                        holder.value().getType(),
                        holder.id()
                )) {
                    return null; // 阶段限制：阻止入队
                }
                return holder;
            }
        }
        return null;
    }
}
