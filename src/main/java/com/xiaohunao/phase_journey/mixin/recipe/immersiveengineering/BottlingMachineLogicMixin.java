package com.xiaohunao.phase_journey.mixin.recipe.immersiveengineering;


import blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.bottling_machine.BottlingMachineLogic;
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

@Mixin(value = BottlingMachineLogic.class, remap = false)
public class BottlingMachineLogicMixin {
    @Unique
    private BlockPos phasejourney$AbsoluteOrigin;


    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    public void phasejourney$tickServer(IMultiblockContext<ArcFurnaceLogic.State> context, CallbackInfo ci) {
        phasejourney$AbsoluteOrigin = context.getLevel().getAbsoluteOrigin();
    }

    @Redirect(method = "onEntityCollision",at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/crafting/BottlingMachineRecipe;findRecipe(Lnet/minecraft/world/level/Level;Lnet/neoforged/neoforge/fluids/FluidStack;[Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/crafting/RecipeHolder;"))
    public RecipeHolder<BottlingMachineRecipe> phasejourney$onEntityCollision(Level level, FluidStack fluid, ItemStack[] input){
        RecipeHolder<BottlingMachineRecipe> recipe = null;
        for(RecipeHolder<BottlingMachineRecipe> recipeHolder : BottlingMachineRecipe.RECIPES.getRecipes(level)) {
            if (recipeHolder.value().matches(input, fluid)) {
                recipe = recipeHolder;
                if (phasejourney$AbsoluteOrigin != null && RecipePhaseManager.MANAGER.isRestricted(level,phasejourney$AbsoluteOrigin,recipeHolder.value().getType(),recipeHolder.id())){
                    return null;
                }
            }
        }
        return recipe;
    }
}
