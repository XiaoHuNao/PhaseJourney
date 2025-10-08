package org.confluence.phase_journey.common.attachment;

import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.confluence.phase_journey.api.phase.IPhaseCapability;
import org.confluence.phase_journey.api.event.PhaseJourneyEvent;
import org.confluence.phase_journey.common.init.PJAttachments;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@ParametersAreNonnullByDefault
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
    public ListTag serializeNBT(HolderLookup.Provider provider) {
        ListTag tags = new ListTag();
        for (ResourceLocation phase : phases) {
            ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, phase).result().ifPresent(tags::add);
        }
        return tags;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, ListTag nbt) {
        for (Tag tag : nbt) {
            ResourceLocation.CODEC.parse(NbtOps.INSTANCE, tag).result().ifPresent(phases::add);
        }
    }

    public static PhaseAttachment of(Player player) {
        return player.getData(PJAttachments.PHASE);
    }

    public static PhaseAttachment of(Level level) {
        if (level.isClientSide) {
            return level.getData(PJAttachments.PHASE); // 客户端世界直接获取
        } else {
            return ((ServerLevel) level).getServer().overworld().getData(PJAttachments.PHASE); // 服务端世界仅获取主世界的
        }
    }

    public static PhaseAttachment of(MinecraftServer server) {
        return of(server.overworld());
    }
}
