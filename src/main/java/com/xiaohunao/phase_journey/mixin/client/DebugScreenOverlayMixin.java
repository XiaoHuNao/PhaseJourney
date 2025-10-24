package com.xiaohunao.phase_journey.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.xiaohunao.phase_journey.common.phase.block.BlockPhaseManager;
import com.xiaohunao.phase_journey.common.phase.block.BlockReplacementPhaseContext;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyExpressionValue(method = "getSystemInformation", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState wrap(BlockState original) {
        return PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER,minecraft.player.level(),minecraft.player,null,(ctx, phaseManager) -> {
            return ctx.getSource().equals(original) ? ctx.getTarget() : original;
        }, original);
    }
}
