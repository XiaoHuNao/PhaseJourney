package org.confluence.phase_journey.common.phase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockReplacement;
import org.jetbrains.annotations.Nullable;


public class PhaseRegisterContext {
    private final ResourceLocation phase;

    public PhaseRegisterContext(ResourceLocation phase) {
        this.phase = phase;
    }

    public @Nullable BlockReplacement blockReplacement(Block source, Block target) {
        return blockReplacement(source.defaultBlockState(), target.defaultBlockState());
    }

    public @Nullable BlockReplacement blockReplacement(BlockState source, BlockState target) {
        if (source.hasBlockEntity() || target.hasBlockEntity()) return null;
        BlockReplacement replacement = new BlockReplacement(phase, source, target);
        PhaseManager.BLOCK.registerBlockPhase(phase, replacement);
        return replacement;
    }
}
