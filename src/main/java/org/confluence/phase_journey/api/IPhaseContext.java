package org.confluence.phase_journey.api;

import com.xiaohunao.xhn_lib.common.codec.ICodec;
import net.minecraft.resources.ResourceLocation;

public interface IPhaseContext extends ICodec<IPhaseContext> {
    ResourceLocation getPhase();
}
