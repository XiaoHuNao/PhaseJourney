package com.xiaohunao.phase_journey.integration.waystones.phase;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.phase_journey.common.phase.PhaseContext;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class WaystoneTeleportPhaseContext extends PhaseContext {

    public static final MapCodec<WaystoneTeleportPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(WaystoneTeleportPhaseContext::getPhase),
            Codec.BOOL.fieldOf("disable_all").orElse(false).forGetter(WaystoneTeleportPhaseContext::disableAll),
            Codec.BOOL.fieldOf("same_dimension_only").orElse(false).forGetter(WaystoneTeleportPhaseContext::sameDimensionOnly),
            Codec.DOUBLE.fieldOf("max_distance").orElse(-1.0).forGetter(WaystoneTeleportPhaseContext::maxDistance),
            ResourceLocation.CODEC.listOf().optionalFieldOf("blocked_dimensions", List.of()).forGetter(WaystoneTeleportPhaseContext::blockedDimensions)
    ).apply(instance, WaystoneTeleportPhaseContext::new));

    private final boolean disableAll;
    private final boolean sameDimensionOnly;
    private final double maxDistance;
    private final List<ResourceLocation> blockedDimensions;

    public WaystoneTeleportPhaseContext(ResourceLocation phase, boolean disableAll, boolean sameDimensionOnly, double maxDistance, List<ResourceLocation> blockedDimensions) {
        super(phase);
        this.disableAll = disableAll;
        this.sameDimensionOnly = sameDimensionOnly;
        this.maxDistance = maxDistance;
        this.blockedDimensions = blockedDimensions;
    }

    public boolean disableAll() {
        return disableAll;
    }

    public boolean sameDimensionOnly() { return sameDimensionOnly; }

    public double maxDistance() { return maxDistance; }

    public List<ResourceLocation> blockedDimensions() { return blockedDimensions; }

    @Override
    public MapCodec<WaystoneTeleportPhaseContext> codec() {
        return CODEC;
    }
}


