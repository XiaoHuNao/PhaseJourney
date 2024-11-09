package org.confluence.phase_journey.common.event;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.command.PhaseJourneyCommands;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.network.SyncPhasePacketS2C;
import org.confluence.phase_journey.common.util.PhaseUtils;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = PhaseJourney.MODID)
public final class GameEvents {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide || hand != InteractionHand.MAIN_HAND) return;

        if (player.isShiftKeyDown()) { // todo just for test
            boolean add = true;
            ResourceLocation phase = PhaseJourney.asResource("phase_1");
            PhaseUtils.achievePhase((ServerPlayer) player, phase, add);
        }
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        for (ResourceLocation phase : player.getData(PJAttachments.PHASE).getPhases()) {
            PacketDistributor.sendToPlayer(player, new SyncPhasePacketS2C(phase, true));
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}