package com.xiaohunao.phase_journey.mixin.recipe.vanilla;

import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = StonecutterMenu.class, remap = false)
public abstract class StonecutterMenuMixin {
    @Shadow @Final private Level level;
    @Shadow private List<RecipeHolder<StonecutterRecipe>> recipes;
    @Unique
    public Player phasejourney$player;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;)V", at = @At("RETURN"))
    private void phasejourney$init(int containerId, Inventory playerInventory, CallbackInfo ci) {
        this.phasejourney$player = playerInventory.player;
    }

    @Inject(method = "slotsChanged", at = @At("RETURN"))
    private void phasejourney$slotsChanged(Container inventory, CallbackInfo ci) {
        List<RecipeHolder<StonecutterRecipe>> remove = new ArrayList<>();
        for (RecipeHolder<StonecutterRecipe> recipe : recipes) {
            if (RecipePhaseManager.MANAGER.isRestricted(this.level, this.phasejourney$player, recipe)) {
                remove.add(recipe);
            }
        }
        recipes.removeAll(remove);
    }
    
}