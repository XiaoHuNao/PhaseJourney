package org.confluence.phase_journey.mixin.recipe.mekanism;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import mekanism.common.recipe.lookup.monitor.RecipeCacheLookupMonitor;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Mixin(value = RecipeCacheLookupMonitor.class, remap = false)
public class RecipeCacheLookupMonitorMixin<RECIPE extends MekanismRecipe<?>> {

    @Shadow
    protected CachedRecipe<RECIPE> cachedRecipe;
    @Shadow
    @Final
    protected int cacheIndex;

    @Shadow
    @Final
    private IRecipeLookupHandler<RECIPE> handler;

    @Inject(method = "updateAndProcess()Z", at = @At("HEAD"), cancellable = true)
    public void phasejourney$updateAndProcess(CallbackInfoReturnable<Boolean> cir) {
        if(cachedRecipe == null || !(handler instanceof BlockEntity blockEntity)){
            return;
        }
        Level level = blockEntity.getLevel();
        if (cachedRecipe.getRecipe().getType() instanceof MekanismRecipeType<?,?,?> mekanismRecipeType){
            for (RecipeHolder<?> recipeHolder : mekanismRecipeType.getRecipes(level)) {
                if (cachedRecipe != null && recipeHolder.value() == cachedRecipe.getRecipe()) {
                    if (RecipePhaseManager.MANAGER.isRestricted(blockEntity, mekanismRecipeType, recipeHolder.id())){
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }
}
