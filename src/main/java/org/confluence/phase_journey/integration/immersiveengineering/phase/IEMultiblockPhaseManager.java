package org.confluence.phase_journey.integration.immersiveengineering.phase;

import java.util.Collection;
import java.util.Map;

import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;

public class IEMultiblockPhaseManager extends PhaseManager<IEMultiblockPhaseContext> {

    public static final IEMultiblockPhaseManager MANAGER = new IEMultiblockPhaseManager();

    public boolean deny(Player player, ResourceLocation multiblockId) {
        for (Map.Entry<ResourceLocation, Collection<IEMultiblockPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) {
                continue;
            }
            for (IEMultiblockPhaseContext ctx : entry.getValue()) {
                if (ctx.disableAll()) {
                    return true;
                }
                if (ctx.bannedMultiblocks().contains(multiblockId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onMultiblockForm(MultiblockHandler.MultiblockFormEvent event) {
        Player player = event.getEntity();
        ResourceLocation id = event.getMultiblock().getUniqueName();
        if (deny(player, id)) {
            event.setCanceled(true);
        }
    }
}
