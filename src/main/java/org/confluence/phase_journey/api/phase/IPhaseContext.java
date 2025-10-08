package org.confluence.phase_journey.api.phase;

import com.xiaohunao.xhn_lib.common.codec.ICodec;
import net.minecraft.resources.ResourceLocation;
import org.confluence.phase_journey.common.phase.PhaseType;

import java.util.Collection;

public interface IPhaseContext extends ICodec<IPhaseContext> {
    ResourceLocation getPhase();

    Collection<PhaseType> getSupportedPhaseTypes();

}
