package org.confluence.phase_journey.common.phase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.phase.block.BlockReplacement;


public class PhaseRegisterContext {
    private final ResourceLocation phase;

    public PhaseRegisterContext(ResourceLocation phase) {
        this.phase = phase;
    }

    public BlockReplacement blockReplacement(Block source, Block target) {
        BlockReplacement replacement = new BlockReplacement(phase, source, target);
        BlockPhaseManager.INSTANCE.registerBlockPhase(phase, replacement);
        return replacement;
    }

    public BlockReplacement blockReplacement(BlockState source, BlockState target) {
        BlockReplacement replacement = new BlockReplacement(phase, source, target);
        BlockPhaseManager.INSTANCE.registerBlockPhase(phase, replacement);
        return replacement;
    }
}
