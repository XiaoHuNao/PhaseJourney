package com.xiaohunao.phase_journey.api.phase;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import com.xiaohunao.xhn_lib.common.codec.ICodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.function.Function;

public interface IPhaseContext extends ICodec<IPhaseContext> {
    Codec<IPhaseContext> CODEC = Codec.lazyInitialized(PJRegistries.PHASE_CONTEXT_CODEC::byNameCodec).dispatch(IPhaseContext::codec, Function.identity());

    ResourceLocation getPhase();

    Collection<PhaseType> getSupportedPhaseTypes();
}
