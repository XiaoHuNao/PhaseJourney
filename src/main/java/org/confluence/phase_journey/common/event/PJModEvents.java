package org.confluence.phase_journey.common.event;

import net.minecraft.world.item.crafting.RecipeType;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.api.event.PhaseJourneyEvent;
import org.confluence.phase_journey.common.init.PJPhaseContextTypes;
import org.confluence.phase_journey.common.init.PJRegistries;
import org.confluence.phase_journey.common.network.SyncPhasePacketS2C;
import org.confluence.phase_journey.common.phase.PhaseContextType;
import org.confluence.phase_journey.common.phase.PhaseType;
import org.confluence.phase_journey.common.phase.recipe.RecipeLockContext;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = PhaseJourney.MODID)
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
        event.register(PhaseType.LEVEL,PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(PhaseJourney.asResource("tset"),ResourceLocation.withDefaultNamespace("stone"), RecipeType.SMELTING));
//        event.register(PJPhaseContextTypes.DIMENSION.get(), DimensionTravelRestrictedContext.denyLeave(PhaseJourney.asResource("test"), Level.NETHER));
//
//        event.register(PJPhaseContextTypes.ENCHANTMENT.get(), new EnchantmentPhaseContext(
//                PhaseJourney.asResource("test"),
//                PhaseJourney.asResourceKey(Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace("protection")),
//                false, false
//        ));
//
//        event.register(PJPhaseContextTypes.ENTITY_INTERACTION.get(), EntityInteractionPhaseContext.builder(PhaseJourney.asResource("test"), EntityType.HORSE)
//                .allowTame(false)
//                .build()
//        );
//
//        event.register(ProjecteHelper.TRANSMUTATION.get(), new TransmutationPhaseContext(PhaseJourney.asResource("test"), true, List.of(), true));
//        event.register(IEHelper.MULTIBLOCK.get(), new IEMultiblockPhaseContext(PhaseJourney.asResource("test"), List.of(), true));
//
//        event.register(PJPhaseContextTypes.MOB_EFFECT.get(), new MobEffectApplicableContext(
//                PhaseJourney.asResource("test"),
//                List.of(PhaseJourney.asResourceKey(Registries.MOB_EFFECT, ResourceLocation.withDefaultNamespace("regeneration"))),
//                false
//        ));
//
//        event.register(PJPhaseContextTypes.CROP_GROWTH.get(), new CropGrowthPhaseContext(
//                PhaseJourney.asResource("test"),
//                List.of(PhaseJourney.asResourceKey(Registries.BLOCK, ResourceLocation.withDefaultNamespace("wheat"))),
//                false
//        ));
//
////        // HeavenDestinyMoment: disallow moment creation for demonstration phase
////        event.register(HDMHelper.MOMENT_CREATE.get(), new HDMMomentCreatePhaseContext(
////                PhaseJourney.asResource("test"),
////                false,
////                List.of()
////        ));
//        if (org.confluence.phase_journey.integration.LoadedCompat.CURIOS) {
//            event.register(CuriosHelper.EQUIPPING.get(), new CuriosEquipPhaseContext(
//                    PhaseJourney.asResource("test"),
//                    List.of("ring", "belt")
//            ));
//        }

    }
}
