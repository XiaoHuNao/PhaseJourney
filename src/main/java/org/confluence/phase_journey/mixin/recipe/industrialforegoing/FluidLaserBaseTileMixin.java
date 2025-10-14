package org.confluence.phase_journey.mixin.recipe.industrialforegoing;

import com.buuz135.industrial.block.resourceproduction.tile.FluidLaserBaseTile;
import com.buuz135.industrial.block.resourceproduction.tile.OreLaserBaseTile;
import com.google.common.collect.Multimap;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(value = FluidLaserBaseTile.class, remap = false)
public class FluidLaserBaseTileMixin {
    @Redirect(method = "onWork", at = @At(value = "INVOKE", target = "Lcom/hrznstudio/titanium/util/RecipeUtil;getRecipes(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/crafting/RecipeType;)Ljava/util/List;"))
    private <T extends Recipe<?>> List<T> phasejourney$onWork(Level level, RecipeType<T> recipeType) {
        OreLaserBaseTile blockEntity = (OreLaserBaseTile) (Object) this;
        if (level == null) {
            return new ArrayList<>();
        }

        Multimap<RecipeType<?>, RecipeHolder<?>> recipesByType = level.getRecipeManager().byType;
        Collection<RecipeHolder<?>> holders = recipesByType.get(recipeType);
        if (holders == null || holders.isEmpty()) {
            return new ArrayList<>();
        }

        List<RecipeHolder<?>> allowedHolders = holders.stream()
                .filter(holder -> !RecipePhaseManager.MANAGER.isRestricted(
                        level,
                        blockEntity.getBlockPos(),
                        recipeType,
                        holder.id()
                ))
                .toList();

        List<T> result = new ArrayList<>(allowedHolders.size());
        for (RecipeHolder<?> holder : allowedHolders) {
            @SuppressWarnings("unchecked")
            T recipe = (T) holder.value();
            result.add(recipe);
        }
        return result;
    }
}
