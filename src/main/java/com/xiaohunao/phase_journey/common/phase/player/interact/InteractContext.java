package com.xiaohunao.phase_journey.common.phase.player.interact;

import com.xiaohunao.phase_journey.common.phase.InteractType;
import com.xiaohunao.phase_journey.common.phase.player.PlayerRestrictedContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public abstract class InteractContext extends PlayerRestrictedContext {
    public final EntityType<?> targetType;
    public final List<InteractType> interactTypes;

    public InteractContext(ResourceLocation phase, EntityType<?> targetType,List<InteractType> interactTypes) {
        super(phase);
        this.targetType = targetType;
        this.interactTypes = interactTypes;
    }

    public EntityType<?> getTargetType() {
        return targetType;
    }

    public List<InteractType> getInteractTypes() {
        return interactTypes;
    }
}
