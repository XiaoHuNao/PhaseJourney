package com.xiaohunao.phase_journey.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.xiaohunao.phase_journey.common.phase.item.ItemPhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemTooltipEvent.class)
public class ItemTooltipEventMixin {

    @ModifyVariable(method = "<init>", at = @At(value = "HEAD"), argsOnly = true)
    private static ItemStack init(ItemStack stack, @Local(argsOnly = true) Player player) {
        if (player == null){
            return stack;
        }

        return PhaseUtils.findFirstContextOrReturnDefault(ItemPhaseManager.MANAGER,player.level(),player,null,(ctx, manager) -> {
            if (ctx.getSource().equals(stack.getItem())){
                return ctx.getTarget().getDefaultInstance();
            }
            return stack;
        },stack);
    }
}
