package org.confluence.phase_journey.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.confluence.phase_journey.common.capability.PhaseCapability;
import org.confluence.phase_journey.common.init.ModCapability;

import java.util.Set;

public interface IPhaseCapability {
    static LazyOptional<PhaseCapability> get(Level level){
        return level.getCapability(ModCapability.PHASE);
    }
    static LazyOptional<PhaseCapability> get(Player player){
        return player.getCapability(ModCapability.PHASE);
    }


    Set<ResourceLocation> getPhases();

    void addPhase(ResourceLocation phase);

    void removePhase(ResourceLocation phase);
}
