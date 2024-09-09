package org.confluence.phase_journey.common.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
	@Inject(at = @At("HEAD"), method = "getName", cancellable = true)
	public void getName(ItemStack stack, CallbackInfoReturnable<Component> callback) {
//		callback.setReturnValue(Items.DIAMOND_BLOCK.getDescription());
	}
}