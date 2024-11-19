package org.confluence.phase_journey.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemModelShaper.class)
public abstract class ItemModelShaperMixin {
    @Shadow
    @Nullable
    public abstract BakedModel getItemModel(Item pItem);

    @Inject(at = @At("HEAD"), method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", cancellable = true)
    private void getModel(ItemStack itemStack, CallbackInfoReturnable<BakedModel> callback) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemPhaseManager.INSTANCE.applyTargetIfPhaseIsNotAchieved(player, itemStack.getItem(), target -> {
            callback.setReturnValue(getItemModel(target));
        });
    }
}