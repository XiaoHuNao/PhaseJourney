package com.xiaohunao.phase_journey.common.phase.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.phase_journey.common.phase.PhaseContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeModLockContext extends PhaseContext implements IRecipeContext{

    public static final MapCodec<RecipeModLockContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(RecipeModLockContext::getPhase),
            ExtraCodecs.NON_EMPTY_STRING.fieldOf("mod_id").forGetter(ctx -> ctx.modId),
            ResourceLocation.CODEC.fieldOf("recipe_type").forGetter(ctx -> BuiltInRegistries.RECIPE_TYPE.getKey(ctx.recipeType))
    ).apply(instance, (phase, modId, recipeTypeId) -> new RecipeModLockContext(
            phase,
            modId,
            BuiltInRegistries.RECIPE_TYPE.get(recipeTypeId)
    )));

    public final String modId;
    public final RecipeType<?> recipeType;

    public RecipeModLockContext(ResourceLocation phase, String modId, RecipeType<?> recipeType) {
        super(phase);
        this.modId = modId;
        this.recipeType = recipeType;
    }

    @Override
    public MapCodec<RecipeModLockContext> codec() {
        return CODEC;
    }

    @Override
    public boolean isRestricted(RecipeType<?> recipeType, ResourceLocation recipeID) {
        return this.recipeType == recipeType && this.modId.equals(recipeID.getNamespace());
    }
}
