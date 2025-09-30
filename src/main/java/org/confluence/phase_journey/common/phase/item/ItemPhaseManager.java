package org.confluence.phase_journey.common.phase.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class ItemPhaseManager extends PhaseManager<ItemPhaseContext> {
    public static final ItemPhaseManager MANAGER = new ItemPhaseManager();


    private final BiMap<Item, ItemPhaseContext> itemReplacements = HashBiMap.create();

    @Override
    public void register(ResourceLocation phase, ItemPhaseContext phaseContext) {
        super.register(phase, phaseContext);

        itemReplacements.put(phaseContext.getSource(), phaseContext);
    }

    public void applyTargetIfPhaseIsNotAchieved(Player player, Item source, Consumer<Item> targetConsumer) {
        ItemPhaseContext replacement = itemReplacements.get(source);
        if (replacement == null) return;
        for (Map.Entry<ResourceLocation, Collection<ItemPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                targetConsumer.accept(replacement.getTarget());
                return;
            }
        }
    }

    public Item replaceSourceIfPhaseIsNotAchieved(Player player, Item source) {
        ItemPhaseContext replacement = itemReplacements.get(source);
        if (replacement == null) return source;
        for (Map.Entry<ResourceLocation, Collection<ItemPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                return replacement.getTarget();
            }
        }
        return source;
    }

    public Item getReplacedItem(Item source) {
        ItemPhaseContext replacement = itemReplacements.get(source);
        if (replacement == null) return source;
        return replacement.getTarget();
    }

    public boolean hasReplacedItem(Item source) {
        return itemReplacements.containsKey(source);
    }
}
