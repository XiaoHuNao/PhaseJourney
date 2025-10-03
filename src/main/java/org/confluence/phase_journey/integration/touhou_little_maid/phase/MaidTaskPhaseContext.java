package org.confluence.phase_journey.integration.touhou_little_maid.phase;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.confluence.phase_journey.api.IPhaseContext;

import java.util.List;

public class MaidTaskPhaseContext implements IPhaseContext {
    public static final MapCodec<MaidTaskPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(MaidTaskPhaseContext::getPhase),
            ResourceLocation.CODEC.listOf().fieldOf("allowed_tasks").forGetter(MaidTaskPhaseContext::getAllowedTasks),
            ResourceLocation.CODEC.listOf().fieldOf("blocked_tasks").forGetter(MaidTaskPhaseContext::getBlockedTasks)
    ).apply(instance, MaidTaskPhaseContext::new));

    protected ResourceLocation phase;
    protected List<ResourceLocation> allowedTasks;
    protected List<ResourceLocation> blockedTasks;

    public MaidTaskPhaseContext(ResourceLocation phase, List<ResourceLocation> allowedTasks, List<ResourceLocation> blockedTasks) {
        this.phase = phase;
        this.allowedTasks = allowedTasks;
        this.blockedTasks = blockedTasks;
    }

    public ResourceLocation getPhase() {
        return phase;
    }

    public List<ResourceLocation> getAllowedTasks() {
        return allowedTasks;
    }

    public List<ResourceLocation> getBlockedTasks() {
        return blockedTasks;
    }

    /**
     * 检查指定的任务是否被允许
     */
    public boolean isTaskAllowed(ResourceLocation taskId) {
        // 如果允许列表不为空，只允许列表中的任务
        if (!allowedTasks.isEmpty()) {
            return allowedTasks.contains(taskId);
        }
        
        // 如果允许列表为空，检查是否在阻止列表中
        return !blockedTasks.contains(taskId);
    }

    @Override
    public MapCodec<? extends IPhaseContext> codec() {
        return CODEC;
    }
}
