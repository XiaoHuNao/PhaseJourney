package com.xiaohunao.phase_journey.integration.create.phase;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.phase_journey.common.phase.PhaseContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public class InfiniteFluidPoolContext extends PhaseContext {

    public static final MapCodec<InfiniteFluidPoolContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(InfiniteFluidPoolContext::getPhase),
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(InfiniteFluidPoolContext::getFluid)
    ).apply(instance, InfiniteFluidPoolContext::new));

    private final Fluid fluid;
     

    public InfiniteFluidPoolContext(ResourceLocation phase, Fluid fluid) {
        super(phase);
        this.fluid = fluid;
    }

    public Fluid getFluid() {
        return fluid;
    }

}
