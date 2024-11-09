package org.confluence.phase_journey.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(at = @At("HEAD"), method = "getName", cancellable = true)
    public void getName(ItemStack itemStack, CallbackInfoReturnable<Component> callback) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemPhaseManager.INSTANCE.applyTargetIfPhaseIsNotAchieved(player, itemStack.getItem(), target -> {
            callback.setReturnValue(target.getDescription());
        });
    }
}