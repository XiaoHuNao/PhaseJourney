package org.confluence.phase_journey.common.phase.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.phase.PhaseContext;

/**
 * 维度限制规则类
 * 用于定义玩家对特定维度的访问限制
 */
public class DimensionRestriction extends PhaseContext {
    public static final Codec<DimensionRestriction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(DimensionRestriction::getPhase),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(DimensionRestriction::getDimension),
            Codec.BOOL.fieldOf("allow_enter").orElse(true).forGetter(DimensionRestriction::isEnterAllowed),
            Codec.BOOL.fieldOf("allow_leave").orElse(true).forGetter(DimensionRestriction::isLeaveAllowed),
            Codec.INT.fieldOf("max_visits").orElse(-1).forGetter(DimensionRestriction::getMaxVisits),
            Codec.STRING.fieldOf("enter_message").orElse("").forGetter(DimensionRestriction::getEnterMessage),
            Codec.STRING.fieldOf("leave_message").orElse("").forGetter(DimensionRestriction::getLeaveMessage),
            Codec.STRING.fieldOf("visit_limit_message").orElse("").forGetter(DimensionRestriction::getVisitLimitMessage)
    ).apply(instance, DimensionRestriction::new));

    private final ResourceKey<Level> dimension;
    private final boolean allowEnter;
    private final boolean allowLeave;
    private final int maxVisits; // -1 表示无限制
    private final String enterMessage;
    private final String leaveMessage;
    private final String visitLimitMessage;

    public DimensionRestriction(ResourceLocation phase, ResourceKey<Level> dimension) {
        this(phase, dimension, true, true, -1, "", "", "");
    }

    public DimensionRestriction(ResourceLocation phase, ResourceKey<Level> dimension, 
                               boolean allowEnter, boolean allowLeave, int maxVisits,
                               String enterMessage, String leaveMessage, String visitLimitMessage) {
        super(phase);
        this.dimension = dimension;
        this.allowEnter = allowEnter;
        this.allowLeave = allowLeave;
        this.maxVisits = maxVisits;
        this.enterMessage = enterMessage;
        this.leaveMessage = leaveMessage;
        this.visitLimitMessage = visitLimitMessage;
    }

    /**
     * 获取目标维度
     */
    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    /**
     * 是否允许进入维度
     */
    public boolean isEnterAllowed() {
        return allowEnter;
    }

    /**
     * 是否允许离开维度
     */
    public boolean isLeaveAllowed() {
        return allowLeave;
    }

    /**
     * 获取最大访问次数，-1表示无限制
     */
    public int getMaxVisits() {
        return maxVisits;
    }

    /**
     * 是否有访问次数限制
     */
    public boolean hasVisitLimit() {
        return maxVisits > 0;
    }

    /**
     * 获取进入限制消息
     */
    public String getEnterMessage() {
        return enterMessage;
    }

    /**
     * 获取离开限制消息
     */
    public String getLeaveMessage() {
        return leaveMessage;
    }

    /**
     * 获取访问次数限制消息
     */
    public String getVisitLimitMessage() {
        return visitLimitMessage;
    }

    /**
     * 创建只限制进入的维度限制
     */
    public static DimensionRestriction denyEnter(ResourceLocation phase, ResourceKey<Level> dimension, String message) {
        return new DimensionRestriction(phase, dimension, false, true, -1, message, "", "");
    }

    /**
     * 创建只限制离开的维度限制
     */
    public static DimensionRestriction denyLeave(ResourceLocation phase, ResourceKey<Level> dimension, String message) {
        return new DimensionRestriction(phase, dimension, true, false, -1, "", message, "");
    }

    /**
     * 创建访问次数限制
     */
    public static DimensionRestriction limitVisits(ResourceLocation phase, ResourceKey<Level> dimension, 
                                                  int maxVisits, String message) {
        return new DimensionRestriction(phase, dimension, true, true, maxVisits, "", "", message);
    }

    /**
     * 创建完全禁止访问的维度限制
     */
    public static DimensionRestriction denyAll(ResourceLocation phase, ResourceKey<Level> dimension, 
                                              String enterMessage, String leaveMessage) {
        return new DimensionRestriction(phase, dimension, false, false, -1, enterMessage, leaveMessage, "");
    }
}

