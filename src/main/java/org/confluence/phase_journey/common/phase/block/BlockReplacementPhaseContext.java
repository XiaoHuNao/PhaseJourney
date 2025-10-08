package org.confluence.phase_journey.common.phase.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.PhaseContext;

public class BlockReplacementPhaseContext extends PhaseContext {
    public static final MapCodec<BlockReplacementPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(BlockReplacementPhaseContext::getPhase),
            BlockState.CODEC.fieldOf("source").forGetter(BlockReplacementPhaseContext::getSource),
            BlockState.CODEC.fieldOf("target").forGetter(BlockReplacementPhaseContext::getTarget),
            Codec.BOOL.fieldOf("allow_destroy").orElse(true).forGetter(BlockReplacementPhaseContext::isDestroyAllowed)
    ).apply(instance, BlockReplacementPhaseContext::new));

    private final BlockState source;
    private final BlockState target;
    private boolean allowDestroy = true;
    private final transient BlockBehaviour.Properties properties;

    public BlockReplacementPhaseContext(ResourceLocation phase, BlockState source, BlockState target) {
        super(phase);
        this.source = source;
        this.target = target;
        this.properties = BlockBehaviour.Properties.ofFullCopy(source.getBlock());
    }

    public BlockReplacementPhaseContext(ResourceLocation phase, BlockState source, BlockState target, boolean allowDestroy) {
        super(phase);
        this.source = source;
        this.target = target;
        this.allowDestroy = allowDestroy;
        this.properties = BlockBehaviour.Properties.ofFullCopy(source.getBlock());
    }

    public BlockReplacementPhaseContext denyDestroy() {
        this.allowDestroy = false;
        return this;
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

    public void replaceProperties() {
        Block sourceBlock = source.getBlock();
        Block targetBlock = target.getBlock();
        sourceBlock.hasCollision = targetBlock.hasCollision;
        sourceBlock.soundType = targetBlock.soundType;
        sourceBlock.friction = targetBlock.friction;
        sourceBlock.speedFactor = targetBlock.speedFactor;
        sourceBlock.jumpFactor = targetBlock.jumpFactor;
        sourceBlock.dynamicShape = targetBlock.dynamicShape;
        sourceBlock.requiredFeatures = targetBlock.requiredFeatures;

        sourceBlock.properties.mapColor = targetBlock.properties.mapColor;
    }

    public void rollbackProperties() {
        Block sourceBlock = source.getBlock();
        sourceBlock.hasCollision = properties.hasCollision;
        sourceBlock.soundType = properties.soundType;
        sourceBlock.friction = properties.friction;
        sourceBlock.speedFactor = properties.speedFactor;
        sourceBlock.jumpFactor = properties.jumpFactor;
        sourceBlock.dynamicShape = properties.dynamicShape;
        sourceBlock.requiredFeatures = properties.requiredFeatures;

        sourceBlock.properties.mapColor = properties.mapColor;
    }

    @Override
    public MapCodec<BlockReplacementPhaseContext> codec() {
        return CODEC;
    }
}
