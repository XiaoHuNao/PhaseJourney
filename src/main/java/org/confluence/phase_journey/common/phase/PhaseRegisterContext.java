package org.confluence.phase_journey.common.phase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseContext;


public class PhaseRegisterContext {
    public static final PhaseRegisterContext INSTANCE = new PhaseRegisterContext();

    public BlockPhaseContext blockPhase(ResourceLocation phase, Block sourceBlock, Block replaceBlock){
        return new BlockPhaseContext(phase,sourceBlock.defaultBlockState(),replaceBlock.defaultBlockState());
    }
    public BlockPhaseContext blockPhase(ResourceLocation phase, BlockState sourceBlockStack, BlockState replaceBlock){
        return new BlockPhaseContext(phase,sourceBlockStack,replaceBlock);
    }
}
