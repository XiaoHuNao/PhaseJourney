package org.confluence.phase_journey.mixin.recipe.industrialforegoing;

import com.buuz135.industrial.block.core.tile.DissolutionChamberTile;
import com.buuz135.industrial.block.core.tile.FluidExtractorTile;
import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.recipe.DissolutionChamberRecipe;
import com.buuz135.industrial.recipe.FluidExtractorRecipe;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidExtractorTile.class, remap = false)
public class FluidExtractorTileMixin {
    @Shadow private FluidExtractorRecipe currentRecipe;

    @Inject(method = "findRecipe" ,at = @At("RETURN"))
    private void phasejourney$findRecipe(Level world, BlockPos pos, CallbackInfoReturnable<FluidExtractorRecipe> cir){
        if(this.currentRecipe == null) return;
        FluidExtractorTile blockEntity = (FluidExtractorTile) (Object) this;
        Level level = blockEntity.getLevel();
        if (level != null) {
            Multimap<RecipeType<?>, RecipeHolder<?>> recipes = level.getRecipeManager().byType;
            for (RecipeHolder<?> holder : recipes.get(ModuleCore.FLUID_EXTRACTOR_TYPE.get())) {
                if (holder.value() == currentRecipe) {
                    if (RecipePhaseManager.MANAGER.isRestricted(level, blockEntity.getBlockPos(), currentRecipe.getType(), holder.id())){
                        this.currentRecipe = null;
                    }
                }
            }
        }
    }
}
