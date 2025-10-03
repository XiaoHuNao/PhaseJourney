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

public class CropGrowthPhaseContext extends PhaseContext {

    public static final MapCodec<CropGrowthPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(CropGrowthPhaseContext::getPhase),
            ResourceKey.codec(Registries.BLOCK).listOf().optionalFieldOf("banned_blocks", java.util.List.of()).forGetter(CropGrowthPhaseContext::bannedBlocks),
            Codec.BOOL.fieldOf("disable_all").orElse(false).forGetter(CropGrowthPhaseContext::disableAll)
    ).apply(instance, CropGrowthPhaseContext::new));

    private final List<ResourceKey<Block>> bannedBlocks;
    private final boolean disableAll;

    public CropGrowthPhaseContext(ResourceLocation phase, List<ResourceKey<Block>> bannedBlocks, boolean disableAll) {
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
    public MapCodec<CropGrowthPhaseContext> codec() {
        return CODEC;
    }
}
