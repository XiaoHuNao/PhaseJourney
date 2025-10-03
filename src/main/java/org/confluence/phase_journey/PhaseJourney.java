package org.confluence.phase_journey;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.phase_journey.common.init.PJAttachments;
import org.confluence.phase_journey.common.init.PJPhaseContextTypes;
import org.confluence.phase_journey.common.init.PJRegistries;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.phase.dimension.DimensionPhaseManager;
import org.confluence.phase_journey.common.phase.effect.MobEffectPhaseManager;
import org.confluence.phase_journey.common.phase.interaction.EntityInteractionPhaseManager;
import org.confluence.phase_journey.common.phase.enchantment.EnchantmentPhaseManager;
import org.confluence.phase_journey.common.phase.growth.CropGrowthPhaseManager;
import org.confluence.phase_journey.integration.LoadedCompat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(PhaseJourney.MODID)
public class PhaseJourney {
    public static final String MODID = "phase_journey";
    public static final Logger LOGGER = LoggerFactory.getLogger("Phase Journey");

    public PhaseJourney(IEventBus eventBus, ModContainer container) {
        eventBus.addListener(PJRegistries::registerRegistries);
        LoadedCompat.register(eventBus);

        PJAttachments.TYPES.register(eventBus);
        PJPhaseContextTypes.CONDITION_CODEC.register(eventBus);

        NeoForge.EVENT_BUS.register(BlockPhaseManager.MANAGER);
//        NeoForge.EVENT_BUS.register(ItemPhaseManager.MANAGER);
        NeoForge.EVENT_BUS.register(DimensionPhaseManager.MANAGER);
        NeoForge.EVENT_BUS.register(EnchantmentPhaseManager.MANAGER);
        NeoForge.EVENT_BUS.register(EntityInteractionPhaseManager.MANAGER);
        NeoForge.EVENT_BUS.register(MobEffectPhaseManager.MANAGER);
        NeoForge.EVENT_BUS.register(CropGrowthPhaseManager.MANAGER);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String asDescriptionId(String path) {
        return MODID + "." + path;
    }

    public static <T> ResourceKey<T> asResourceKey(ResourceKey<? extends Registry<T>> registryKey, String path) {
        return ResourceKey.create(registryKey, asResource(path));
    }

    public static <T> ResourceKey<Registry<T>> asResourceKey(String path) {
        return ResourceKey.createRegistryKey(asResource(path));
    }

    public static <T> ResourceKey<T> asResourceKey(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation path) {
        return ResourceKey.create(registryKey, path);
    }
}
