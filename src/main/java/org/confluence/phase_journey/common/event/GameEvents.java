package org.confluence.phase_journey.common.event;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.attachment.PhaseAttachmemnt;
import org.confluence.phase_journey.common.command.PhaseJourneyCommands;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.network.PhaseSyncS2CPack;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = PhaseJourney.MODID)
public final class GameEvents {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide || hand != InteractionHand.MAIN_HAND) return;

        if (player.isShiftKeyDown()) { // todo just for test
            boolean remove = true;
            PhaseAttachmemnt playerPhase = player.getData(PJAttachments.PHASE);
            PhaseAttachmemnt levelPhase = level.getData(PJAttachments.PHASE);
            ResourceLocation phase1 = PhaseJourney.asResource("phase_1");
            if (remove) {
                playerPhase.removePhase(phase1);
                levelPhase.removePhase(phase1);
            } else {
                playerPhase.addPhase(phase1);
                levelPhase.addPhase(phase1);
            }
            PacketDistributor.sendToPlayer((ServerPlayer) player, new PhaseSyncS2CPack(phase1, !remove));
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}