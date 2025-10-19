package com.xiaohunao.phase_journey.common.phase;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.api.phase.IPhaseContext;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class PhaseManager <T extends IPhaseContext> {
    public static final Logger LOGGER = LoggerFactory.getLogger(PhaseManager.class);
    protected final Multimap<PhaseType, Pair<ResourceLocation, T>> phaseContexts = ArrayListMultimap.create();

    public void register(PhaseType type,ResourceLocation phase, T phaseContext) {
        if (phaseContext.getSupportedPhaseTypes().contains(type)){
            phaseContexts.put(type, Pair.of(phase, phaseContext));
        }else {
            LOGGER.error("PhaseContext {} does not support phase type {}, supported phase types: {}", phaseContext, type, phaseContext.getSupportedPhaseTypes());
        }
    }

    public Collection<Pair<ResourceLocation, T>> getPhases(PhaseType type) {
        return phaseContexts.get(type);
    }

    public boolean isRestricted(@Nullable Level level, @Nullable BlockPos pos, @Nullable Player player, Function<T,Boolean> ctx){
        for (Map.Entry<PhaseType, Pair<ResourceLocation, T>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            T phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = entry.getValue().getFirst();
            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, pos, player);
            if (phaseAttachment.getPhases().contains(phase)) {
                return false;
            }
            return ctx.apply(phaseContext);
        }
        return false;
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

    public void broadcastPhaseChangeToClient(ResourceLocation phase, boolean add) {

    }

    public void achievePlayerPhase(ServerPlayer player, ResourceLocation phase, boolean add){

    }

    public void achieveLevelPhase(ServerLevel serverLevel, ResourceLocation phase, boolean add){

    }
}
