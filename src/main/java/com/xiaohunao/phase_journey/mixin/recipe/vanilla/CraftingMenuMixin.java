package com.xiaohunao.phase_journey.mixin.recipe.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin {
    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;"), cancellable = true)
    private static void phasejourney$slotChanged(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftSlots, ResultContainer resultSlots, RecipeHolder<CraftingRecipe> recipe, CallbackInfo ci, @Local ServerPlayer serverPlayer, @Local @NotNull Optional<RecipeHolder<CraftingRecipe>> optional) {
        if (optional.isPresent()) {
            RecipeHolder<CraftingRecipe> rec = optional.get();
            if (RecipePhaseManager.MANAGER.isRestricted(level, serverPlayer, rec)) {
                resultSlots.clearContent();
                ci.cancel();
            }
        }
    }
}
