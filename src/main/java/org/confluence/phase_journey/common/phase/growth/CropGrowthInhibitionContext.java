package org.confluence.phase_journey.common.phase.growth;

import java.util.List;

import org.confluence.phase_journey.common.phase.PhaseContext;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class CropGrowthInhibitionContext extends PhaseContext {

    public static final MapCodec<CropGrowthInhibitionContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(CropGrowthInhibitionContext::getPhase),
            ResourceKey.codec(Registries.BLOCK).listOf().optionalFieldOf("banned_blocks", java.util.List.of()).forGetter(CropGrowthInhibitionContext::bannedBlocks),
            Codec.BOOL.fieldOf("disable_all").orElse(false).forGetter(CropGrowthInhibitionContext::disableAll)
    ).apply(instance, CropGrowthInhibitionContext::new));

    private final List<ResourceKey<Block>> bannedBlocks;
    private final boolean disableAll;

    public CropGrowthInhibitionContext(ResourceLocation phase, List<ResourceKey<Block>> bannedBlocks, boolean disableAll) {
        super(phase);
        this.bannedBlocks = bannedBlocks;
        this.disableAll = disableAll;
    }

    public List<ResourceKey<Block>> bannedBlocks() {
        return bannedBlocks;
    }

    public boolean disableAll() {
        return disableAll;
    }

    @Override
    public MapCodec<CropGrowthInhibitionContext> codec() {
        return CODEC;
    }
}
