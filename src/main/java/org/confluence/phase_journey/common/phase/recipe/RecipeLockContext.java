package org.confluence.phase_journey.common.phase.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.confluence.phase_journey.common.phase.PhaseContext;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeLockContext extends PhaseContext {

    public static final MapCodec<RecipeLockContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(RecipeLockContext::getPhase),
            ResourceLocation.CODEC.fieldOf("recipe_id").forGetter(ctx -> ctx.recipeID),
            ResourceLocation.CODEC.fieldOf("recipe_type").forGetter(ctx -> BuiltInRegistries.RECIPE_TYPE.getKey(ctx.recipeType))
    ).apply(instance, (phase, recipeId, recipeTypeId) -> new RecipeLockContext(
            phase,
            recipeId,
            BuiltInRegistries.RECIPE_TYPE.get(recipeTypeId)
    )));

    public RecipeType<?> recipeType;
    public ResourceLocation recipeID;

    public RecipeLockContext(ResourceLocation phase, ResourceLocation recipeID, RecipeType<?> recipeType) {
        super(phase);
        this.recipeID = recipeID;
        this.recipeType = recipeType;
    }

    @Override
    public MapCodec<RecipeLockContext> codec() {
        return CODEC;
    }
}
