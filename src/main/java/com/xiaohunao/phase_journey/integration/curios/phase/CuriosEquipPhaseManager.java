package com.xiaohunao.phase_journey.integration.curios.phase;

import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioCanEquipEvent;

import java.util.Map;

public class CuriosEquipPhaseManager extends PhaseManager<CuriosEquipPhaseContext> {

    public static final CuriosEquipPhaseManager MANAGER = new CuriosEquipPhaseManager();

    public boolean isRestricted(Level level, Player player, String slotIdentifier) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, CuriosEquipPhaseContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            CuriosEquipPhaseContext phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = phaseContext.getPhase();

            if (!phaseContext.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, null, player);
            if (phaseAttachment == null) {
                return false;
            }

            return phaseAttachment.ifPhaseAbsent(phase, () -> phaseContext.bannedSlots().contains(slotIdentifier),false);
        }
        return false;
    }

    @SubscribeEvent
    public void onCurioCanEquip(CurioCanEquipEvent event) {
        SlotContext ctx = event.getSlotContext();
        LivingEntity entity = ctx.entity();
        if (entity instanceof Player player) {
            if (isRestricted(player.level(), player, ctx.identifier())) {
                event.setEquipResult(TriState.FALSE);
            }
        }
    }
}
