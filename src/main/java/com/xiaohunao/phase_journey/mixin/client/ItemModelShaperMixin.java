package com.xiaohunao.phase_journey.mixin.client;

import com.xiaohunao.phase_journey.common.phase.item.ItemPhaseManager;
import com.xiaohunao.phase_journey.common.phase.item.ItemReplacementContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(ItemModelShaper.class)
public abstract class ItemModelShaperMixin {
    @Shadow
    @Nullable
    public abstract BakedModel getItemModel(Item pItem);

    @Inject(at = @At("HEAD"), method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", cancellable = true)
    private void getModel(ItemStack itemStack, CallbackInfoReturnable<BakedModel> callback) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemPhaseManager.MANAGER.isRestricted(player.level(), null, player,ctx -> {
            ItemReplacementContext itemReplacementContext = ItemPhaseManager.MANAGER.getItemReplacementContext(itemStack.getItem());
            return ctx.equals(itemReplacementContext) ? itemReplacementContext.getTarget() : null;
        }, item -> callback.setReturnValue(getItemModel(item)));
    }
}