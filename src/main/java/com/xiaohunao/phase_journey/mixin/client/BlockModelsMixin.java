package com.xiaohunao.phase_journey.mixin.client;

import com.xiaohunao.phase_journey.common.phase.block.BlockPhaseManager;
import com.xiaohunao.phase_journey.common.phase.block.BlockReplacementPhaseContext;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(BlockModelShaper.class)
public abstract class BlockModelsMixin {
    @Shadow
    private Map<BlockState, BakedModel> modelByStateCache;

    @Shadow
    @Final
    private ModelManager modelManager;

    @Inject(at = @At("HEAD"), method = "getBlockModel", cancellable = true)
    private void getBlockModel(BlockState source, CallbackInfoReturnable<BakedModel> callback) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!BlockPhaseManager.MANAGER.hasReplacement(source)){
            return;
        }

        PhaseUtils.applyActionToMatchingContexts(BlockPhaseManager.MANAGER,player.level(),player,null,(ctx, phaseManager) -> {
            return ctx.getSource().equals(source);
        },(ctx,phaseManager) -> {
            callback.setReturnValue(modelByStateCache.getOrDefault(ctx.getTarget(), modelManager.getMissingModel()));
        });
    }
}