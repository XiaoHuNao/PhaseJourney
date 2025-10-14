package org.confluence.phase_journey.mixin.recipe.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.MetalPressLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MetalPressLogic.class, remap = false)
public class MetalPressLogicMixin {

    @Unique
    private BlockPos phasejourney$AbsoluteOrigin;

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    public void phasejourney$tickServer(IMultiblockContext<MetalPressLogic.State> context, CallbackInfo ci) {
        phasejourney$AbsoluteOrigin = context.getLevel().getAbsoluteOrigin();
    }

    @Redirect(
            method = "onEntityCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/immersiveengineering/api/crafting/MetalPressRecipe;findRecipe(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/item/crafting/RecipeHolder;"
            )
    )
    public RecipeHolder<MetalPressRecipe> phasejourney$guardFindRecipe(
            ItemStack mold,
            ItemStack input,
            Level level,
            IMultiblockContext<MetalPressLogic.State> ctx
    ) {
        for (RecipeHolder<MetalPressRecipe> holder : MetalPressRecipe.STANDARD_RECIPES.getRecipes(level)) {
            if (holder.value().matches(mold, input, level)) {
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
