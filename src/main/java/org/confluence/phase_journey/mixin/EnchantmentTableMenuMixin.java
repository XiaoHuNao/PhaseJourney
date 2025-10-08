package org.confluence.phase_journey.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.phase.enchantment.EnchantmentPhaseManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public class EnchantmentTableMenuMixin {

    /**
     * 拦截附魔台获取可用附魔的方法，过滤掉被阶段限制的附魔
     */
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
