package com.xiaohunao.phase_journey.mixin;

import com.xiaohunao.phase_journey.common.phase.enchantment.EnchantmentPhaseManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public class EnchantmentTableMenuMixin {
    @Inject(method = "getEnchantmentList", at = @At("RETURN"), cancellable = true)
    private void phaseJourney$filterRestrictedEnchantments(CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        List<EnchantmentInstance> enchantments = cir.getReturnValue();
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;

        // 过滤掉被限制的附魔
        enchantments.removeIf(enchantmentInstance -> {
            ResourceKey<Enchantment> enchantmentKey = enchantmentInstance.enchantment.getKey();
            return EnchantmentPhaseManager.MANAGER.isRestricted(player.level(), player, enchantmentKey, true);
        });

        cir.setReturnValue(enchantments);
    }
}
