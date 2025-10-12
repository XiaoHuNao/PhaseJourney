package org.confluence.phase_journey.integration.ironsspellbooks.phase;

import java.util.Map;

import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.PhaseType;

import com.mojang.datafixers.util.Pair;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;

public class SpellCastPhaseManager extends PhaseManager<SpellCastPhaseContext> {

    public static final SpellCastPhaseManager MANAGER = new SpellCastPhaseManager();

    public boolean isRestricted(Level level, ServerPlayer player, SpellPreCastEvent event) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, SpellCastPhaseContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            SpellCastPhaseContext phaseContext = entry.getValue().getSecond();
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
                java.util.List<ResourceLocation> configuredSchools = phaseContext.schoolTypes();
                if (!configuredSchools.isEmpty()) {
                    SchoolType school = event.getSchoolType();
                    ResourceLocation schoolId = school.getId();
                    if (configuredSchools.contains(schoolId)) {
                        return true;
                    }
                }
                java.util.List<String> configuredSpellIds = phaseContext.spellIds();
                if (!configuredSpellIds.isEmpty()) {
                    String spellId = event.getSpellId();
                    if (configuredSpellIds.contains(spellId)) {
                        return true;
                    }
                }
                return false;
            },false);
        }
        return false;
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
