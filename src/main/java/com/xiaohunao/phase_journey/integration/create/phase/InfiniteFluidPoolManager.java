package com.xiaohunao.phase_journey.integration.create.phase;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;

/**
 * 无限流体池管理器
 * 当机械动力软管滑轮判断流体方块达到10,000格时，该流体是否视为无限，由阶段判断
 */
public class InfiniteFluidPoolManager extends PhaseManager<InfiniteFluidPoolContext> {
    public static final InfiniteFluidPoolManager MANAGER = new InfiniteFluidPoolManager();

    private final BiMap<Fluid, InfiniteFluidPoolContext> fluidInfiniteContexts = HashBiMap.create();

    @Override
    public void register(PhaseType type,ResourceLocation phase, InfiniteFluidPoolContext context) {
        phaseContexts.put(type, Pair.of(phase, context));
        fluidInfiniteContexts.put(context.getFluid(), context);
    }

    public boolean isRestricted(Level level, BlockPos pos, ServerPlayer player, Fluid fluid) {
        InfiniteFluidPoolContext context = fluidInfiniteContexts.get(fluid);
        if (context == null) {
            return false;
        }
        
        for (Map.Entry<PhaseType, Pair<ResourceLocation, InfiniteFluidPoolContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            ResourceLocation phase = entry.getValue().getFirst();
            InfiniteFluidPoolContext ctx = entry.getValue().getSecond();

            if (!ctx.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            if (!fluid.equals(ctx.getFluid())) {
                continue;
            }

            PhaseAttachment attachment = phaseType.getPhaseAttachment(level, pos, player);
            if (attachment == null) {
                return false;
            }

            return attachment.ifPhaseAbsent(phase, () -> true,false);
        }
        return false;
    }
}
