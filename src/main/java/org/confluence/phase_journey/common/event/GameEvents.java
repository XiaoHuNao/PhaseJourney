package org.confluence.phase_journey.common.event;


import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.command.PhaseJourneyCommands;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.network.SyncPlayerPhasePacketS2C;
import org.confluence.phase_journey.common.phase.PhaseManager;

import java.util.Set;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = PhaseJourney.MODID)
public final class GameEvents {
    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        Set<ResourceLocation> allPhases = Sets.newHashSet(player.getData(PJAttachments.PHASE).getPhases());
        Set<ResourceLocation> levelPhases = player.level().getData(PJAttachments.PHASE).getPhases();
        allPhases.addAll(levelPhases);
        for (ResourceLocation phase : allPhases) {
            PacketDistributor.sendToPlayer(player, new SyncPlayerPhasePacketS2C(phase, true)); // 新来的玩家沿袭已达成的阶段
        }
        if (player.server.getPlayerList().getPlayerCount() == 1) { // 新打开的世界需要初始化
            for (ResourceLocation phase : levelPhases) {
                PhaseManager.BLOCK.rollbackBlockProperties(phase);
            }
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent
    public static void blockDrops(BlockDropsEvent event) {
        BlockState source;
        if (!PhaseManager.BLOCK.hasReplacement(source = event.getState())) return;
        ServerLevel level = event.getLevel();
        if (event.getBreaker() instanceof Player player) {
            PhaseManager.BLOCK.applyTargetIfPlayerNotReachedPhase(player, source, target -> {
                Block.dropResources(target, level, event.getPos(), null, player, event.getTool());
                event.setCanceled(true);
            });
        } else {
            PhaseManager.BLOCK.applyTargetIfLevelNotFinishedPhase(level, source, target -> {
                Block.dropResources(target, level, event.getPos(), null);
                event.setCanceled(true);
            });
        }
    }
}