package org.confluence.phase_journey.common.phase.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.confluence.phase_journey.common.phase.PhaseContext;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.PhaseRegisterContext;

import java.util.AbstractMap;
import java.util.Map;

public class BlockPhaseContext extends PhaseContext {
    private BlockState sourceBlock;
    private BlockState replaceBlock;
    private boolean canDestroy = true;

    public BlockPhaseContext(ResourceLocation phase, Block sourceBlock, Block replaceBlock) {
        super(phase);
        this.sourceBlock = sourceBlock.defaultBlockState();
        this.replaceBlock = replaceBlock.defaultBlockState();
    }
    public BlockPhaseContext(ResourceLocation phase, BlockState sourceBlockStack, BlockState replaceBlock) {
        super(phase);
        this.sourceBlock = sourceBlockStack;
        this.replaceBlock = replaceBlock;
    }
    public BlockPhaseContext canDestroy(boolean canDestroy){
        this.canDestroy = canDestroy;
        return this;
    }
    public PhaseRegisterContext register(){
        PhaseManager.registerBlockPhase(phase,this);
        return PhaseRegisterContext.INSTANCE;
    }
    public boolean canDestroy(){
        return canDestroy;
    }
    public @Nullable BlockState getReplaceBlock(){
        return replaceBlock;
    }
    public @Nullable BlockState getSourceBlock(){
        return sourceBlock;
    }
}
