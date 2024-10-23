package org.confluence.phase_journey;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.phase_journey.api.PhaseJourneyEvent;
import org.confluence.phase_journey.common.command.PhaseJourneyCommands;
import org.confluence.phase_journey.common.init.ModAttachments;
import org.confluence.phase_journey.common.network.PhaseSyncS2CPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(PhaseJourney.MODID)
public class PhaseJourney {
    public static final String MODID = "phase_journey";
    private static final Logger LOGGER = LoggerFactory.getLogger("Phase Journey");

    public PhaseJourney(IEventBus eventBus, ModContainer container) {
        ModAttachments.TYPES.register(eventBus);
        eventBus.addListener(PhaseJourney::commonSetup);
        eventBus.addListener(PhaseJourney::registerPayloadHandlers);
        NeoForge.EVENT_BUS.addListener(PhaseJourney::registerCommands);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NeoForge.EVENT_BUS.start();
            NeoForge.EVENT_BUS.post(new PhaseJourneyEvent.Register());
        });
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    private static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                PhaseSyncS2CPack.TYPE,
                PhaseSyncS2CPack.STREAM_CODEC,
                PhaseSyncS2CPack::handle
        );
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
