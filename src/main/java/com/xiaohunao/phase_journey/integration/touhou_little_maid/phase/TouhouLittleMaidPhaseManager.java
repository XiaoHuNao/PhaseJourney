package com.xiaohunao.phase_journey.integration.touhou_little_maid.phase;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTaskEnableEvent;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;

public class TouhouLittleMaidPhaseManager extends PhaseManager<TouhouLittleMaidPhaseContext> {

    public static final TouhouLittleMaidPhaseManager MANAGER = new TouhouLittleMaidPhaseManager();

    public boolean isTaskRestricted(Level level, ServerPlayer player, ResourceLocation taskId) {
        for (java.util.Map.Entry<PhaseType, Pair<ResourceLocation, TouhouLittleMaidPhaseContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            TouhouLittleMaidPhaseContext phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = phaseContext.getPhase();

            if (!phaseContext.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, null, player);
            if (phaseAttachment == null) {
                return false;
            }

            return phaseAttachment.ifPhaseAbsent(phase, () -> !phaseContext.isTaskAllowed(taskId),false);
        }
        return false;
    }

    /**
     * 检查女仆是否可以切换到指定任务
     */
    public boolean canSwitchToTask(EntityMaid maid, ResourceLocation taskId) {
        Level level = maid.level();
        ServerPlayer owner = maid.getOwner() instanceof ServerPlayer sp ? sp : null;
        if (!(level instanceof net.minecraft.server.level.ServerLevel)) {
            return true;
        }
        return !isTaskRestricted(level, owner, taskId);
    }

    /**
     * 处理女仆任务启用事件
     */
    @SubscribeEvent
    public void onMaidTaskEnable(MaidTaskEnableEvent event) {
        EntityMaid maid = event.getEntityMaid();
        IMaidTask task = event.getTargetTask();

        if (!MANAGER.canSwitchToTask(maid, task.getUid())) {
            // 取消任务切换
            event.setCanceled(true);
        }
    }
}
