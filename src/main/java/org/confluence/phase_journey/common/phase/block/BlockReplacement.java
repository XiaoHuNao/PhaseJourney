package org.confluence.phase_journey.common.phase.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.PhaseContext;
import org.confluence.phase_journey.common.phase.PhaseRegisterContext;

public class BlockReplacement extends PhaseContext {
    public static final Codec<BlockReplacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(BlockReplacement::getPhase),
            BlockState.CODEC.fieldOf("source").forGetter(BlockReplacement::getSource),
            BlockState.CODEC.fieldOf("target").forGetter(BlockReplacement::getTarget),
            Codec.BOOL.fieldOf("allow_destroy").forGetter(BlockReplacement::isDestroyAllowed)
    ).apply(instance, BlockReplacement::new));

    private final BlockState source;
    private final BlockState target;
    private boolean allowDestroy = true;

    public BlockReplacement(ResourceLocation phase, Block source, Block target) {
        super(phase);
        this.source = source.defaultBlockState();
        this.target = target.defaultBlockState();
    }

    public BlockReplacement(ResourceLocation phase, BlockState sourceBlockStack, BlockState target) {
        super(phase);
        this.source = sourceBlockStack;
        this.target = target;
    }

    public BlockReplacement(ResourceLocation phase, BlockState sourceBlockStack, BlockState target, Boolean allowDestroy) {
        super(phase);
        this.source = sourceBlockStack;
        this.target = target;
        this.allowDestroy = allowDestroy;
    }

    public BlockReplacement isDestroyAllowed(boolean canDestroy) {
        this.allowDestroy = canDestroy;
        return this;
    }

    public PhaseRegisterContext register() {
        BlockPhaseManager.INSTANCE.registerBlockPhase(phase, this);
        return PhaseRegisterContext.INSTANCE;
    }

    public boolean isDestroyAllowed() {
        return allowDestroy;
    }

    public BlockState getTarget() {
        return target;
    }

    public BlockState getSource() {
        return source;
    }
}
