package com.xiaohunao.phase_journey.common.phase.effect;

import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import javax.annotation.Nullable;
import java.util.Map;

public class MobEffectPhaseManager extends PhaseManager<MobEffectApplicableContext> {
    public static final MobEffectPhaseManager MANAGER = new MobEffectPhaseManager();

    public boolean isRestricted(Level level, @Nullable Player player, Holder<MobEffect> effect, LivingEntity  entity){
        return isRestricted(level,null,player,ctx -> {
            if (ctx.entityTypes().contains(entity.getType())) {
                if (ctx.disableAll()) {
                    return true;
                }

                return ctx.bannedEffects().contains(effect);
            }
            return false;
        });
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


