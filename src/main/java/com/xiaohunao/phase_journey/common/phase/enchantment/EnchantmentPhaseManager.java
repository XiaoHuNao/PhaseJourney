package com.xiaohunao.phase_journey.common.phase.enchantment;

import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

public class EnchantmentPhaseManager extends PhaseManager<EnchantmentRestrictedContext> {

    public static final EnchantmentPhaseManager MANAGER = new EnchantmentPhaseManager();

    @Override
    public void register(PhaseType type, ResourceLocation phase, EnchantmentRestrictedContext phaseContext) {
        super.register(type, phase, phaseContext);
    }

    public boolean isRestricted(Level level, Player player, ResourceKey<Enchantment> enchantment, boolean isEnchantmentTable) {
        return isRestricted(level, player.getOnPos(),player, ctx -> {
            if (isEnchantmentTable) {
                return !ctx.isAllowInEnchantmentTable();
            } else {
                return !ctx.isAllowAnvil();
            }
        });
    }

    @SubscribeEvent
    public void onAnvilUpdateEvent(AnvilUpdateEvent event) {
        Player player = event.getPlayer();
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        ItemEnchantments leftItemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(left);
        ItemEnchantments rightItemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(right);

        for (Object2IntMap.Entry<Holder<Enchantment>> holderEntry : leftItemEnchantments.entrySet()) {
            ResourceKey<Enchantment> key = holderEntry.getKey().getKey();
            if (isRestricted(player.level(), player, key, false)) {
                event.setCanceled(true);
                return;
            }
        }

        for (Object2IntMap.Entry<Holder<Enchantment>> holderEntry : rightItemEnchantments.entrySet()) {
            ResourceKey<Enchantment> key = holderEntry.getKey().getKey();
            if (isRestricted(player.level(), player, key, false)) {
                event.setCanceled(true);
                return;
            }
        }
    }

}
