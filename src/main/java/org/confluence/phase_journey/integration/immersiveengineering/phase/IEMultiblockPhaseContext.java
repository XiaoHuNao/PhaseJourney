package org.confluence.phase_journey.integration.immersiveengineering.phase;

import java.util.List;

import org.confluence.phase_journey.common.phase.PhaseContext;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;

public class IEMultiblockPhaseContext extends PhaseContext {

    public static final MapCodec<IEMultiblockPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(IEMultiblockPhaseContext::phase),
            ResourceLocation.CODEC.listOf().fieldOf("banned_multiblocks").forGetter(IEMultiblockPhaseContext::bannedMultiblocks),
            Codec.BOOL.fieldOf("disable_all").forGetter(IEMultiblockPhaseContext::disableAll)
    ).apply(instance, IEMultiblockPhaseContext::new));

    private final List<ResourceLocation> bannedMultiblocks;
    private final boolean disableAll;

    public IEMultiblockPhaseContext(ResourceLocation phase, List<ResourceLocation> bannedMultiblocks, boolean disableAll) {
        super(phase);
        this.bannedMultiblocks = bannedMultiblocks;
        this.disableAll = disableAll;
    }

    public ResourceLocation phase() {
        return phase;
    }

    public List<ResourceLocation> bannedMultiblocks() {
        return bannedMultiblocks;
    }

    public boolean disableAll() {
        return disableAll;
    }
}
