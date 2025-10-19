package com.xiaohunao.phase_journey.common.init;

import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
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
import com.xiaohunao.phase_journey.common.phase.player.interact.InteractContext;
import com.xiaohunao.phase_journey.common.phase.player.interact.InteractContextManager;
import com.xiaohunao.phase_journey.common.phase.recipe.IRecipeContext;
import com.xiaohunao.phase_journey.common.phase.recipe.RecipePhaseManager;
import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;

public class PJPhaseContextTypes {

    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, PhaseJourney.MODID);

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<BlockReplacementPhaseContext>> BLOCK = CONDITION_CODEC.registerStatic("block", () -> new PhaseContextType<>(BlockReplacementPhaseContext.class, BlockPhaseManager.MANAGER));
    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<ItemReplacementContext>> ITEM = CONDITION_CODEC.registerStatic("item", () -> new PhaseContextType<>(ItemReplacementContext.class, ItemPhaseManager.MANAGER));
    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<DimensionTravelRestrictedContext>> DIMENSION = CONDITION_CODEC.registerStatic("dimension", () -> new PhaseContextType<>(DimensionTravelRestrictedContext.class, DimensionPhaseManager.MANAGER));
    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<EnchantmentRestrictedContext>> ENCHANTMENT = CONDITION_CODEC.registerStatic("enchantment", () -> new PhaseContextType<>(EnchantmentRestrictedContext.class, EnchantmentPhaseManager.MANAGER));
    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<MobEffectApplicableContext>> MOB_EFFECT = CONDITION_CODEC.registerStatic("mob_effect", () -> new PhaseContextType<>(MobEffectApplicableContext.class, MobEffectPhaseManager.MANAGER));
    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<CropGrowthInhibitionContext>> CROP_GROWTH = CONDITION_CODEC.registerStatic("crop_growth", () -> new PhaseContextType<>(CropGrowthInhibitionContext.class, CropGrowthPhaseManager.MANAGER));
    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<IRecipeContext>> RECIPE = CONDITION_CODEC.registerStatic("recipe", () -> new PhaseContextType<>(IRecipeContext.class, RecipePhaseManager.MANAGER));
    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<InteractContext>> INTERACT = CONDITION_CODEC.registerStatic("interact", () -> new PhaseContextType<>(InteractContext.class, InteractContextManager.MANAGER));

}
