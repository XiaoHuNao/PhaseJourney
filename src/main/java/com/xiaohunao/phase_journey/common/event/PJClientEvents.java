package com.xiaohunao.phase_journey.common.event;

import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.api.event.ResourceManagerReloadEvent;
import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = PhaseJourney.MODID, value = Dist.CLIENT)
public class PJClientEvents {
    @SubscribeEvent
    public static void clientResource(RegisterClientReloadListenersEvent e) {
        e.registerReloadListener((ResourceManagerReloadListener) res -> NeoForge.EVENT_BUS.post(new ResourceManagerReloadEvent(res, LogicalSide.CLIENT)));
    }
}
