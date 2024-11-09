package org.confluence.phase_journey.common.event;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.api.PhaseJourneyEvent;
import org.confluence.phase_journey.common.attachment.PhaseCapability;
import org.confluence.phase_journey.common.command.PhaseJourneyCommands;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.network.PhaseSyncS2CPack;
import org.confluence.phase_journey.common.phase.block.BlockPhaseContext;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Map;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = PhaseJourney.MODID)
public final class GameEvents {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide || hand != InteractionHand.MAIN_HAND) {
            return;
        }

        if (player.isShiftKeyDown()) {
            PhaseCapability phaseCap = player.getData(PJAttachments.PHASE_CAPABILITY.get());
            ResourceLocation phase1 = PhaseJourney.asResource("phase_1");
            phaseCap.addPhase(phase1);
            PacketDistributor.sendToPlayer((ServerPlayer) player, new PhaseSyncS2CPack(phase1, true));
        }
    }

    @SubscribeEvent
    public static void onPhaseJourneyAdd(PhaseJourneyEvent.Add event) {
        BlockPhaseManager.INSTANCE.ReplaceBlockBlockBehaviour(event.phase, false);
    }

    @SubscribeEvent
    public static void playerHarvestCheck(PlayerEvent.HarvestCheck event) {
        Player player = event.getEntity();
        BlockState replaced = null;
        for (Map.Entry<ResourceLocation, BlockPhaseContext> entry : BlockPhaseManager.INSTANCE.getBlockPhaseContexts().entries()) {
            ResourceLocation phase = entry.getKey();
            BlockPhaseContext blockPhaseContext = entry.getValue();
            if (PhaseUtils.ContainsPhase(phase, player) || PhaseUtils.ContainsPhase(phase, player.level())) {
                return;
            }
            if (BlockPhaseManager.INSTANCE.checkReplaceBlock(event.getTargetBlock())) {
                replaced = blockPhaseContext.getReplaceBlock();
            }
        }
        if (replaced != null) {
            event.setCanHarvest(!replaced.requiresCorrectToolForDrops() || player.getInventory().getSelected().isCorrectToolForDrops(replaced));
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}