package org.confluence.phase_journey.common.phase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockReplacement;


public class PhaseRegisterContext {
    public static final PhaseRegisterContext INSTANCE = new PhaseRegisterContext();

    public BlockReplacement blockReplacement(ResourceLocation phase, Block source, Block target) {
        return new BlockReplacement(phase, source, target);
    }

    public BlockReplacement blockReplacement(ResourceLocation phase, BlockState source, BlockState target) {
        return new BlockReplacement(phase, source, target);
    }
}
