package org.confluence.phase_journey.common.phase;



import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.xiaohunao.xhn_lib.api.data.loader.BaseDynamicLoader;
import com.xiaohunao.xhn_lib.common.serialization.IDynamicSerializer;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.confluence.phase_journey.common.phase.block.BlockPhaseContext;

public class PhaseManager <T extends PhaseContext> {
    protected final Multimap<ResourceLocation, T> phaseContexts = ArrayListMultimap.create();

    public void register(ResourceLocation phase, T phaseContext) {
        this.phaseContexts.put(phase, phaseContext);
    }

    public void init() {

    }

    public void broadcastPhaseChangeToClient(ResourceLocation phase, boolean add) {

    }

    public void achievePlayerPhase(ServerPlayer player, ResourceLocation phase, boolean add){

    }

    public void achieveLevelPhase(ServerLevel serverLevel, ResourceLocation phase, boolean add){

    }
}
