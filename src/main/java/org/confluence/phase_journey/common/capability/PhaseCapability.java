package org.confluence.phase_journey.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.confluence.phase_journey.api.IPhaseCapability;
import org.confluence.phase_journey.common.init.ModCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;


public class PhaseCapability implements IPhaseCapability,INBTSerializable<ListTag> {
    private Set<ResourceLocation> phases;

    @Override
    public Set<ResourceLocation> getPhases() {
        return phases;
    }

    @Override
    public ListTag serializeNBT() {
        ListTag tags = new ListTag();
        for (ResourceLocation phase : phases) {
            ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, phase).result().ifPresent(tags::add);
        }
        return tags;
    }


    @Override
    public void deserializeNBT(ListTag nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            ResourceLocation.CODEC.parse(NbtOps.INSTANCE, nbt.get(i)).result().ifPresent(phases::add);
        }
    }

    public static class Provider implements ICapabilityProvider {
        private final LazyOptional<PhaseCapability> instance;

        public Provider() {
            this.instance = LazyOptional.of(PhaseCapability::new);
        }
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ModCapability.PHASE.orEmpty(cap, instance);
        }
    }
}