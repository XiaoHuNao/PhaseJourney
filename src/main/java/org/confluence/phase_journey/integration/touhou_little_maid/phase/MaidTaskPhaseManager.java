package org.confluence.phase_journey.integration.touhou_little_maid.phase;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTaskEnableEvent;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.api.IPhaseCapability;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@EventBusSubscriber(modid = PhaseJourney.MODID)
public class MaidTaskPhaseManager extends PhaseManager<MaidTaskPhaseContext> {
    public static final MaidTaskPhaseManager MANAGER = new MaidTaskPhaseManager();

    /**
     * 检查女仆是否可以切换到指定任务
     */
    public boolean canSwitchToTask(EntityMaid maid, ResourceLocation taskId) {
        // 获取女仆所在的世界
        if (!(maid.level() instanceof ServerLevel serverLevel)) {
            return true; // 客户端或非服务器世界，允许切换
        }

        // 获取世界的阶段能力
        IPhaseCapability phaseCapability = PhaseUtils.getPhaseCapability();

        // 检查当前阶段是否允许该任务
        Set<ResourceLocation> currentPhases = phaseCapability.getPhases();
        for (ResourceLocation phase : currentPhases) {
            MaidTaskPhaseContext context = getPhaseContext(phase);
            if (context != null && !context.isTaskAllowed(taskId)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取指定阶段的上下文
     */
    private MaidTaskPhaseContext getPhaseContext(ResourceLocation phase) {
        return phaseContexts.get(phase).stream()
                .filter(context -> context instanceof MaidTaskPhaseContext)
                .map(context -> (MaidTaskPhaseContext) context)
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取女仆当前可用的任务列表
     */
    public List<IMaidTask> getAvailableTasks(EntityMaid maid) {
        if (!(maid.level() instanceof ServerLevel serverLevel)) {
            return TaskManager.getTaskIndex(); // 客户端返回所有任务
        }

        IPhaseCapability phaseCapability = PhaseUtils.getPhaseCapability();

        Set<ResourceLocation> currentPhases = phaseCapability.getPhases();
        return TaskManager.getTaskIndex().stream()
                .filter(task -> {
                    for (ResourceLocation phase : currentPhases) {
                        MaidTaskPhaseContext context = getPhaseContext(phase);
                        if (context != null && !context.isTaskAllowed(task.getUid())) {
                            return false;
                        }
                    }
                    return true;
                })
                .toList();
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
            
            // 发送消息给玩家
            if (maid.getOwner() instanceof ServerPlayer player) {
                player.sendSystemMessage(
                    net.minecraft.network.chat.Component.translatable(
                        "message.phase_journey.maid_task_blocked", 
                        task.getName()
                    )
                );
            }
        }
    }
}
