package com.xiaohunao.phase_journey.mixin.client;

import com.xiaohunao.phase_journey.common.phase.item.ItemPhaseManager;
import com.xiaohunao.phase_journey.common.phase.item.ItemReplacementContext;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public abstract class ClientItemMixin {
    @Inject(at = @At("HEAD"), method = "getName", cancellable = true)
    public void getName(ItemStack itemStack, CallbackInfoReturnable<Component> callback) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        PhaseUtils.applyActionToMatchingContexts(ItemPhaseManager.MANAGER,player.level(),player,null,(ctx,manager) -> {
            return ctx.getSource().equals(itemStack.getItem());
        },(ctx,manager) -> {
            callback.setReturnValue(ctx.getTarget().getDescription());
        });
    }

    @ModifyVariable(method = "appendHoverText", at = @At("HEAD"), argsOnly = true)
    public ItemStack appendHoverText(ItemStack itemStack) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return itemStack;

        return PhaseUtils.findFirstContextOrReturnDefault(ItemPhaseManager.MANAGER,player.level(),player,null,(ctx,manager) -> {
            if (ctx.getSource().equals(itemStack.getItem())) {
                return ctx.getTarget().getDefaultInstance();
            }
            return itemStack;
        },itemStack);
    }


}