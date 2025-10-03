package org.confluence.phase_journey.integration.ironsspellbooks.phase;

import java.util.Collection;
import java.util.Map;

import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

public class SpellCastPhaseManager extends PhaseManager<SpellCastPhaseContext> {

    public static final SpellCastPhaseManager MANAGER = new SpellCastPhaseManager();

    private boolean deny(ServerPlayer player, SpellPreCastEvent event) {
        for (Map.Entry<ResourceLocation, Collection<SpellCastPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) {
                continue;
            }
            for (SpellCastPhaseContext ctx : entry.getValue()) {
                if (ctx.disableAll()) {
                    return true;
                }
                java.util.List<ResourceLocation> configuredSchools = ctx.schoolTypes();
                if (!configuredSchools.isEmpty()) {
                    SchoolType school = event.getSchoolType();
                    ResourceLocation schoolId = school.getId();
                    if (configuredSchools.contains(schoolId)) {
                        return true;
                    }
                }
                java.util.List<String> configuredSpellIds = ctx.spellIds();
                if (!configuredSpellIds.isEmpty()) {
                    String spellId = event.getSpellId();
                    if (configuredSpellIds.contains(spellId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onSpellPreCast(SpellPreCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (deny(player, event)) {
            event.setCanceled(true);
        }
    }
}
