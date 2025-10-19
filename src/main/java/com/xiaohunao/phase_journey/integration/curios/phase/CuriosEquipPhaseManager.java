package com.xiaohunao.phase_journey.integration.curios.phase;

import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioCanEquipEvent;

public class CuriosEquipPhaseManager extends PhaseManager<CuriosEquipPhaseContext> {

    public static final CuriosEquipPhaseManager MANAGER = new CuriosEquipPhaseManager();

    public boolean isRestricted(Level level, Player player, String slotIdentifier) {
        return isRestricted(level,null, player,ctx -> {
            return ctx.bannedSlots().contains(slotIdentifier);
        });
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
