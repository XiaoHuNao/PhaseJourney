package com.xiaohunao.phase_journey.integration.immersiveengineering.phase;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Map;

public class IEMultiblockPhaseManager extends PhaseManager<IEMultiblockPhaseContext> {

    public static final IEMultiblockPhaseManager MANAGER = new IEMultiblockPhaseManager();

    public boolean isRestricted(Level level, Player player, ResourceLocation multiblockId) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, IEMultiblockPhaseContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            IEMultiblockPhaseContext phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = phaseContext.getPhase();

            if (!phaseContext.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, null, player);
            if (phaseAttachment == null) {
                return false;
            }

            return phaseAttachment.ifPhaseAbsent(phase, () -> {
                if (phaseContext.disableAll()) {
                    return true;
                }
                return phaseContext.bannedMultiblocks().contains(multiblockId);
            },false);
        }
        return false;
    }

    @SubscribeEvent
    public void onMultiblockForm(MultiblockHandler.MultiblockFormEvent event) {
        Player player = event.getEntity();
        ResourceLocation id = event.getMultiblock().getUniqueName();
        if (isRestricted(player.level(), player, id)) {
            event.setCanceled(true);
        }
    }
}
