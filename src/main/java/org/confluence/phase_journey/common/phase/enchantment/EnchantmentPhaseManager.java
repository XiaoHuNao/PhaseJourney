package org.confluence.phase_journey.common.phase.enchantment;

import java.util.Collection;
import java.util.Map;

import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

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

public class EnchantmentPhaseManager extends PhaseManager<EnchantmentPhaseContext> {

    public static final EnchantmentPhaseManager MANAGER = new EnchantmentPhaseManager();

    private final BiMap<ResourceKey<Enchantment>, EnchantmentPhaseContext> enchantmentRules = HashBiMap.create();

    @Override
    public void register(ResourceLocation phase, EnchantmentPhaseContext phaseContext) {
        super.register(phase, phaseContext);
        enchantmentRules.put(phaseContext.getEnchantment(), phaseContext);
    }

    public boolean denyInTable(Player player, ResourceKey<Enchantment> enchantment) {
        EnchantmentPhaseContext rule = enchantmentRules.get(enchantment);
        if (rule == null) {
            return false;
        }
        for (Map.Entry<ResourceLocation, Collection<EnchantmentPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) {
                continue;
            }
            if (entry.getValue().contains(rule)) {
                return !rule.isAllowInEnchantmentTable();
            }
        }
        return false;
    }

    public boolean denyAnvil(Player player, ResourceKey<Enchantment> enchantment) {
        EnchantmentPhaseContext rule = enchantmentRules.get(enchantment);
        if (rule == null) {
            return false;
        }
        for (Map.Entry<ResourceLocation, Collection<EnchantmentPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) {
                continue;
            }
            if (entry.getValue().contains(rule)) {
                return !rule.isAllowAnvil();
            }
        }
        return false;
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
            if (denyAnvil(player, key)) {
                event.setCanceled(true);
                return;
            }
        }

        for (Object2IntMap.Entry<Holder<Enchantment>> holderEntry : rightItemEnchantments.entrySet()) {
            ResourceKey<Enchantment> key = holderEntry.getKey().getKey();
            if (denyAnvil(player, key)) {
                event.setCanceled(true);
                return;
            }
        }
    }

}
