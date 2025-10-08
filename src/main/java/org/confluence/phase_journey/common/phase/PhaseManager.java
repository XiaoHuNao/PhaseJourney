package org.confluence.phase_journey.common.phase;



import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.confluence.phase_journey.api.phase.IPhaseContext;

import java.util.Collection;

public abstract class PhaseManager <T extends IPhaseContext> {
    protected final Multimap<PhaseType, Pair<ResourceLocation, T>> phaseContexts = ArrayListMultimap.create();

    public void register(PhaseType type,ResourceLocation phase, T phaseContext) {
        phaseContexts.put(type, Pair.of(phase, phaseContext));
    }

    public Collection<Pair<ResourceLocation, T>> getPhases(PhaseType type) {
        return phaseContexts.get(type);
    }

    public Collection<T> getPhaseContexts(PhaseType type, ResourceLocation phase){
        return phaseContexts.get(type).stream()
                .filter(pair -> pair.getFirst().equals(phase))
                .map(Pair::getSecond)
                .toList();
    }

    public Collection<T> getPhaseContexts(PhaseType type){
        return phaseContexts.get(type).stream()
                .map(Pair::getSecond)
                .toList();
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
