package org.confluence.phase_journey.mixin.recipe.immersiveengineering;

import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CrusherLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

@Mixin(value = CrusherLogic.class, remap = false)
public class CrusherLogicMixin {

    @Unique
    private BlockPos phasejourney$AbsoluteOrigin;

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    public void phasejourney$tickServer(IMultiblockContext<CrusherLogic.State> context, CallbackInfo ci) {
        phasejourney$AbsoluteOrigin = context.getLevel().getAbsoluteOrigin();
    }

    @Redirect(
            method = "onEntityCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/items/IItemHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    public ItemStack phasejourney$guardInsertOnCollision(
            IItemHandler handler,
            int slot,
            ItemStack stack,
            boolean simulate,
            IMultiblockContext<CrusherLogic.State> ctx,
            BlockPos posInMultiblock,
            Entity collided
    ) {
        Level level = ctx.getLevel().getRawLevel();
        RecipeHolder<CrusherRecipe> recipe = null;
        for (RecipeHolder<CrusherRecipe> holder : CrusherRecipe.RECIPES.getRecipes(level)) {
            if (holder.value().input.test(stack)) {
                recipe = holder;
                break;
            }
        }
        if (recipe != null && phasejourney$AbsoluteOrigin != null) {
            if (RecipePhaseManager.MANAGER.isRestricted(level, phasejourney$AbsoluteOrigin, recipe.value().getType(), recipe.id())) {
                return stack;
            }
        }
        return handler.insertItem(slot, stack, simulate);
    }
}
