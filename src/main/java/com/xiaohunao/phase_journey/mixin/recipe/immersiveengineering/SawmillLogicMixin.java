package com.xiaohunao.phase_journey.mixin.recipe.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.SawmillRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.sawmill.SawmillLogic;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SawmillLogic.class, remap = false)
public abstract class SawmillLogicMixin {
    @Shadow
    private static boolean insertItemToProcess(ItemStack stack, boolean simulate, SawmillLogic.State state, Level rawLevel) {
        return false;
    }

    @Redirect(
            method = "onEntityCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/immersiveengineering/common/blocks/multiblocks/logic/sawmill/SawmillLogic;insertItemToProcess(Lnet/minecraft/world/item/ItemStack;ZLblusunrize/immersiveengineering/common/blocks/multiblocks/logic/sawmill/SawmillLogic$State;Lnet/minecraft/world/level/Level;)Z"
            )
    )
    private static boolean phasejourney$guardInsertItemToProcess(
            ItemStack stack,
            boolean simulate,
            SawmillLogic.State state,
            Level rawLevel,
            IMultiblockContext<SawmillLogic.State> ctx,
            BlockPos posInMultiblock,
            Entity collided
    ) {
        // Determine matching recipe
        RecipeHolder<SawmillRecipe> recipe = null;
        for (RecipeHolder<SawmillRecipe> holder : SawmillRecipe.RECIPES.getRecipes(rawLevel)) {
            if (holder.value().input.test(stack)) {
                recipe = holder;
                break;
            }
        }
        if (recipe != null) {
            BlockPos origin = ctx.getLevel().getAbsoluteOrigin();
            if (origin != null && RecipePhaseManager.MANAGER.isRestricted(
                    rawLevel,
                    origin,
                    recipe.value().getType(),
                    recipe.id()
            )) {
                return false;
            }
        }
        return insertItemToProcess(stack, simulate, state, rawLevel);
    }


}


