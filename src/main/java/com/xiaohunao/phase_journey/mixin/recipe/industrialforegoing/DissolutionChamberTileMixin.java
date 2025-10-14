package com.xiaohunao.phase_journey.mixin.recipe.industrialforegoing;

import com.buuz135.industrial.block.core.tile.DissolutionChamberTile;
import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.recipe.DissolutionChamberRecipe;
import com.google.common.collect.Multimap;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DissolutionChamberTile.class, remap = false)
public class DissolutionChamberTileMixin {

    @Shadow
    private DissolutionChamberRecipe currentRecipe;

    @Inject(method = "checkForRecipe" ,at = @At("RETURN"))
    private void phasejourney$checkForRecipe(CallbackInfo ci){
        if(this.currentRecipe == null) return;
        DissolutionChamberTile blockEntity = (DissolutionChamberTile) (Object) this;
        Level level = blockEntity.getLevel();
        if (level != null) {
            Multimap<RecipeType<?>, RecipeHolder<?>> recipes = level.getRecipeManager().byType;
            for (RecipeHolder<?> holder : recipes.get(ModuleCore.DISSOLUTION_TYPE.get())) {
                if (holder.value() == currentRecipe) {
                    if (RecipePhaseManager.MANAGER.isRestricted(level, blockEntity.getBlockPos(), currentRecipe.getType(), holder.id())){
                        this.currentRecipe = null;
                    }
                }
            }
        }
    }
}