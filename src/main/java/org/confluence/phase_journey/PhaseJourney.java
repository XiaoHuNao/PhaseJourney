package org.confluence.phase_journey;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(PhaseJourney.MODID)
public class PhaseJourney {
    public static final String MODID = "phase_journey";
    private static final Logger LOGGER = LoggerFactory.getLogger("Phase Journey");

    public PhaseJourney(IEventBus eventBus, ModContainer container) {
        PJAttachments.TYPES.register(eventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
