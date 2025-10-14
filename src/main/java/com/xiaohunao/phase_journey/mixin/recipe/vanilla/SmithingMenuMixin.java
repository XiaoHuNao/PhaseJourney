package com.xiaohunao.phase_journey.mixin.recipe.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SmithingMenu.class, remap = false)
public abstract class SmithingMenuMixin {
    @Shadow @Final private Level level;

    @Unique
    public Player phasejourney$player;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    public void init(int containerId, Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.phasejourney$player = playerInventory.player;
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;)V", at = @At("RETURN"))
    public void init(int containerId, Inventory playerInventory, CallbackInfo ci) {
        this.phasejourney$player = playerInventory.player;
    }

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/SmithingRecipe;assemble(Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    public void phasejourney$createResult(CallbackInfo ci, @Local @NotNull RecipeHolder<SmithingRecipe> recipeHolder) {
        SmithingMenu menu = (SmithingMenu) (Object) this;
        if (RecipePhaseManager.MANAGER.isRestricted(this.level, this.phasejourney$player, recipeHolder)) {
            menu.resultSlots.clearContent();
            ci.cancel();
        }
    }



}
