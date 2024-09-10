package org.confluence.phase_journey.common.mixin;

import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemModelShaper.class)
public abstract class ItemModelShaperMixin {

    @Shadow @Nullable public abstract BakedModel getItemModel(Item item);

    @Inject(at = @At("HEAD"), method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", cancellable = true)
    private void getModel(ItemStack itemStack, CallbackInfoReturnable<BakedModel> callback) {
//        callback.setReturnValue(this.getItemModel(Items.DIAMOND_BLOCK));
    }
}