package org.confluence.phase_journey.common.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.phase.PhaseManager;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = PhaseJourney.MODID, value = Dist.CLIENT)
public final class PJClientGameEvens {
    @SubscribeEvent
    public static void clientPlayerNetwork$LoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        PhaseManager.BLOCK.replaceAllProperties(); // 全部重置到没解锁前的属性
    }
}
