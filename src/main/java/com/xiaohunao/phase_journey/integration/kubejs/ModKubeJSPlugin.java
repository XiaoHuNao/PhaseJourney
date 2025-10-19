package com.xiaohunao.phase_journey.integration.kubejs;

import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.common.attachment.BlockOwnerAttachment;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.init.PJAttachments;
import com.xiaohunao.phase_journey.common.init.PJPhaseContextTypes;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseContext;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import com.xiaohunao.phase_journey.common.phase.block.BlockPhaseManager;
import com.xiaohunao.phase_journey.common.phase.block.BlockReplacementPhaseContext;
import com.xiaohunao.phase_journey.common.phase.dimension.DimensionPhaseManager;
import com.xiaohunao.phase_journey.common.phase.dimension.DimensionTravelRestrictedContext;
import com.xiaohunao.phase_journey.common.phase.effect.MobEffectApplicableContext;
import com.xiaohunao.phase_journey.common.phase.effect.MobEffectPhaseManager;
import com.xiaohunao.phase_journey.common.phase.enchantment.EnchantmentPhaseManager;
import com.xiaohunao.phase_journey.common.phase.enchantment.EnchantmentRestrictedContext;
import com.xiaohunao.phase_journey.common.phase.growth.CropGrowthInhibitionContext;
import com.xiaohunao.phase_journey.common.phase.growth.CropGrowthPhaseManager;
import com.xiaohunao.phase_journey.common.phase.item.ItemPhaseManager;
import com.xiaohunao.phase_journey.common.phase.item.ItemReplacementContext;
import com.xiaohunao.phase_journey.common.phase.player.interact.BlockInteractContext;
import com.xiaohunao.phase_journey.common.phase.player.interact.EntityInteractContext;
import com.xiaohunao.phase_journey.common.phase.player.interact.InteractContext;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipeLockContext;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipeModLockContext;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import com.xiaohunao.phase_journey.integration.LoadedCompat;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import mezz.jei.api.recipe.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public class ModKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(PhaseJourneyEvents.GROUP);
    }
    @Override
    public void initStartup() {
        ModList.get().getModContainerById(PhaseJourney.MODID).ifPresent(container -> {
            IEventBus eventBus = container.getEventBus();
            if (eventBus != null) eventBus.addListener(PhaseJourneyEvent.Register.class, event -> {
                if (PhaseJourneyEvents.REGISTER.hasListeners()) {
                    PhaseJourneyEvents.REGISTER.post(new PhaseJourneyEventJS.RegisterJS(event));
                }
            });
        });
    }


    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("IPhaseContext", IPhaseContext.class);
        bindings.add("PhaseType", PhaseType.class);
        bindings.add("PhaseContext", PhaseContext.class);
        bindings.add("PhaseContextType", PhaseContextType.class);
        bindings.add("PhaseManager", PhaseManager.class);
        bindings.add("PhaseUtils", PhaseUtils.class);
        bindings.add("PhaseJourneyEvent.Register", PhaseJourneyEvent.Register.class);
        bindings.add("BlockOwnerAttachment", BlockOwnerAttachment.class);
        bindings.add("PhaseAttachment", PhaseAttachment.class);
        bindings.add("PJAttachments", PJAttachments.class);
        bindings.add("PJPhaseContextTypes", PJPhaseContextTypes.class);
        bindings.add("PJRegistries", PJRegistries.class);
        bindings.add("RecipeType", RecipeType.class);

        bindings.add("BlockPhaseManager", BlockPhaseManager.MANAGER);
        bindings.add("BlockReplacementPhaseContext", BlockReplacementPhaseContext.class);

        bindings.add("DimensionPhaseManager", DimensionPhaseManager.MANAGER);
        bindings.add("DimensionTravelRestrictedContext", DimensionTravelRestrictedContext.class);

        bindings.add("MobEffectPhaseManager", MobEffectPhaseManager.MANAGER);
        bindings.add("MobEffectApplicableContext", MobEffectApplicableContext.class);

        bindings.add("EnchantmentPhaseManager", EnchantmentPhaseManager.MANAGER);
        bindings.add("EnchantmentRestrictedContext", EnchantmentRestrictedContext.class);

        bindings.add("CropGrowthPhaseManager", CropGrowthPhaseManager.MANAGER);
        bindings.add("CropGrowthInhibitionContext", CropGrowthInhibitionContext.class);

        bindings.add("InteractContextManager", InteractContext.InteractContextManager.MANAGER);
        bindings.add("EntityInteractContext", EntityInteractContext.class);
        bindings.add("BlockInteractContext", BlockInteractContext.class);

        bindings.add("ItemPhaseManager", ItemPhaseManager.MANAGER);
        bindings.add("ItemReplacementContext", ItemReplacementContext.class);

        bindings.add("RecipePhaseManager", RecipePhaseManager.MANAGER);
        bindings.add("RecipeLockContext", RecipeLockContext.class);
        bindings.add("RecipeModLockContext", RecipeModLockContext.class);

        LoadedCompat.registerKubeJSBindings(bindings);

    }
}