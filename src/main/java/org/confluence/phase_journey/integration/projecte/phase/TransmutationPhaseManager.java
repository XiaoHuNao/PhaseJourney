package org.confluence.phase_journey.integration.projecte.phase;

import java.util.Map;

import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.PhaseType;

import com.mojang.datafixers.util.Pair;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import moze_intel.projecte.api.event.PlayerAttemptCondenserSetEvent;
import moze_intel.projecte.api.event.PlayerAttemptLearnEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;

public class TransmutationPhaseManager extends PhaseManager<TransmutationPhaseContext> {

    public static final TransmutationPhaseManager MANAGER = new TransmutationPhaseManager();

    public boolean isInsertRestricted(Level level, Player player, Item item) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, TransmutationPhaseContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            TransmutationPhaseContext phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = phaseContext.getPhase();

            if (!phaseContext.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, null, player);
            if (phaseAttachment == null) {
                return false;
            }

            return phaseAttachment.ifPhaseAbsent(phase, () -> phaseContext.disableAllItems() || phaseContext.bannedItems().contains(item));
        }
        return false;
    }

    public boolean isLearnRestricted(Level level, Player player) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, TransmutationPhaseContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            TransmutationPhaseContext phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = phaseContext.getPhase();

            if (!phaseContext.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, null, player);
            if (phaseAttachment == null) {
                return false;
            }

            if (phaseAttachment.ifPhaseAbsent(phase, phaseContext::allowEmcGainButNoLearn)) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onPlayerAttemptLearn(PlayerAttemptLearnEvent event) {
        if (isInsertRestricted(event.getPlayer().level(), event.getPlayer(), event.getSourceInfo().getItem().value())) {
            event.setCanceled(true);
            return;
        }
        if (isLearnRestricted(event.getPlayer().level(), event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerAttemptCondenserSet(PlayerAttemptCondenserSetEvent event) {
        if (isInsertRestricted(event.getPlayer().level(), event.getPlayer(), event.getSourceInfo().getItem().value())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMultiblockHandlerMultiblockForm(MultiblockHandler.MultiblockFormEvent event) {
    }

}
