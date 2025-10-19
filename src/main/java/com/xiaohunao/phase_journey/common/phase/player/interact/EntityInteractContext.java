package com.xiaohunao.phase_journey.common.phase.player.interact;

import com.google.common.collect.Lists;
import com.xiaohunao.phase_journey.common.phase.InteractType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class EntityInteractContext extends InteractContext {
    public final EntityType<?> entityType;

    public EntityInteractContext(ResourceLocation phase, List<InteractType> interactTypes, EntityType<?> entityType) {
        super(phase,entityType,interactTypes);
        this.entityType = entityType;
    }

    public static Builder builder(ResourceLocation phase, EntityType<?> entityType) {
        return new Builder(phase, entityType);
    }

    public static final class Builder {
        private final ResourceLocation phase;
        private final EntityType<?> entityType;
        private final List<InteractType> interactTypes = Lists.newArrayList();

        public Builder(ResourceLocation phase, EntityType<?> entityType) {
            this.phase = phase;
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
            return new EntityInteractContext(phase,interactTypes,entityType);
        }
    }


}
