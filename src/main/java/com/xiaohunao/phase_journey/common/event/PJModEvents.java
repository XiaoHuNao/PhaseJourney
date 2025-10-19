package com.xiaohunao.phase_journey.common.event;

import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.network.SyncPhasePacketS2C;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = PhaseJourney.MODID)
public final class PJModEvents {
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
//        event.register(PhaseType.LEVEL,PJPhaseContextTypes.RECIPE.get(),
//                new RecipeLockContext(
//                    PhaseJourney.asResource("test"),
//                    ResourceLocation.withDefaultNamespace("stone"),
//                    RecipeType.SMELTING
//                )
//        );
//        event.register(PhaseType.LEVEL,PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                    PhaseJourney.asResource("test"),
//                    ResourceLocation.withDefaultNamespace("diamond_from_blasting_diamond_ore"),
//                    RecipeType.BLASTING
//                )
//        );
//        event.register(PhaseType.LEVEL,PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.withDefaultNamespace("quartz_slab_from_stonecutting"),
//                RecipeType.STONECUTTING
//            )
//        );
//        event.register(PhaseType.LEVEL,PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.withDefaultNamespace("netherite_sword_smithing"),
//                RecipeType.SMITHING
//            )
//        );
//
//        event.register(PhaseType.LEVEL,PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.tryParse("create:milling/cobblestone"),
//                AllRecipeTypes.MILLING.getType()
//            )
//        );
//
//        event.register(PhaseType.LEVEL, CreateHelper.INFINITE_FLUID_POOL.get(), new InfiniteFluidPoolContext(PhaseJourney.asResource("tset"), Fluids.LAVA));
//
//        event.register(PhaseType.LEVEL, PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.tryParse("industrialforegoing:dissolution_chamber/pink_slime_ball"),
//                ModuleCore.DISSOLUTION_TYPE.get()
//        ));
//
//        event.register(PhaseType.LEVEL, PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.tryParse("minecraft:/mekanism_generated/charcoal"),
//                ModuleCore.LASER_DRILL_TYPE.get()
//        ));
//
//        event.register(PhaseType.LEVEL, PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.tryParse("minecraft:/mekanism_generated/charcoal"),
//                MekanismRecipeType.SMELTING.getRecipeType()
//        ));
//
//        event.register(PhaseType.LEVEL, PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.tryParse("immersiveengineering:alloysmelter/constantan"),
//                IERecipeTypes.ALLOY.get()
//        ));
//
//        event.register(PhaseType.LEVEL, PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.tryParse("immersiveengineering:arcfurnace/alloy_constantan"),
//                IERecipeTypes.ARC_FURNACE.get()
//        ));
//
//        event.register(PhaseType.LEVEL, PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.tryParse("immersiveengineering:cokeoven/coke_block"),
//                IERecipeTypes.COKE_OVEN.get()
//        ));
//
//        event.register(PhaseType.LEVEL, PJPhaseContextTypes.RECIPE.get(), new RecipeLockContext(
//                PhaseJourney.asResource("test"),
//                ResourceLocation.tryParse("immersiveengineering:crusher/ingot_iron"),
//                IERecipeTypes.CRUSHER.get()
//        ));
//        event.register(PJPhaseContextTypes.DIMENSION.get(), DimensionTravelRetrictedContext.denyLeave(PhaseJourney.asResource("test"), Level.NETHER));
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
//        // HeavenDestinyMoment: disallow moment creation for demonstration phase
//        event.register(HDMHelper.MOMENT_CREATE.get(), new HDMMomentCreatePhaseContext(
//                PhaseJourney.asResource("test"),
//                false,
//                List.of()
//        ));
//        if (org.confluence.phase_journey.integration.LoadedCompat.CURIOS) {
//            event.register(CuriosHelper.EQUIPPING.get(), new CuriosEquipPhaseContext(
//                    PhaseJourney.asResource("test"),
//                    List.of("ring", "belt")
//            ));
//        }

    }
}
