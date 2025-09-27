package org.confluence.phase_journey.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.command.PhaseJourneyCommands;
import org.confluence.phase_journey.common.network.SyncPhasePacketS2C;
import org.confluence.phase_journey.common.phase.PhaseManager;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = PhaseJourney.MODID)
public final class PJGameEvents {
    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        SyncPhasePacketS2C.sync2Player4All((ServerPlayer) event.getEntity(), true);
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        SyncPhasePacketS2C.sync2Player4All((ServerPlayer) event.getEntity(), true);
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        SyncPhasePacketS2C.sync2Player4All((ServerPlayer) event.getEntity(), true);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent
    public static void blockDrops(BlockDropsEvent event) {
        if (event.getBreaker() instanceof Player player) {
            PhaseManager.BLOCK.applyTargetIfPlayerNotReachedPhase(player, event.getState(), target -> {
                Block.dropResources(target, event.getLevel(), event.getPos(), null, player, event.getTool());
                event.setCanceled(true);
            });
        } else {
            PhaseManager.BLOCK.applyTargetIfLevelNotFinishedPhase(event.getLevel(), event.getState(), target -> {
                Block.dropResources(target, event.getLevel(), event.getPos(), null);
                event.setCanceled(true);
            });
        }
    }
}
