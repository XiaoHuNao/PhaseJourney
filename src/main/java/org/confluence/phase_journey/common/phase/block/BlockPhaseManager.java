package org.confluence.phase_journey.common.phase.block;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;
import org.confluence.phase_journey.common.phase.item.ItemReplacement;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class BlockPhaseManager {
    public static final BlockPhaseManager INSTANCE = new BlockPhaseManager();

    private final Multimap<ResourceLocation, BlockReplacement> phaseToReplacements = ArrayListMultimap.create();
    private final BiMap<BlockState, BlockReplacement> blockStateReplacements = HashBiMap.create();

    public void registerBlockPhase(ResourceLocation phase, BlockReplacement replacement) {
        phaseToReplacements.put(phase, replacement);
        blockStateReplacements.put(replacement.getSource(), replacement);
        Item sourceItem = replacement.getSource().getBlock().asItem();
        Item targetItem;
        if (sourceItem != Items.AIR && (targetItem = replacement.getTarget().getBlock().asItem()) != Items.AIR) {
            if (!ItemPhaseManager.INSTANCE.hasReplacedItem(sourceItem)) { // 确保物品只注册一次
                ItemReplacement itemReplacement = new ItemReplacement(phase, sourceItem, targetItem);
                ItemPhaseManager.INSTANCE.registerItemReplacement(phase, itemReplacement);
            }
        }
    }

    public void applyTargetIfNotAchievedPhase(Player player, BlockState source, Consumer<BlockState> targetConsumer) {
        BlockReplacement replacement = blockStateReplacements.get(source);
        if (replacement == null) return;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacement>> entry : phaseToReplacements.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                targetConsumer.accept(replacement.getTarget());
                return;
            }
        }
    }

    public void applyTargetIfPlayerNotReachedPhase(Player player, BlockState source, Consumer<BlockState> targetConsumer) {
        BlockReplacement replacement = blockStateReplacements.get(source);
        if (replacement == null) return;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacement>> entry : phaseToReplacements.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerReachedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                targetConsumer.accept(replacement.getTarget());
                return;
            }
        }
    }

    public void applyTargetIfLevelNotFinishedPhase(Level level, BlockState source, Consumer<BlockState> targetConsumer) {
        BlockReplacement replacement = blockStateReplacements.get(source);
        if (replacement == null) return;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacement>> entry : phaseToReplacements.asMap().entrySet()) {
            if (PhaseUtils.hadLevelFinishedPhase(entry.getKey(), level)) continue;
            if (entry.getValue().contains(replacement)) {
                targetConsumer.accept(replacement.getTarget());
                return;
            }
        }
    }

    public BlockState replaceSourceIfNotAchievedPhase(Player player, BlockState source) {
        BlockReplacement replacement = blockStateReplacements.get(source);
        if (replacement == null) return source;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacement>> entry : phaseToReplacements.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                return replacement.getTarget();
            }
        }
        return source;
    }

    public BlockState replaceSourceIfPlayerNotReachedPhase(Player player, BlockState source) {
        BlockReplacement replacement = blockStateReplacements.get(source);
        if (replacement == null) return source;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacement>> entry : phaseToReplacements.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerReachedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                return replacement.getTarget();
            }
        }
        return source;
    }

    public BlockState replaceSourceIfLevelNotFinishedPhase(Level level, BlockState source) {
        BlockReplacement replacement = blockStateReplacements.get(source);
        if (replacement == null) return source;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacement>> entry : phaseToReplacements.asMap().entrySet()) {
            if (PhaseUtils.hadLevelFinishedPhase(entry.getKey(), level)) continue;
            if (entry.getValue().contains(replacement)) {
                return replacement.getTarget();
            }
        }
        return source;
    }

    public boolean denyDestroy(Player player, BlockState source) {
        for (Map.Entry<ResourceLocation, Collection<BlockReplacement>> entry : phaseToReplacements.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerReachedPhase(entry.getKey(), player)) continue;
            BlockReplacement replacement = blockStateReplacements.get(source);
            if (replacement != null) return !replacement.isDestroyAllowed();
        }
        return false;
    }

    public BlockState getReplacedBlockState(BlockState source) {
        BlockReplacement replacement = blockStateReplacements.get(source);
        if (replacement == null) return source;
        return replacement.getTarget();
    }

    public void replaceBlockProperties(ResourceLocation phase) {
        for (BlockReplacement replacement : phaseToReplacements.get(phase)) {
            replacement.replaceProperties();
        }
    }

    public void rollbackBlockProperties(ResourceLocation phase) {
        for (BlockReplacement replacement : phaseToReplacements.get(phase)) {
            replacement.rollbackProperties();
        }
    }
}
