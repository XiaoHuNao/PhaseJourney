package com.xiaohunao.phase_journey.api.phase;


import java.util.Collection;
import java.util.Map;
import java.util.function.*;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import com.xiaohunao.xhn_lib.api.data.loader.BaseDynamicLoader;
import com.xiaohunao.xhn_lib.common.serialization.IDynamicSerializer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class PhaseManager <T extends IPhaseContext> extends BaseDynamicLoader<T> {
    public static final Logger LOGGER = LoggerFactory.getLogger(PhaseManager.class);
    protected final Multimap<PhaseType, Pair<ResourceLocation, T>> phaseContexts = ArrayListMultimap.create();

    public PhaseManager(String folderName, Registry<T> registry, IDynamicSerializer<T> serializer) {
        super(folderName, registry, serializer);
    }

    public PhaseManager() {
        super((Registry<T>) PJRegistries.PHASE_CONTEXT, IDynamicSerializer.of((Codec<T>) IPhaseContext.CODEC));
    }

    @Override
    protected void loadNewValues(MappedRegistry<T> mappedRegistry, Map<ResourceLocation, JsonElement> resources) {
        init();
    }

    public void register(PhaseType type, ResourceLocation phase, T phaseContext) {
        if (phaseContext.getSupportedPhaseTypes().contains(type)){
            phaseContexts.put(type, Pair.of(phase, phaseContext));
        }else {
            LOGGER.error("PhaseContext {} does not support phase type {}, supported phase types: {}", phaseContext, type, phaseContext.getSupportedPhaseTypes());
        }
    }

    public Collection<Pair<ResourceLocation, T>> getPhases(PhaseType type) {
        return phaseContexts.get(type);
    }

    public Multimap<PhaseType, Pair<ResourceLocation, T>> getPhaseContexts() {
        return phaseContexts;
    }


    public void forEach(BiConsumer<ResourceLocation, T> consumer) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, T>> entry : phaseContexts.entries()) {
            consumer.accept(entry.getValue().getFirst(), entry.getValue().getSecond());
        }
    }

    public void forEach(PhaseType phaseType, BiConsumer<ResourceLocation, T> consumer) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, T>> entry : phaseContexts.entries()) {
            if (entry.getKey() == phaseType) {
                consumer.accept(entry.getValue().getFirst(), entry.getValue().getSecond());
            }
        }
    }


    public void init() {

    }

    public void applyOrRevokePhase(Level level,ResourceLocation phase, boolean add){

    }

    public void clear(){
        phaseContexts.clear();
    }

}
