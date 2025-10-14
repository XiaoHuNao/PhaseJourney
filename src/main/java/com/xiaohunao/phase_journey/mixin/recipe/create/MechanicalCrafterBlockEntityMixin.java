package com.xiaohunao.phase_journey.mixin.recipe.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingInput;
import com.simibubi.create.content.kinetics.crafter.RecipeGridHandler;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(value = MechanicalCrafterBlockEntity.class,remap = false)
public class MechanicalCrafterBlockEntityMixin {
    //MECHANICAL_CRAFTING
    @Redirect(method = "tick",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/crafter/RecipeGridHandler;tryToApplyRecipe(Lnet/minecraft/world/level/Level;Lcom/simibubi/create/content/kinetics/crafter/RecipeGridHandler$GroupedItems;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack phasejourney$tryToApplyRecipe(Level level, RecipeGridHandler.GroupedItems groupedItems) {
        MechanicalCrafterBlockEntity blockEntity = (MechanicalCrafterBlockEntity) (Object) this;
        groupedItems.calcStats();
        CraftingInput craftingInput = MechanicalCraftingInput.of(groupedItems);
        ItemStack result = null;
        RegistryAccess registryAccess = level.registryAccess();

        CRecipes recipes = AllConfigs.server().recipes;

        if (AllConfigs.server().recipes.allowRegularCraftingInCrafter.get()) {
            Optional<RecipeHolder<CraftingRecipe>> regularRecipe = level.getRecipeManager()
                    .getRecipeFor(RecipeType.CRAFTING, craftingInput, level)
                    .filter(r -> RecipeGridHandler.isRecipeAllowed(r, craftingInput));
            
            if (regularRecipe.isPresent() && !RecipePhaseManager.MANAGER.isRestricted(blockEntity, regularRecipe.get())) {
                result = regularRecipe.get().value().assemble(craftingInput, registryAccess);
            }
        }
        
        if (result == null) {
            Optional<RecipeHolder<Recipe<CraftingInput>>> recipeRecipeHolder = AllRecipeTypes.MECHANICAL_CRAFTING.find(craftingInput, level);

            if (recipeRecipeHolder.isPresent() && !RecipePhaseManager.MANAGER.isRestricted(blockEntity, recipeRecipeHolder.get())) {
                result = recipeRecipeHolder.get().value().assemble(craftingInput, registryAccess);
            }
        }
        return result;
    }
}
