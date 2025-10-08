package org.confluence.phase_journey.common.phase.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.PhaseType;
import org.confluence.phase_journey.common.phase.item.ItemPhaseContext;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class BlockPhaseManager extends PhaseManager<BlockReplacementPhaseContext> {
    public static final BlockPhaseManager MANAGER = new BlockPhaseManager();


    private final BiMap<BlockState, BlockReplacementPhaseContext> blockStateReplacements = HashBiMap.create();

    @Override
    public void register(PhaseType type,ResourceLocation phase, BlockReplacementPhaseContext phaseContext) {
        super.register(type,phase, phaseContext);

        blockStateReplacements.put(phaseContext.getSource(), phaseContext);
        Item sourceItem = phaseContext.getSource().getBlock().asItem();
        Item targetItem;
        if (sourceItem != Items.AIR && (targetItem = phaseContext.getTarget().getBlock().asItem()) != Items.AIR) {
            if (!ItemPhaseManager.MANAGER.hasReplacedItem(sourceItem)) { // 确保物品只注册一次
                ItemPhaseContext itemPhaseContext = new ItemPhaseContext(phase, sourceItem, targetItem);
                ItemPhaseManager.MANAGER.register(phase, itemPhaseContext);
            }
        }
    }

    @Override
    public void init() {
        phaseContexts.forEach((phase, phaseContext) -> {
            replaceBlockProperties(phase);
        });
    }

    @Override
    public void broadcastPhaseChangeToClient(ResourceLocation phase, boolean add) {
        updateBlockProperties(phase,add);
    }

    @Override
    public void achievePlayerPhase(ServerPlayer player, ResourceLocation phase, boolean add) {
        updateBlockProperties(phase,add);
    }

    @Override
    public void achieveLevelPhase(ServerLevel serverLevel, ResourceLocation phase, boolean add) {
        updateBlockProperties(phase,add);
    }

    public void updateBlockProperties(ResourceLocation phase,boolean add) {
        if (add){
            rollbackBlockProperties(phase);
        }else {
            replaceBlockProperties(phase);
        }
    }

    @SubscribeEvent
    public void serverStarted(ServerStartedEvent event) {
        for (ResourceLocation phase : PhaseAttachment.of(event.getServer()).getPhases()) {
             rollbackBlockProperties(phase);
        }
    }

    @SubscribeEvent
    public void blockDrops(BlockDropsEvent event) {
        if (event.getBreaker() instanceof Player player) {
            applyTargetIfPlayerNotReachedPhase(player, event.getState(), target -> {
                Block.dropResources(target, event.getLevel(), event.getPos(), null, player, event.getTool());
                event.setCanceled(true);
            });
        } else {
            applyTargetIfLevelNotFinishedPhase(event.getLevel(), event.getState(), target -> {
                Block.dropResources(target, event.getLevel(), event.getPos(), null);
                event.setCanceled(true);
            });
        }
    }



    public void applyTargetIfNotAchievedPhase(Player player, BlockState source, Consumer<BlockState> targetConsumer) {
        if (source.hasBlockEntity() || source.isAir()) return;
        BlockReplacementPhaseContext replacement = blockStateReplacements.get(source);
        if (replacement == null) return;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacementPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                targetConsumer.accept(replacement.getTarget());
                return;
            }
        }
    }

    public void applyTargetIfPlayerNotReachedPhase(Player player, BlockState source, Consumer<BlockState> targetConsumer) {
        if (source.hasBlockEntity() || source.isAir()) return;
        BlockReplacementPhaseContext replacement = blockStateReplacements.get(source);
        if (replacement == null) return;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacementPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerReachedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                targetConsumer.accept(replacement.getTarget());
                return;
            }
        }
    }

    public void applyTargetIfLevelNotFinishedPhase(Level level, BlockState source, Consumer<BlockState> targetConsumer) {
        if (source.hasBlockEntity() || source.isAir()) return;
        BlockReplacementPhaseContext replacement = blockStateReplacements.get(source);
        if (replacement == null) return;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacementPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadLevelFinishedPhase(entry.getKey(), level)) continue;
            if (entry.getValue().contains(replacement)) {
                targetConsumer.accept(replacement.getTarget());
                return;
            }
        }
    }

    public BlockState replaceSourceIfNotAchievedPhase(Player player, BlockState source) {
        if (source.hasBlockEntity() || source.isAir()) return source;
        BlockReplacementPhaseContext replacement = blockStateReplacements.get(source);
        if (replacement == null) return source;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacementPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                return replacement.getTarget();
            }
        }
        return source;
    }

    public BlockState replaceSourceIfPlayerNotReachedPhase(Player player, BlockState source) {
        if (source.hasBlockEntity() || source.isAir()) return source;
        BlockReplacementPhaseContext replacement = blockStateReplacements.get(source);
        if (replacement == null) return source;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacementPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerReachedPhase(entry.getKey(), player)) continue;
            if (entry.getValue().contains(replacement)) {
                return replacement.getTarget();
            }
        }
        return source;
    }

    public BlockState replaceSourceIfLevelNotFinishedPhase(Level level, BlockState source) {
        if (source.hasBlockEntity() || source.isAir()) return source;
        BlockReplacementPhaseContext replacement = blockStateReplacements.get(source);
        if (replacement == null) return source;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacementPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadLevelFinishedPhase(entry.getKey(), level)) continue;
            if (entry.getValue().contains(replacement)) {
                return replacement.getTarget();
            }
        }
        return source;
    }

    public boolean denyDestroy(Player player, BlockState source) {
        if (source.hasBlockEntity() || source.isAir()) return false;
        for (Map.Entry<ResourceLocation, Collection<BlockReplacementPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerReachedPhase(entry.getKey(), player)) continue;
            BlockReplacementPhaseContext replacement = blockStateReplacements.get(source);
            if (replacement != null) return !replacement.isDestroyAllowed();
        }
        return false;
    }

    public BlockState getReplacedBlockState(BlockState source) {
        if (source.hasBlockEntity() || source.isAir()) return source;
        BlockReplacementPhaseContext replacement = blockStateReplacements.get(source);
        if (replacement == null) return source;
        return replacement.getTarget();
    }

    public boolean hasReplacement(BlockState source) {
        if (source.hasBlockEntity() || source.isAir()) return false;
        return blockStateReplacements.get(source) != null;
    }

    public void replaceBlockProperties(ResourceLocation phase) {
        for (BlockReplacementPhaseContext replacement : phaseContexts.get(phase)) {
            replacement.replaceProperties();
        }
    }

    public void rollbackBlockProperties(ResourceLocation phase) {
        for (BlockReplacementPhaseContext replacement : phaseContexts.get(phase)) {
            replacement.rollbackProperties();
        }
    }
}
