package org.confluence.phase_journey.common.phase.effect;

import java.util.Collection;
import java.util.Map;

import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.minecraft.core.Holder;

public class MobEffectPhaseManager extends PhaseManager<MobEffectPhaseContext> {

    public static final MobEffectPhaseManager MANAGER = new MobEffectPhaseManager();

    private boolean deny(LivingEntity entity, ResourceKey<MobEffect> effectKey) {
        for (Map.Entry<ResourceLocation, Collection<MobEffectPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            Player player = entity instanceof Player p ? p : null;
            if (PhaseUtils.hadPlayerOrLevelAchievedPhase(entry.getKey(), player)) {
                continue;
            }
            for (MobEffectPhaseContext ctx : entry.getValue()) {
                // Default: applies to players only
                if (ctx.entityTypes().isEmpty()) {
                    if (!(entity instanceof Player)) {
                        continue;
                    }
                } else {
                    // If entity types are specified, only apply to matching types
                    ResourceLocation typeLocation = EntityType.getKey(entity.getType());
                    ResourceKey<net.minecraft.world.entity.EntityType<?>> typeKey = ResourceKey.create(Registries.ENTITY_TYPE, typeLocation);
                    if (!ctx.entityTypes().contains(typeKey)) {
                        continue;
                    }
                }
                if (ctx.disableAll()) {
                    return true;
                }
                if (ctx.bannedEffects().contains(effectKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        Holder<MobEffect> holder = event.getEffectInstance().getEffect();
        java.util.Optional<ResourceKey<MobEffect>> keyOpt = holder.unwrapKey();
        if (keyOpt.isEmpty()) return;
        ResourceKey<MobEffect> effectKey = keyOpt.get();
        if (deny(entity, effectKey)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}


