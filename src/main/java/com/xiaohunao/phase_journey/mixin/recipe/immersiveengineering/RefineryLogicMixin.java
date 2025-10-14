package com.xiaohunao.phase_journey.mixin.recipe.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.RefineryLogic;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RefineryLogic.class, remap = false)
public class RefineryLogicMixin {

    @Unique
    private BlockPos phasejourney$AbsoluteOrigin;

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    public void phasejourney$tickServer(IMultiblockContext<RefineryLogic.State> context, CallbackInfo ci) {
        phasejourney$AbsoluteOrigin = context.getLevel().getAbsoluteOrigin();
    }

    @Redirect(
            method = "tryEnqueueProcess",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/immersiveengineering/api/crafting/RefineryRecipe;findRecipe(Lnet/minecraft/world/level/Level;Lnet/neoforged/neoforge/fluids/FluidStack;Lnet/neoforged/neoforge/fluids/FluidStack;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/crafting/RecipeHolder;"
            )
    )
    public RecipeHolder<RefineryRecipe> phasejourney$guardFindRecipe(
            Level level,
            FluidStack left,
            FluidStack right,
            ItemStack catalyst
    ) {
        RecipeHolder<RefineryRecipe> holder = RefineryRecipe.findRecipe(level, left, right, catalyst);
        if (holder != null && phasejourney$AbsoluteOrigin != null) {
            if (RecipePhaseManager.MANAGER.isRestricted(
                    level,
                    phasejourney$AbsoluteOrigin,
                    holder.value().getType(),
                    holder.id()
            )) {
                return null;
            }
        }
        return holder;
    }
}


