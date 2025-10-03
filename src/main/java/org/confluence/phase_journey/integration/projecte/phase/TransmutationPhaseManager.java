package org.confluence.phase_journey.integration.projecte.phase;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import moze_intel.projecte.api.event.PlayerAttemptCondenserSetEvent;
import moze_intel.projecte.api.event.PlayerAttemptLearnEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;

public class TransmutationPhaseManager extends PhaseManager<TransmutationPhaseContext> {
    public static final TransmutationPhaseManager MANAGER = new TransmutationPhaseManager();

    public boolean denyInsert(Player player, Item item) {
        for (Map.Entry<ResourceLocation, Collection<TransmutationPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            for (TransmutationPhaseContext ctx : entry.getValue()) {
                if (ctx.disableAllItems()) {
                    return true;
                }
                if (ctx.bannedItems().contains(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean allowEmcGainButNoLearn(Player player) {
        for (Map.Entry<ResourceLocation, Collection<TransmutationPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) continue;
            for (TransmutationPhaseContext ctx : entry.getValue()) {
                if (ctx.allowEmcGainButNoLearn()) {
                    return true;
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onPlayerAttemptLearn(PlayerAttemptLearnEvent event) {
        if (denyInsert(event.getPlayer(), event.getSourceInfo().getItem().value())) {
            event.setCanceled(true);
            return;
        }
        if (allowEmcGainButNoLearn(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerAttemptCondenserSet(PlayerAttemptCondenserSetEvent event) {
        if (denyInsert(event.getPlayer(), event.getSourceInfo().getItem().value())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMultiblockHandlerMultiblockForm(MultiblockHandler.MultiblockFormEvent event) {
    }

}


