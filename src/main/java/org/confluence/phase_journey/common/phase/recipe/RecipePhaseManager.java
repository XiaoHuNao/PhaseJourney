package org.confluence.phase_journey.common.phase.recipe;

import java.util.Map;

import net.minecraft.world.entity.player.Player;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.PhaseType;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

public class RecipePhaseManager extends PhaseManager<RecipeLockContext> {

    public static final RecipePhaseManager MANAGER = new RecipePhaseManager();

    public boolean isRestricted(Level level, Player player, RecipeHolder<?> recipeHolder) {
        return isRestricted(level, null, player, recipeHolder);
    }

    public boolean isRestricted(Level level, BlockPos pos, Player player, RecipeHolder<?> recipeHolder) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, RecipeLockContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            ResourceLocation phase = entry.getValue().getFirst();
            RecipeLockContext ctx = entry.getValue().getSecond();

            if (!ctx.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            if (recipeHolder.value().getType() != ctx.recipeType) {
                continue;
            }

            if (!recipeHolder.id().equals(ctx.recipeID)) {
                continue;
            }

            PhaseAttachment attachment = phaseType.getPhaseAttachment(level, pos, player);
            if (attachment == null) {
                return false;
            }

            return attachment.ifPhaseAbsent(phase, () -> true);
        }
        return false;
    }
}
