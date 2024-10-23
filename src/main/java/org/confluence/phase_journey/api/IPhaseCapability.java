package org.confluence.phase_journey.api;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public interface IPhaseCapability {
    Set<ResourceLocation> getPhases();

    void addPhase(ResourceLocation phase);

    void removePhase(ResourceLocation phase);
}
