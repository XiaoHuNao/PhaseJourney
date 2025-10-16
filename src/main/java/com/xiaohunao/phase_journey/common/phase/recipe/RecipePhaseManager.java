package com.xiaohunao.phase_journey.common.phase.recipe;

import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
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
        for (PhaseType phaseType : PhaseType.values()) {
            for (Pair<ResourceLocation, IRecipeContext> pair : phaseContexts.get(phaseType)) {
                ResourceLocation phase = pair.getFirst();
                IRecipeContext ctx = pair.getSecond();

                if (!ctx.getSupportedPhaseTypes().contains(phaseType)) {
                    continue;
                }

                if (!ctx.isRestricted(recipeType, recipeID)) {
                    continue;
                }

                PhaseAttachment attachment = phaseType.getPhaseAttachment(level, pos, player);
                if (attachment == null) {
                    return false;
                }

                return attachment.ifPhaseAbsent(phase, () -> true, false);
            }
        }
        return false;
    }
}
