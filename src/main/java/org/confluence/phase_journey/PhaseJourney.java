package org.confluence.phase_journey;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.confluence.phase_journey.common.command.PhaseJourneyCommands;
import org.confluence.phase_journey.common.event.PhaseJourneyRegisterEvent;
import org.confluence.phase_journey.common.network.NetworkHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(PhaseJourney.MODID)
public class PhaseJourney {
    public static final String MODID = "phase_journey";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PhaseJourney() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        eventBus.addListener(this::onFMLCommonSetup);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    @SubscribeEvent
    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.register();

        MinecraftForge.EVENT_BUS.start();
        MinecraftForge.EVENT_BUS.post(new PhaseJourneyRegisterEvent());
    }


    @SubscribeEvent
    public void onCmdRegister(@NotNull RegisterCommandsEvent event) {
        PhaseJourneyCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

}
