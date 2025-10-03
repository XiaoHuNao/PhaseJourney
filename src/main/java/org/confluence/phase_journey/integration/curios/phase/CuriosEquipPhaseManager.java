package org.confluence.phase_journey.integration.curios.phase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioCanEquipEvent;

import java.util.Collection;
import java.util.Map;

public class CuriosEquipPhaseManager extends PhaseManager<CuriosEquipPhaseContext> {

    public static final CuriosEquipPhaseManager MANAGER = new CuriosEquipPhaseManager();

    public boolean deny(LivingEntity entity, SlotContext slotContext) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        for (Map.Entry<ResourceLocation, Collection<CuriosEquipPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) {
                continue;
            }
            for (CuriosEquipPhaseContext ctx : entry.getValue()) {
                if (ctx.bannedSlots().contains(slotContext.identifier())) {
                    return true;
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onCurioCanEquip(CurioCanEquipEvent event) {
        SlotContext ctx = event.getSlotContext();
        LivingEntity entity = ctx.entity();
        if (deny(entity, ctx)) {
            event.setEquipResult(TriState.FALSE);
        }
    }
}
