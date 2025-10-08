package org.confluence.phase_journey.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.command.PhaseJourneyCommands;
import org.confluence.phase_journey.common.network.SyncPhasePacketS2C;

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

}
