package org.confluence.phase_journey.common.phase;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.confluence.phase_journey.api.IPhaseContext;

public class PhaseContext implements IPhaseContext {
    public static final MapCodec<PhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(PhaseContext::getPhase)
    ).apply(instance, PhaseContext::new));

    protected ResourceLocation phase;

    public PhaseContext(ResourceLocation phase) {
        this.phase = phase;
    }

    public ResourceLocation getPhase() {
        return phase;
    }

    public String getPhaseName() {
        return phase.toString();
    }

    @Override
    public MapCodec<? extends IPhaseContext> codec() {
        return CODEC;
    }
}
