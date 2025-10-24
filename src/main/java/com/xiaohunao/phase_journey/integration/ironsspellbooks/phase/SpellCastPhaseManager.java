package com.xiaohunao.phase_journey.integration.ironsspellbooks.phase;

import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.List;

public class SpellCastPhaseManager extends PhaseManager<SpellCastPhaseContext> {

    public static final SpellCastPhaseManager MANAGER = new SpellCastPhaseManager();

    public boolean isRestricted(Level level, ServerPlayer player, SpellPreCastEvent event) {
        return PhaseUtils.anyContextMatches(this,level,player,null, (ctx, phaseManager) -> {
            if (ctx.disableAll()) {
                return true;
            }
            List<ResourceLocation> configuredSchools = ctx.schoolTypes();
            if (!configuredSchools.isEmpty()) {
                SchoolType school = event.getSchoolType();
                ResourceLocation schoolId = school.getId();
                if (configuredSchools.contains(schoolId)) {
                    return true;
                }
            }
            List<String> configuredSpellIds = ctx.spellIds();
            if (!configuredSpellIds.isEmpty()) {
                String spellId = event.getSpellId();
                if (configuredSpellIds.contains(spellId)) {
                    return true;
                }
            }
            return false;
        });
    }

    @SubscribeEvent
    public void onSpellPreCast(SpellPreCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (isRestricted(player.level(), player, event)) {
            event.setCanceled(true);
        }
    }
}
