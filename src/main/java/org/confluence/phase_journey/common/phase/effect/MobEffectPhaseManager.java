package org.confluence.phase_journey.common.phase.effect;

import java.util.Map;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.PhaseType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.minecraft.core.Holder;

public class MobEffectPhaseManager extends PhaseManager<MobEffectApplicableContext> {
    public static final MobEffectPhaseManager MANAGER = new MobEffectPhaseManager();

    public boolean isRestricted(Level level,Player player,Holder<MobEffect> effect,LivingEntity  entity){
        for (Map.Entry<PhaseType, Pair<ResourceLocation, MobEffectApplicableContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            MobEffectApplicableContext phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = phaseContext.getPhase();

            if (!phaseContext.getSupportedPhaseTypes().contains(phaseType)){
                continue;
            }

            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, null, player);
            if (phaseAttachment == null){
                return false;
            }

            return phaseAttachment.ifPhaseAbsent(phase, () -> {
                if (phaseContext.entityTypes().contains(entity.getType())) {
                    if (phaseContext.disableAll()) {
                        return true;
                    }

                    return phaseContext.bannedEffects().contains(effect);
                }
                return false;
            },false);
        }
        return false;
    }

    @SubscribeEvent
    public void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        Player player = entity instanceof Player ? (Player) entity : null;
        if (isRestricted(entity.level(),player,event.getEffectInstance().getEffect(),entity)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}


