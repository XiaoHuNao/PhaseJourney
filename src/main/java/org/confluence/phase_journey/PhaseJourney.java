package org.confluence.phase_journey;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.phase_journey.common.command.PhaseJourneyCommands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(PhaseJourney.MODID)
public class PhaseJourney {
    public static final String MODID = "phase_journey";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PhaseJourney() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    @SubscribeEvent
    public void onCmdRegister(@NotNull RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

}
