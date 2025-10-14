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

    /**
     * 检查特定阶段是否未解锁，如果未解锁则添加它
     * @param phase 要检查和可能添加的阶段标识符
     * @return 如果阶段被添加则返回true，如果已经解锁则返回false
     */
    default boolean addPhaseIfAbsent(ResourceLocation phase) {
        if (getPhases().contains(phase)) {
            return false;
        }
        addPhase(phase);
        return true;
    }

    /**
     * 检查特定阶段是否存在，如果存在则移除它
     * @param phase 要检查和可能移除的阶段标识符
     * @return 如果阶段被移除则返回true，如果不存在则返回false
     */
    default boolean removePhaseIfPresent(ResourceLocation phase) {
        if (!getPhases().contains(phase)) {
            return false;
        }
        removePhase(phase);
        return true;
    }
}
