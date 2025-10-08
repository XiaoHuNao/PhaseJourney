package org.confluence.phase_journey.common.phase.effect;

import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import org.confluence.phase_journey.common.phase.PhaseContext;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

public class MobEffectApplicableContext extends PhaseContext {

    public static final MapCodec<MobEffectApplicableContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(MobEffectApplicableContext::getPhase),
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().listOf().fieldOf("banned_effects").forGetter(MobEffectApplicableContext::bannedEffects),
            Codec.BOOL.fieldOf("disable_all").forGetter(MobEffectApplicableContext::disableAll),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("entity_types", java.util.List.of()).forGetter(MobEffectApplicableContext::entityTypes)
    ).apply(instance, MobEffectApplicableContext::new));

    private final List<Holder<MobEffect>> bannedEffects;
    private final boolean disableAll;
    private final List<EntityType<?>> entityTypes;

    public MobEffectApplicableContext(ResourceLocation phase, List<Holder<MobEffect>> bannedEffects, boolean disableAll) {
        this(phase, bannedEffects, disableAll, List.of(EntityType.PLAYER));
    }

    public MobEffectApplicableContext(ResourceLocation phase, List<Holder<MobEffect>> bannedEffects, boolean disableAll, List<EntityType<?>> entityTypes) {
        super(phase);
        this.bannedEffects = bannedEffects;
        this.disableAll = disableAll;
        this.entityTypes = entityTypes;
    }

    public List<Holder<MobEffect>> bannedEffects() {
        return bannedEffects;
    }

    public boolean disableAll() {
        return disableAll;
    }

    public List<EntityType<?>> entityTypes() {
        return entityTypes;
    }


    @Override
    public MapCodec<MobEffectApplicableContext> codec() {
        return CODEC;
    }
}


