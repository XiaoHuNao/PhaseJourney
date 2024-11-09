package org.confluence.phase_journey.common.attachment;

import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.confluence.phase_journey.api.IPhaseCapability;
import org.confluence.phase_journey.api.PhaseJourneyEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PhaseAttachment implements IPhaseCapability, INBTSerializable<ListTag> {
    private final Set<ResourceLocation> phases = Sets.newHashSet();

    @Override
    public Set<ResourceLocation> getPhases() {
        return phases;
    }

    @Override
    public void addPhase(ResourceLocation phase) {
        if (!NeoForge.EVENT_BUS.post(new PhaseJourneyEvent.Add(phase)).isCanceled()) {
            phases.add(phase);
        }
    }

    @Override
    public void removePhase(ResourceLocation phase) {
        if (!NeoForge.EVENT_BUS.post(new PhaseJourneyEvent.Remove(phase)).isCanceled()) {
            phases.remove(phase);
        }
    }

    @Override
    public ListTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        ListTag tags = new ListTag();
        for (ResourceLocation phase : phases) {
            ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, phase).result().ifPresent(tags::add);
        }
        return tags;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, ListTag nbt) {
        for (Tag tag : nbt) {
            ResourceLocation.CODEC.parse(NbtOps.INSTANCE, tag).result().ifPresent(phases::add);
        }
    }
}
