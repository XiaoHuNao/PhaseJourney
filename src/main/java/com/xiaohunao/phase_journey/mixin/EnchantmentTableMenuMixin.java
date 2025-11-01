package com.xiaohunao.phase_journey.mixin;

import com.xiaohunao.phase_journey.common.phase.enchantment.EnchantmentPhaseManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public class EnchantmentTableMenuMixin {
    @Unique
    private Player phaseJourney$player;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void cachePlayer(int containerId, Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.phaseJourney$player = playerInventory.player;
    }

    @Inject(method = "getEnchantmentList", at = @At("RETURN"), cancellable = true)
    private void phaseJourney$filterRestrictedEnchantments(CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        if (phaseJourney$player == null) return;

        List<EnchantmentInstance> enchantments = cir.getReturnValue();
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }

        // 过滤掉被限制的附魔
        enchantments.removeIf(enchantmentInstance -> {
            ResourceKey<Enchantment> enchantmentKey = enchantmentInstance.enchantment.getKey();
            return EnchantmentPhaseManager.MANAGER.isRestricted(phaseJourney$player.level(), phaseJourney$player, enchantmentKey, true);
        });

        cir.setReturnValue(enchantments);
    }
}
