package org.confluence.phase_journey.common.phase.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.PhaseContext;
import org.confluence.phase_journey.common.phase.PhaseRegisterContext;

public class BlockPhaseContext extends PhaseContext {
    public static final Codec<BlockPhaseContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(BlockPhaseContext::getPhase),
            BlockState.CODEC.fieldOf("sourceBlock").forGetter(BlockPhaseContext::getSourceBlock),
            BlockState.CODEC.fieldOf("replaceBlock").forGetter(BlockPhaseContext::getReplaceBlock),
            Codec.BOOL.fieldOf("canDestroy").forGetter(BlockPhaseContext::canDestroy)
    ).apply(instance,BlockPhaseContext::new));

    private final BlockState sourceBlock;
    private final BlockState replaceBlock;
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

    public BlockPhaseContext(ResourceLocation phase, BlockState sourceBlockStack, BlockState replaceBlock, Boolean canDestroy) {
        super(phase);
        this.sourceBlock = sourceBlockStack;
        this.replaceBlock = replaceBlock;
        this.canDestroy = canDestroy;
    }

    public BlockPhaseContext canDestroy(boolean canDestroy){
        this.canDestroy = canDestroy;
        return this;
    }
    public PhaseRegisterContext register(){
        BlockPhaseManager.INSTANCE.registerBlockPhase(phase,this);
        return PhaseRegisterContext.INSTANCE;
    }
    public boolean canDestroy(){
        return canDestroy;
    }
    public BlockState getReplaceBlock(){
        return replaceBlock;
    }
    public BlockState getSourceBlock(){
        return sourceBlock;
    }
}
