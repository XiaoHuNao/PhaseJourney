package org.confluence.phase_journey.common.init;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.capability.PhaseCapability;

@Mod.EventBusSubscriber
public class ModCapability {
    public static final Capability<PhaseCapability> PHASE = CapabilityManager.get(new CapabilityToken<>() {});
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PhaseCapability.class);
    }
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player player) {
            event.addCapability(PhaseJourney.asResource("phase_journey"), new PhaseCapability.Provider());
        }
    }
    @SubscribeEvent
    public static void attachLevelCapability(AttachCapabilitiesEvent<Level> event) {
        Level level = event.getObject();
        event.addCapability(PhaseJourney.asResource("phase_journey"), new PhaseCapability.Provider());
    }
}