package org.confluence.phase_journey.common.phase.interaction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.confluence.phase_journey.api.IPhaseContext;
import org.confluence.phase_journey.common.phase.PhaseContext;

public record EntityInteractionPhaseContext(
        ResourceLocation phase,
        EntityType<?> entityType,
        boolean allowTame, //驯服
        boolean allowBreed, //繁育
        boolean allowRide, //骑乘
        boolean allowFeed, //喂食
        boolean allowInteract, //交互
        boolean allowBlockPlace, //放置方块
        boolean allowBlockBreak, //破坏方块
        boolean allowBlockInteract, //方块交互
        boolean allowItemUse, //使用物品
        boolean allowEntityAttack //攻击
) implements IPhaseContext {

    public static final MapCodec<EntityInteractionPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(EntityInteractionPhaseContext::phase),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInteractionPhaseContext::entityType),
            Codec.BOOL.optionalFieldOf("allow_tame", true).forGetter(EntityInteractionPhaseContext::allowTame),
            Codec.BOOL.optionalFieldOf("allow_breed", true).forGetter(EntityInteractionPhaseContext::allowBreed),
            Codec.BOOL.optionalFieldOf("allow_ride", true).forGetter(EntityInteractionPhaseContext::allowRide),
            Codec.BOOL.optionalFieldOf("allow_feed", true).forGetter(EntityInteractionPhaseContext::allowFeed),
            Codec.BOOL.optionalFieldOf("allow_interact", true).forGetter(EntityInteractionPhaseContext::allowInteract),
            Codec.BOOL.optionalFieldOf("allow_block_place", true).forGetter(EntityInteractionPhaseContext::allowBlockPlace),
            Codec.BOOL.optionalFieldOf("allow_block_break", true).forGetter(EntityInteractionPhaseContext::allowBlockBreak),
            Codec.BOOL.optionalFieldOf("allow_block_interact", true).forGetter(EntityInteractionPhaseContext::allowBlockInteract),
            Codec.BOOL.optionalFieldOf("allow_item_use", true).forGetter(EntityInteractionPhaseContext::allowItemUse),
            Codec.BOOL.optionalFieldOf("allow_entity_attack", true).forGetter(EntityInteractionPhaseContext::allowEntityAttack)
    ).apply(instance, EntityInteractionPhaseContext::new));

    @Override
    public MapCodec<EntityInteractionPhaseContext> codec() {
        return CODEC;
    }

	public static Builder builder(ResourceLocation phase, EntityType<?> entityType) {
        return new Builder(phase,entityType);
    }

	@Override
	public ResourceLocation getPhase() {
		return phase;
	}

	public static final class Builder {
        private final ResourceLocation phase;
        private final EntityType<?> entityType;
        private boolean allowTame = true;
        private boolean allowBreed = true;
        private boolean allowRide = true;
        private boolean allowFeed = true;
        private boolean allowInteract = true;
        private boolean allowBlockPlace = true;
        private boolean allowBlockBreak = true;
        private boolean allowBlockInteract = true;
        private boolean allowItemUse = true;
        private boolean allowEntityAttack = true;

		public Builder(ResourceLocation phase, EntityType<?> entityType) {
			this.phase = phase;
			this.entityType = entityType;
		}

		public Builder allowTame(boolean value) { this.allowTame = value; return this; }
        public Builder allowBreed(boolean value) { this.allowBreed = value; return this; }
        public Builder allowRide(boolean value) { this.allowRide = value; return this; }
        public Builder allowFeed(boolean value) { this.allowFeed = value; return this; }
        public Builder allowInteract(boolean value) { this.allowInteract = value; return this; }
        public Builder allowBlockPlace(boolean value) { this.allowBlockPlace = value; return this; }
        public Builder allowBlockBreak(boolean value) { this.allowBlockBreak = value; return this; }
        public Builder allowBlockInteract(boolean value) { this.allowBlockInteract = value; return this; }
        public Builder allowItemUse(boolean value) { this.allowItemUse = value; return this; }
        public Builder allowEntityAttack(boolean value) { this.allowEntityAttack = value; return this; }

        public EntityInteractionPhaseContext build() {
            return new EntityInteractionPhaseContext(
                    phase,
					entityType,
                    allowTame,
                    allowBreed,
                    allowRide,
                    allowFeed,
                    allowInteract,
                    allowBlockPlace,
                    allowBlockBreak,
                    allowBlockInteract,
                    allowItemUse,
                    allowEntityAttack
            );
        }
    }
}


