package com.xiaohunao.phase_journey.common.phase.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.phase_journey.common.phase.PhaseContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;


public class DimensionTravelRestrictedContext extends PhaseContext {
    public static final MapCodec<DimensionTravelRestrictedContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(DimensionTravelRestrictedContext::getPhase),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().optionalFieldOf("entity_type",EntityType.PLAYER).forGetter(DimensionTravelRestrictedContext::getEntityType),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(DimensionTravelRestrictedContext::getDimension),
            Codec.BOOL.optionalFieldOf("allow_enter",true).forGetter(DimensionTravelRestrictedContext::isEnterAllowed),
            Codec.BOOL.optionalFieldOf("allow_leave",true).forGetter(DimensionTravelRestrictedContext::isLeaveAllowed),
            Codec.INT.optionalFieldOf("max_enter_visits",-1).forGetter(DimensionTravelRestrictedContext::getMaxEnterVisits),
            Codec.INT.optionalFieldOf("max_leave_visits",-1).forGetter(DimensionTravelRestrictedContext::getMaxLeaveVisits)
    ).apply(instance, DimensionTravelRestrictedContext::new));

    private final EntityType<?> entityType;
    private final ResourceKey<Level> dimension;
    private final boolean allowEnter;
    private final boolean allowLeave;
    private final int maxEnterVisits; // -1 表示无限制
    private final int maxLeaveVisits;

    public DimensionTravelRestrictedContext(ResourceLocation phase, ResourceKey<Level> dimension) {
        this(phase,EntityType.PLAYER, dimension, false, true, -1,-1);
    }

    public DimensionTravelRestrictedContext(ResourceLocation phase, EntityType<?> entityType, ResourceKey<Level> dimension, boolean allowEnter, boolean allowLeave, int maxEnterVisits, int maxLeaveVisits) {
        super(phase);
        this.entityType = entityType;
        this.dimension = dimension;
        this.allowEnter = allowEnter;
        this.allowLeave = allowLeave;
        this.maxEnterVisits = maxEnterVisits;
        this.maxLeaveVisits = maxLeaveVisits;
    }

    /**
     * 获取限制的实体类型
     */
    public EntityType<?> getEntityType() {
        return entityType;
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
     * 获取最大进入访问次数，-1表示无限制
     */
    public int getMaxEnterVisits() {
        return maxEnterVisits;
    }

    /**
     * 获取最大离开访问次数，-1表示无限制
     */
    public int getMaxLeaveVisits() {
        return maxLeaveVisits;
    }

    /**
     * 创建只限制进入的维度限制
     */
    public static DimensionTravelRestrictedContext denyEnter(ResourceLocation phase, EntityType<?> entityType, ResourceKey<Level> dimension) {
        return new DimensionTravelRestrictedContext(phase, entityType, dimension, false, true, -1,-1);
    }

    /**
     * 创建只限制离开的维度限制
     */
    public static DimensionTravelRestrictedContext denyLeave(ResourceLocation phase, EntityType<?> entityType, ResourceKey<Level> dimension) {
        return new DimensionTravelRestrictedContext(phase, entityType , dimension, true, false, -1,-1);
    }

    /**
     * 创建访问次数限制
     */
    public static DimensionTravelRestrictedContext limitVisits(ResourceLocation phase, EntityType<?> entityType, ResourceKey<Level> dimension,
                                                               int maxEnterVisits, int maxLeaveVisits) {
        return new DimensionTravelRestrictedContext(phase, entityType , dimension, true, true, maxEnterVisits, maxLeaveVisits);
    }

    /**
     * 创建只限制进入的维度限制
     */
    public static DimensionTravelRestrictedContext denyEnter(ResourceLocation phase, ResourceKey<Level> dimension) {
        return new DimensionTravelRestrictedContext(phase, EntityType.PLAYER, dimension, false, true, -1,-1);
    }

    /**
     * 创建只限制离开的维度限制
     */
    public static DimensionTravelRestrictedContext denyLeave(ResourceLocation phase, ResourceKey<Level> dimension) {
        return new DimensionTravelRestrictedContext(phase, EntityType.PLAYER , dimension, true, false, -1,-1);
    }

    /**
     * 创建访问次数限制
     */
    public static DimensionTravelRestrictedContext limitVisits(ResourceLocation phase, ResourceKey<Level> dimension,
                                                               int maxEnterVisits, int maxLeaveVisits) {
        return new DimensionTravelRestrictedContext(phase, EntityType.PLAYER , dimension, true, true, maxEnterVisits, maxLeaveVisits);
    }


    @Override
    public MapCodec<DimensionTravelRestrictedContext> codec() {
        return CODEC;
    }
}

