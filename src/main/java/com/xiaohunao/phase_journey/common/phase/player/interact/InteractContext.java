package com.xiaohunao.phase_journey.common.phase.player.interact;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.xiaohunao.phase_journey.common.phase.InteractType;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import com.xiaohunao.phase_journey.common.phase.player.PlayerRestrictedContext;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;
import java.util.function.Function;

public class InteractContext extends PlayerRestrictedContext {
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


}
