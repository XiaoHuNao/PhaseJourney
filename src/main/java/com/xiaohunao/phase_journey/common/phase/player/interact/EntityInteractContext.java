package com.xiaohunao.phase_journey.common.phase.player.interact;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.common.phase.InteractType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class EntityInteractContext extends InteractContext {
    public static final MapCodec<EntityInteractContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(EntityInteractContext::getPhase),
            InteractType.CODEC.listOf().fieldOf("interact_types").forGetter(EntityInteractContext::getInteractTypes),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInteractContext::getEntityType),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("target_type").forGetter(EntityInteractContext::getTargetType)
    ).apply(instance, EntityInteractContext::new));


    public final EntityType<?> entityType;

    public EntityInteractContext(ResourceLocation phase, List<InteractType> interactTypes, EntityType<?> targetType, EntityType<?> entityType) {
        super(phase,targetType,interactTypes);
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public static Builder builder(ResourceLocation phase,EntityType<?> targetType, EntityType<?> entityType) {
        return new Builder(phase,targetType,entityType);
    }

    @Override
    public MapCodec<? extends IPhaseContext> codec() {
        return CODEC;
    }

    public static final class Builder {
        private final ResourceLocation phase;
        private final EntityType<?> entityType;
        private final EntityType<?> targetType;
        private final List<InteractType> interactTypes = Lists.newArrayList();

        public Builder(ResourceLocation phase,EntityType<?> targetType, EntityType<?> entityType) {
            this.phase = phase;
            this.targetType = targetType;
            this.entityType = entityType;
        }

        public Builder denyTame() {
            interactTypes.add(InteractType.TAME);
            return this;
        }

        public Builder denyBreed() {
            interactTypes.add(InteractType.BREED);
            return this;
        }

        public Builder denyRide() {
            interactTypes.add(InteractType.RIDE);
            return this;
        }

        public Builder denyAttack() {
            interactTypes.add(InteractType.ATTACK);
            return this;
        }

        public Builder denyInteract() {
            interactTypes.add(InteractType.ENTITY_INTERACT);
            return this;
        }

        public Builder denyUseItem(){
            interactTypes.add(InteractType.USE_ITEM);
            return this;
        }

        public EntityInteractContext build() {
            return new EntityInteractContext(phase,interactTypes,targetType,entityType);
        }
    }


}
