package org.confluence.phase_journey.common.event;


import com.google.common.collect.Sets;
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
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Set;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = PhaseJourney.MODID)
public final class GameEvents {
    @SubscribeEvent
    public static void playerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
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
        Set<ResourceLocation> allPhases = Sets.newHashSet(player.getData(PJAttachments.PHASE).getPhases());
        Set<ResourceLocation> levelPhases = player.level().getData(PJAttachments.PHASE).getPhases();
        allPhases.addAll(levelPhases);
        for (ResourceLocation phase : allPhases) {
            PacketDistributor.sendToPlayer(player, new SyncPhasePacketS2C(phase, true)); // 新来的玩家沿袭已达成的阶段
        }
        if (player.server.getPlayerList().getPlayerCount() == 1) { // 新打开的世界需要初始化
            for (ResourceLocation phase : levelPhases) {
                BlockPhaseManager.INSTANCE.rollbackBlockProperties(phase);
            }
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}