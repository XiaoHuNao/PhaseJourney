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

    @Override
    public void clear() {
        super.clear();
        itemReplacements.clear();
    }

    public ItemReplacementContext getItemReplacementContext(Item item) {
        return itemReplacements.get(item);
    }

    public boolean hasReplacedItem(Item source) {
        return itemReplacements.containsKey(source);
    }

}
