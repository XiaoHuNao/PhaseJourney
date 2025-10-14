package org.confluence.phase_journey.mixin.recipe.industrialforegoing;

import com.buuz135.industrial.block.resourceproduction.tile.MaterialStoneWorkFactoryTile;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(value = MaterialStoneWorkFactoryTile.class, remap = false)
public class MaterialStoneWorkFactoryTileMixin {

    @Redirect(method = "getRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getAllRecipesFor(Lnet/minecraft/world/item/crafting/RecipeType;)Ljava/util/List;"))
    private <T extends Recipe<?>> List<RecipeHolder<T>> phasejourney$getRecipe(RecipeManager recipeManager, RecipeType<T> recipeType) {
        MaterialStoneWorkFactoryTile blockEntity = (MaterialStoneWorkFactoryTile) (Object) this;
        Level level = blockEntity.getLevel();
        Collection<RecipeHolder<?>> recipeHolders = recipeManager.byType.get(recipeType);
        if (recipeHolders == null || recipeHolders.isEmpty()) {
            return new ArrayList<>();
        }

        List<RecipeHolder<?>> allowed = recipeHolders.stream()
                .filter(holder -> level == null || !RecipePhaseManager.MANAGER.isRestricted(
                level,
                blockEntity.getBlockPos(),
                recipeType,
                holder.id()
        ))
        .toList();

        List<RecipeHolder<T>> typed = new ArrayList<>(allowed.size());
        for (RecipeHolder<?> holder : allowed) {
            @SuppressWarnings("unchecked")
            RecipeHolder<T> casted = (RecipeHolder<T>) holder;
            typed.add(casted);
        }
        return typed;
    }
}
