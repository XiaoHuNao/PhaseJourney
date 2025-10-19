package com.xiaohunao.phase_journey.common.phase.player.interact;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.common.phase.InteractType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlockInteractContext extends InteractContext {
    public static final MapCodec<BlockInteractContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(BlockInteractContext::getPhase),
            InteractType.CODEC.listOf().fieldOf("interact_types").forGetter(BlockInteractContext::getInteractTypes),
            Block.CODEC.fieldOf("block").forGetter(BlockInteractContext::getBlock)
    ).apply(instance, BlockInteractContext::new));

    public final Block block;

    public BlockInteractContext(ResourceLocation phase, List<InteractType> interactTypes, Block block) {
        super(phase, EntityType.PLAYER,interactTypes);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public static Builder builder(ResourceLocation phase, Block block) {
        return new Builder(phase, block);
    }

    @Override
    public MapCodec<? extends IPhaseContext> codec() {
        return CODEC;
    }

    public static final class Builder {
        private final ResourceLocation phase;
        private final Block block;
        private final List<InteractType> interactTypes = Lists.newArrayList();

        public Builder(ResourceLocation phase, Block block) {
            this.phase = phase;
            this.block = block;
        }

        public Builder denyBlockInteract() {
            interactTypes.add(InteractType.BLOCK_INTERACT);
            return this;
        }

        public Builder denyBreed() {
            interactTypes.add(InteractType.BREED);
            return this;
        }

        public Builder denyPlace() {
            interactTypes.add(InteractType.BLOCK_PLACE);
            return this;
        }

        public BlockInteractContext build() {
            return new BlockInteractContext(phase,interactTypes,block);
        }
    }
}
