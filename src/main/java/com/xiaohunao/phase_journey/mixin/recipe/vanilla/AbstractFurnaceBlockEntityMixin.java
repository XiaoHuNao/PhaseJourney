package com.xiaohunao.phase_journey.mixin.recipe.vanilla;

import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

    @Inject(method = "canBurn", at = @At("HEAD"), cancellable = true)
    private static void phasejourney$canBurn(RegistryAccess registryAccess, RecipeHolder<?> recipeHolder, NonNullList<ItemStack> inventory, int maxStackSize, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Boolean> cir) {
        Level level = furnace.getLevel();
        if (level != null && !level.isClientSide && recipeHolder != null) {
            if (RecipePhaseManager.MANAGER.isRestricted(furnace, recipeHolder)) {
                cir.setReturnValue(false);
            }
        }
    }
}
