package org.confluence.phase_journey.integration.ironsspellbooks.phase;

import org.confluence.phase_journey.common.phase.PhaseContext;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;

public class SpellCastPhaseContext extends PhaseContext {

    public static final MapCodec<SpellCastPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(SpellCastPhaseContext::getPhase),
            Codec.BOOL.fieldOf("disable_all").orElse(false).forGetter(SpellCastPhaseContext::disableAll),
            ResourceLocation.CODEC.listOf().optionalFieldOf("school_types", java.util.List.of()).forGetter(SpellCastPhaseContext::schoolTypes),
            Codec.STRING.listOf().optionalFieldOf("spell_ids", java.util.List.of()).forGetter(SpellCastPhaseContext::spellIds)
    ).apply(instance, SpellCastPhaseContext::new));

    private final boolean disableAll;
    private final java.util.List<ResourceLocation> schoolTypes;
    private final java.util.List<String> spellIds;

    public SpellCastPhaseContext(ResourceLocation phase, boolean disableAll, java.util.List<ResourceLocation> schoolTypes, java.util.List<String> spellIds) {
        super(phase);
        this.disableAll = disableAll;
        this.schoolTypes = java.util.List.copyOf(schoolTypes);
        this.spellIds = java.util.List.copyOf(spellIds);
    }

    public boolean disableAll() {
        return disableAll;
    }

    public java.util.List<ResourceLocation> schoolTypes() {
        return schoolTypes;
    }

    public java.util.List<String> spellIds() {
        return spellIds;
    }

    @Override
    public MapCodec<SpellCastPhaseContext> codec() {
        return CODEC;
    }
}
