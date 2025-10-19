package com.xiaohunao.phase_journey.common.phase;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.List;

public abstract class PhaseContext implements IPhaseContext {
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
    public abstract MapCodec<? extends IPhaseContext> codec();
}
