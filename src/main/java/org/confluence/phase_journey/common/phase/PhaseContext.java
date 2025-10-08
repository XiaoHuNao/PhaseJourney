package org.confluence.phase_journey.common.phase;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.confluence.phase_journey.api.phase.IPhaseContext;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;

import java.util.Collection;
import java.util.List;

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

    public Collection<PhaseType> getSupportedPhaseTypes(){
        return List.of(PhaseType.LEVEL,PhaseType.PLAYER);
    }


    @Override
    public MapCodec<? extends IPhaseContext> codec() {
        return CODEC;
    }
}
