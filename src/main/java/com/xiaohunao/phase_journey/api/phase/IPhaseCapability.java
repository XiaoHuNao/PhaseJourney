package com.xiaohunao.phase_journey.api.phase;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.function.Supplier;

public interface IPhaseCapability {
    /**
     * 获取所有已解锁的阶段集合
     * @return 包含所有阶段ResourceLocation的集合
     */
    Set<ResourceLocation> getPhases();

    /**
     * 添加一个新的阶段
     * @param phase 要添加的阶段标识符
     */
    void addPhase(ResourceLocation phase);

    /**
     * 移除一个已存在的阶段
     * @param phase 要移除的阶段标识符
     */
    void removePhase(ResourceLocation phase);

    /**
     * 检查特定阶段是否已解锁，如果已解锁则执行提供的操作
     * @param phase 要检查的阶段标识符
     * @param action 如果阶段已解锁要执行的操作
     * @return 如果阶段已解锁并执行了操作则返回true，否则返回false
     */
    default<T> T ifPhasePresent(ResourceLocation phase, Supplier<T> action) {
        if (getPhases().contains(phase)) {
            return action.get();
        }
        return null;
    }

    /**
     * 检查特定阶段是否已解锁，如果未解锁则执行提供的操作
     * @param phase 要检查的阶段标识符
     * @param action 如果阶段未解锁要执行的操作
     * @param defaultValue 如果阶段未解锁则返回的默认值
     * @return 如果阶段已解锁并执行了操作则返回true，否则返回false
     */
    default<T> T ifPhaseAbsent(ResourceLocation phase, Supplier<T> action,T defaultValue) {
        if (getPhases().contains(phase)) {
            return defaultValue;
        }
        return action.get();
    }
}
