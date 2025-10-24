package com.xiaohunao.phase_journey.common.phase.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.network.RebuildChunksS2C;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import com.xiaohunao.phase_journey.common.phase.item.ItemPhaseManager;
import com.xiaohunao.phase_journey.common.phase.item.ItemReplacementContext;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

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
                ItemReplacementContext itemReplacementContext = new ItemReplacementContext(phase, sourceItem, targetItem);
                ItemPhaseManager.MANAGER.register(type,phase, itemReplacementContext);
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        blockStateReplacements.clear();
    }

    @Override
    public void init() {
        forEach((phase, ctx) -> {
            replaceBlockProperties(phase);
        });
    }

    @Override
    public void applyOrRevokePhase(Level level,ResourceLocation phase, boolean add) {
        updateBlockProperties(phase,add);
        if (!level.isClientSide){
            PacketDistributor.sendToAllPlayers(new RebuildChunksS2C());
        }
    }

    public BlockReplacementPhaseContext getBlockReplacementPhaseContext(BlockState blockState) {
        return blockStateReplacements.get(blockState);
    }

    public void updateBlockProperties(ResourceLocation phase, boolean add) {
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
        Player player = event.getBreaker() instanceof Player ? (Player) event.getBreaker() : null;
        ServerLevel level = event.getLevel();
        PhaseUtils.applyActionToMatchingContexts(this,level,player,null,(ctx,phaseManager) -> {
            return ctx.getSource().equals(event.getState());
        },(target,phaseManager) -> {
            Block.dropResources(target.getTarget(), event.getLevel(), event.getPos(), null, player, event.getTool());
            event.setCanceled(true);
        });
    }

    @SubscribeEvent
    public void onBlockEntityPlace(BlockEvent.EntityPlaceEvent event) {
		BlockState placed = event.getPlacedBlock();
        Level level = (Level)event.getLevel();
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;

        //测试
//        PhaseUtils.applyActionToMatchingContexts(this,level,player,null,(ctx,phaseManager) -> {
//            return ctx.getSource().equals(placed);
//        },(target,phaseManager) -> {
//            BlockPos pos = event.getPos();
//            level.setBlock(pos, target.getTarget(), 3);
//        });
    }

    public boolean denyDestroy(Player player, BlockState source) {
        if (source.hasBlockEntity() || source.isAir()) return false;

        return PhaseUtils.anyContextMatches(this,player.level(),player,null,(ctx,phaseManager) ->{
            return ctx.getSource().equals(source) && !ctx.isDestroyAllowed();
        });
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
        forEach((phaseLocation, ctx) -> {
            if (phaseLocation.equals(phase)) {
                ctx.replaceProperties();
            }
        });
    }

    public void rollbackBlockProperties(ResourceLocation phase) {
        forEach((phaseLocation, ctx) -> {
            if (phaseLocation.equals(phase)) {
                ctx.rollbackProperties();
            }
        });
    }


}
