package org.confluence.phase_journey.common.event;

import com.xiaohunao.xhn_lib.common.event.FlexibleRegisterEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.LootTableLoadEvent;
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
            PhaseJourney.asResource("advanced_enchantment_lock"),
            PhaseJourney.asResourceKey(Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace("protection")),
            false, false
        ));
    }
}
