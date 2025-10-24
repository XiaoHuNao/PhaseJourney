package com.xiaohunao.phase_journey.common.phase.recipe;

import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RecipePhaseManager extends PhaseManager<IRecipeContext> {

    public static final RecipePhaseManager MANAGER = new RecipePhaseManager();

    public boolean isRestricted(BlockEntity entity, RecipeHolder<?> recipeHolder) {
        return isRestricted(entity.getLevel(), entity.getBlockPos(), null, recipeHolder.value().getType(), recipeHolder.id());
    }

    public boolean isRestricted(BlockEntity entity, RecipeType<?> recipeType, ResourceLocation recipeID) {
        return isRestricted(entity.getLevel(), entity.getBlockPos(), recipeType, recipeID);
    }

    public boolean isRestricted(Level level, Player player, RecipeHolder<?> recipeHolder) {
        return isRestricted(level, null, player, recipeHolder.value().getType(), recipeHolder.id());
    }

    public boolean isRestricted(Level level, BlockPos pos, RecipeHolder<?> recipeHolder) {
        return isRestricted(level, pos, null, recipeHolder.value().getType(), recipeHolder.id());
    }

    public boolean isRestricted(Level level, BlockPos pos, RecipeType<?> recipeType, ResourceLocation recipeID) {
        return isRestricted(level, pos, null, recipeType, recipeID);
    }

    public boolean isRestricted(Level level, BlockPos pos, Player player, RecipeType<?> recipeType, ResourceLocation recipeID) {
        return PhaseUtils.anyContextMatches(this,level, player, pos, (ctx, phaseManager) -> {
            return ctx.isRestricted(recipeType, recipeID);
        });
    }
}
