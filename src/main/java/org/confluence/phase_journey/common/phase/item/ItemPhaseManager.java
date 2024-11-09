package org.confluence.phase_journey.common.phase.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemPhaseManager {
    public static final ItemPhaseManager INSTANCE = new ItemPhaseManager();

    private final Multimap<ResourceLocation, ItemPhaseContext> itemPhaseContexts = ArrayListMultimap.create();
    private final BiMap<Item, ItemPhaseContext> sourceItemPhaseContexts = HashBiMap.create();

    public Multimap<ResourceLocation, ItemPhaseContext> getItemPhaseContexts() {
        return itemPhaseContexts;
    }

    public void registerBlockPhase(ResourceLocation phase, ItemPhaseContext itemPhaseContext) {
        itemPhaseContexts.put(phase, itemPhaseContext);
        sourceItemPhaseContexts.put(itemPhaseContext.getSourceItem(), itemPhaseContext);
    }

    public boolean checkReplaceItem(Item item) {
        return sourceItemPhaseContexts.containsKey(item);
    }

    public Component getReplaceItemDescription(Item item) {
        return sourceItemPhaseContexts.get(item).getReplaceItem().getDescription();
    }
}
