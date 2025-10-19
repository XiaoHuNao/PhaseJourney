package com.xiaohunao.phase_journey.common.phase.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class ItemPhaseManager extends PhaseManager<ItemReplacementContext> {
    public static final ItemPhaseManager MANAGER = new ItemPhaseManager();
    private final BiMap<Item, ItemReplacementContext> itemReplacements = HashBiMap.create();

    @Override
    public void register(PhaseType type, ResourceLocation phase, ItemReplacementContext phaseContext) {
        super.register(type, phase, phaseContext);
        itemReplacements.put(phaseContext.getSource(), phaseContext);
    }

    public void applyTargetIfPhaseIsNotAchieved(Player player, Item source, Consumer<Item> targetConsumer) {
        ItemReplacementContext replacement = itemReplacements.get(source);
        if (replacement == null) return;

        for (Map.Entry<PhaseType, Collection<Pair<ResourceLocation, ItemReplacementContext>>> entry : phaseContexts.asMap().entrySet()) {
            for (Pair<ResourceLocation, ItemReplacementContext> pair : entry.getValue()) {
                ResourceLocation phase = pair.getFirst();
                ItemReplacementContext ctx = pair.getSecond();
                if (PhaseUtils.hadPlayerOrLevelAchievedPhase(phase, player)) continue;
                if (ctx.equals(replacement)) {
                    targetConsumer.accept(replacement.getTarget());
                    return;
                }
            }
        }
    }

    public Item replaceSourceIfPhaseIsNotAchieved(Player player, Item source) {
        ItemReplacementContext replacement = itemReplacements.get(source);
        if (replacement == null) return source;

        for (Map.Entry<PhaseType, Collection<Pair<ResourceLocation, ItemReplacementContext>>> entry : phaseContexts.asMap().entrySet()) {
            for (Pair<ResourceLocation, ItemReplacementContext> pair : entry.getValue()) {
                ResourceLocation phase = pair.getFirst();
                ItemReplacementContext ctx = pair.getSecond();
                if (PhaseUtils.hadPlayerOrLevelAchievedPhase(phase, player)) continue;
                if (ctx.equals(replacement)) {
                    return replacement.getTarget();
                }
            }
        }
        return source;
    }

    public Item getReplacedItem(Item source) {
        ItemReplacementContext replacement = itemReplacements.get(source);
        if (replacement == null) return source;
        return replacement.getTarget();
    }

    public boolean hasReplacedItem(Item source) {
        return itemReplacements.containsKey(source);
    }


    public boolean isRestricted(Level level, BlockPos pos, ServerPlayer player, Item item) {
        return isRestricted(level, pos, player, ctx -> {
            return item.equals(ctx.getSource());
        });
    }
}
