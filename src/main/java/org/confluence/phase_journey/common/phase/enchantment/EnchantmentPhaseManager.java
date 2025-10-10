package org.confluence.phase_journey.common.phase.enchantment;

import java.util.Map;

import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.PhaseType;

import com.mojang.datafixers.util.Pair;

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
        for (Map.Entry<PhaseType, Pair<ResourceLocation, EnchantmentRestrictedContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            EnchantmentRestrictedContext phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = phaseContext.getPhase();

            if (!phaseContext.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            if (!phaseContext.getEnchantment().equals(enchantment)) {
                continue;
            }

            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, null, player);
            if (phaseAttachment == null) {
                return false;
            }

            return phaseAttachment.ifPhaseAbsent(phase, () -> {
                if (isEnchantmentTable) {
                    return !phaseContext.isAllowInEnchantmentTable();
                } else {
                    return !phaseContext.isAllowAnvil();
                }
            });
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
