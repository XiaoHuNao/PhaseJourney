package org.confluence.phase_journey.common.event.subscriber;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.api.IPhaseCapability;
import org.confluence.phase_journey.common.network.NetworkHandler;
import org.confluence.phase_journey.common.network.PhaseSyncS2CPack;


@Mod.EventBusSubscriber
public class PlayerEventSubscriber {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide || hand != InteractionHand.MAIN_HAND) {
            return;
        }

        if (player.isShiftKeyDown()) {
            IPhaseCapability.get(player).ifPresent(phaseCap -> {
                ResourceLocation phase1 = PhaseJourney.asResource("phase_1");
                phaseCap.addPhase(phase1);
                NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                        new PhaseSyncS2CPack(phase1, true)
                );
            });
        }
    }

}