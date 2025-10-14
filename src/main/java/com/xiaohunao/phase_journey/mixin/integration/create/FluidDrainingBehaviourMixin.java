package com.xiaohunao.phase_journey.mixin.integration.create;

import com.simibubi.create.content.fluids.transfer.FluidDrainingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.xiaohunao.phase_journey.integration.create.phase.InfiniteFluidPoolManager;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = FluidDrainingBehaviour.class, remap = false)
public class FluidDrainingBehaviourMixin {
    @Redirect(method = "continueSearch", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/transfer/FluidDrainingBehaviour;canDrainInfinitely(Lnet/minecraft/world/level/material/Fluid;)Z"))
    private boolean redirectCanDrainInfinitely(FluidDrainingBehaviour instance, Fluid fluid) {
        SmartBlockEntity blockEntity = instance.blockEntity;
        if (InfiniteFluidPoolManager.MANAGER.isRestricted(blockEntity.getLevel(), blockEntity.getBlockPos(), null, fluid)){
            return false;
        }
        if (fluid == null)
            return false;
        return AllConfigs.server().fluids.hosePulleyBlockThreshold.get() != -1 && AllConfigs.server().fluids.bottomlessFluidMode.get()
                .test(fluid);
    }
}
