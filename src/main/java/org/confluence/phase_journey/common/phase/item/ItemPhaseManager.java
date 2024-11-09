package org.confluence.phase_journey.common.phase.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class ItemPhaseManager {
    public static final ItemPhaseManager INSTANCE = new ItemPhaseManager();

    private final Multimap<ResourceLocation, ItemReplacement> phaseToReplacements = ArrayListMultimap.create();
    private final BiMap<Item, ItemReplacement> itemReplacements = HashBiMap.create();

    public void registerItemReplacement(ResourceLocation phase, ItemReplacement replacement) {
        phaseToReplacements.put(phase, replacement);
        itemReplacements.put(replacement.getSource(), replacement);
    }

    public void applyTargetIfPhaseIsNotAchieved(Player player, Item source, Consumer<Item> targetConsumer) {
        for (Map.Entry<ResourceLocation, Collection<ItemReplacement>> entry : phaseToReplacements.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            Item target = getReplacedItem(source);
            if (source != target) {
                targetConsumer.accept(target);
                return;
            }
        }
    }

    public Item replaceSourceIfPhaseIsNotAchieved(Player player, Item source) {
        for (Map.Entry<ResourceLocation, Collection<ItemReplacement>> entry : phaseToReplacements.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            Item target = getReplacedItem(source);
            if (source != target) {
                return target;
            }
        }
        return source;
    }

    public Item getReplacedItem(Item source) {
        ItemReplacement replacement = itemReplacements.get(source);
        if (replacement == null) return source;
        return replacement.getTarget();
    }
}
