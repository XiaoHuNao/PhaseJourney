package org.confluence.phase_journey.common.phase.effect;

import java.util.List;

import org.confluence.phase_journey.common.phase.PhaseContext;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

public class MobEffectPhaseContext extends PhaseContext {

    public static final MapCodec<MobEffectPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(MobEffectPhaseContext::getPhase),
            ResourceKey.codec(Registries.MOB_EFFECT).listOf().fieldOf("banned_effects").forGetter(MobEffectPhaseContext::bannedEffects),
            Codec.BOOL.fieldOf("disable_all").forGetter(MobEffectPhaseContext::disableAll),
            ResourceKey.codec(Registries.ENTITY_TYPE).listOf().optionalFieldOf("entity_types", java.util.List.of()).forGetter(MobEffectPhaseContext::entityTypes)
    ).apply(instance, MobEffectPhaseContext::new));

    private final List<ResourceKey<MobEffect>> bannedEffects;
    private final boolean disableAll;
    private final List<ResourceKey<EntityType<?>>> entityTypes;

    public MobEffectPhaseContext(ResourceLocation phase, List<ResourceKey<MobEffect>> bannedEffects, boolean disableAll) {
        this(phase, bannedEffects, disableAll, List.of());
    }

    public MobEffectPhaseContext(ResourceLocation phase, List<ResourceKey<MobEffect>> bannedEffects, boolean disableAll, List<ResourceKey<EntityType<?>>> entityTypes) {
        super(phase);
        this.bannedEffects = bannedEffects;
        this.disableAll = disableAll;
        this.entityTypes = entityTypes;
    }

    public List<ResourceKey<MobEffect>> bannedEffects() {
        return bannedEffects;
    }

    public boolean disableAll() {
        return disableAll;
    }

    public List<ResourceKey<EntityType<?>>> entityTypes() {
        return entityTypes;
    }

    @Override
    public MapCodec<MobEffectPhaseContext> codec() {
        return CODEC;
    }
}


