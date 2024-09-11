package org.confluence.phase_journey.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
	@Inject(at = @At("HEAD"), method = "getName", cancellable = true)
	public void getName(ItemStack itemStack, CallbackInfoReturnable<Component> callback) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null) {
			ItemPhaseManager.INSTANCE.getItemPhaseContexts().forEach((phase, phaseContext) -> {
				if (PhaseUtils.ContainsPhase(phase,player) || PhaseUtils.ContainsPhase(phase,player.clientLevel)){
					return;
				}
				if (ItemPhaseManager.INSTANCE.checkReplaceItem(itemStack.getItem())) {
					callback.setReturnValue(ItemPhaseManager.INSTANCE.getReplaceItemDescription(itemStack.getItem()));
				}
			});
		}
	}
}