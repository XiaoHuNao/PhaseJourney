package org.confluence.phase_journey.common.phase.block;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;
import org.confluence.phase_journey.common.phase.item.ItemReplacement;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class BlockPhaseManager {
    public static final BlockPhaseManager INSTANCE = new BlockPhaseManager();

    private final Multimap<ResourceLocation, BlockReplacement> phaseToContexts = ArrayListMultimap.create();
    private final BiMap<BlockState, BlockReplacement> blockStateReplacements = HashBiMap.create();

    public void registerBlockPhase(ResourceLocation phase, BlockReplacement replacement) {
        phaseToContexts.put(phase, replacement);
        blockStateReplacements.put(replacement.getSource(), replacement);
        registerBlockReplacement(phase, replacement);
    }

    public void applyTargetIfPhaseIsNotAchieved(Player player, BlockState source, Consumer<BlockState> targetConsumer) {
        for (Map.Entry<ResourceLocation, Collection<BlockReplacement>> entry : phaseToContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            BlockState target = BlockPhaseManager.INSTANCE.getReplacedBlockState(source);
            if (source != target) {
                targetConsumer.accept(target);
                return;
            }
        }
    }

    public BlockState replaceSourceIfPhaseIsNotAchieved(Player player, BlockState source) {
        for (Map.Entry<ResourceLocation, Collection<BlockReplacement>> entry : phaseToContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            BlockState target = BlockPhaseManager.INSTANCE.getReplacedBlockState(source);
            if (source != target) {
                return target;
            }
        }
        return source;
    }

    public BlockState getReplacedBlockState(BlockState source) {
        BlockReplacement replacement = blockStateReplacements.get(source);
        if (replacement == null) return source;
        return replacement.getTarget();
    }

    public void registerBlockReplacement(ResourceLocation phase, BlockReplacement blockReplacement) {
        Item sourceItem = blockReplacement.getSource().getBlock().asItem();
        Item targetItem = blockReplacement.getTarget().getBlock().asItem();
        ItemReplacement itemReplacement = new ItemReplacement(phase, sourceItem, targetItem);
        ItemPhaseManager.INSTANCE.registerItemReplacement(phase, itemReplacement);
    }
}
