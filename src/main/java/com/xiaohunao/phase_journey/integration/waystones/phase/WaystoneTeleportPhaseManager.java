package com.xiaohunao.phase_journey.integration.waystones.phase;

import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import net.blay09.mods.waystones.api.Waystone;
import net.blay09.mods.waystones.api.WaystoneTeleportContext;
import net.blay09.mods.waystones.api.event.WaystoneTeleportEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;

public class WaystoneTeleportPhaseManager extends PhaseManager<WaystoneTeleportPhaseContext> {

    public static final WaystoneTeleportPhaseManager MANAGER = new WaystoneTeleportPhaseManager();

    public boolean isRestricted(Level level, BlockPos pos, Player player, WaystoneTeleportContext waystoneTeleportContext){
        return isRestricted(level,pos,player,ctx -> {
            if (ctx.disableAll()) {
                return true;
            }

            Waystone target = waystoneTeleportContext.getTargetWaystone();
            if (!ctx.blockedDimensions().isEmpty()) {
                ResourceLocation targetDim = target.getDimension().location();
                if (ctx.blockedDimensions().contains(targetDim)) {
                    return true;
                }
            }

            if (ctx.sameDimensionOnly()) {
                if (!target.getDimension().location().equals(level.dimension().location())) {
                    return true;
                }
            }

            double maxDistance = ctx.maxDistance();
            if (maxDistance >= 0) {
                if (target.getDimension().location().equals(level.dimension().location())) {
                    Vec3 playerPos = player.position();
                    Vec3 targetPos = new Vec3(target.getPos().getX() + 0.5, target.getPos().getY() + 0.5, target.getPos().getZ() + 0.5);
                    return playerPos.distanceToSqr(targetPos) > maxDistance * maxDistance;
                }
            }
            return false;
        });
    }

    @SubscribeEvent
    public void onWaystoneTeleport(WaystoneTeleportEvent.Pre event) {
        WaystoneTeleportContext waystoneTeleportContext = event.getContext();
        if (!(waystoneTeleportContext.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (isRestricted(player.level(), waystoneTeleportContext.getTargetWaystone().getPos(), player,waystoneTeleportContext)) {
            event.setCanceled(true);
        }
    }
}


