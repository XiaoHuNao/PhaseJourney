package org.confluence.phase_journey.integration.waystones.phase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;

import net.blay09.mods.waystones.api.event.WaystoneTeleportEvent;
import net.blay09.mods.waystones.api.WaystoneTeleportContext;
import net.blay09.mods.waystones.api.Waystone;

public class WaystoneTeleportPhaseManager extends PhaseManager<WaystoneTeleportPhaseContext> {

    public static final WaystoneTeleportPhaseManager MANAGER = new WaystoneTeleportPhaseManager();

    private boolean deny(ServerPlayer player, WaystoneTeleportEvent.Pre event) {
        for (Map.Entry<ResourceLocation, Collection<WaystoneTeleportPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) {
                continue;
            }
            for (WaystoneTeleportPhaseContext ctx : entry.getValue()) {
                if (ctx.disableAll()) {
                    return true;
                }
                WaystoneTeleportContext context = event.getContext();
                Waystone target = context.getTargetWaystone();

                // blocked dimensions restriction
                if (!ctx.blockedDimensions().isEmpty()) {
                    ResourceLocation targetDim = target.getDimension().location();
                    if (ctx.blockedDimensions().contains(targetDim)) {
                        return true;
                    }
                }

                // same dimension restriction
                if (ctx.sameDimensionOnly()) {
                    ServerLevel playerLevel = player.serverLevel();
                    if (!target.getDimension().location().equals(playerLevel.dimension().location())) {
                        return true;
                    }
                }

                // max distance restriction (only applies if same dimension)
                double maxDistance = ctx.maxDistance();
                if (maxDistance >= 0) {
                    ServerLevel playerLevel = player.serverLevel();
                    if (target.getDimension().location().equals(playerLevel.dimension().location())) {
                        Vec3 playerPos = player.position();
                        Vec3 targetPos = new Vec3(target.getPos().getX() + 0.5, target.getPos().getY() + 0.5, target.getPos().getZ() + 0.5);
                        if (playerPos.distanceToSqr(targetPos) > maxDistance * maxDistance) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onWaystoneTeleport(WaystoneTeleportEvent.Pre event) {
        if (!(event.getContext().getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (deny(player, event)) {
            event.setCanceled(true);
        }
    }
}


