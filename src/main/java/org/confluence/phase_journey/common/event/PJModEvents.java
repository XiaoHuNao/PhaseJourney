package org.confluence.phase_journey.common.event;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.api.PhaseJourneyEvent;
import org.confluence.phase_journey.common.init.PJPhaseContextTypes;
import org.confluence.phase_journey.common.init.PJRegistries;
import org.confluence.phase_journey.common.network.SyncPhasePacketS2C;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.phase_journey.common.phase.PhaseContextType;
import org.confluence.phase_journey.common.phase.dimension.DimensionPhaseContext;
import org.confluence.phase_journey.common.phase.enchantment.EnchantmentPhaseContext;
import org.confluence.phase_journey.common.phase.interaction.EntityInteractionPhaseContext;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = PhaseJourney.MODID)
public final class PJModEvents {

    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            PhaseJourneyEvent.Register register = new PhaseJourneyEvent.Register();
            ModLoader.postEvent(register);

            for (PhaseContextType<?> type : PJRegistries.PHASE_CONTEXT_TYPE) {
                type.manager().init();
            }
        });
    }

    @SubscribeEvent
    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                SyncPhasePacketS2C.TYPE,
                SyncPhasePacketS2C.STREAM_CODEC,
                SyncPhasePacketS2C::handle
        );
    }

    @SubscribeEvent
    public static void onPhaseJourney(PhaseJourneyEvent.Register event) {
        event.register(PJPhaseContextTypes.DIMENSION.get(), DimensionPhaseContext.denyLeave(PhaseJourney.asResource("test"), Level.NETHER));

        event.register(PJPhaseContextTypes.ENCHANTMENT.get(), new EnchantmentPhaseContext(
                PhaseJourney.asResource("test"),
                PhaseJourney.asResourceKey(Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace("protection")),
                false, false
        ));

        event.register(PJPhaseContextTypes.ENTITY_INTERACTION.get(), EntityInteractionPhaseContext.builder(PhaseJourney.asResource("test"), EntityType.HORSE)
                .allowTame(false)
                .build()
        );

        event.register(ProjecteHelper.TRANSMUTATION.get(), new TransmutationPhaseContext(PhaseJourney.asResource("test"), true, List.of(), true));
        event.register(IEHelper.MULTIBLOCK.get(), new IEMultiblockPhaseContext(PhaseJourney.asResource("test"), List.of(), true));
    }
}
