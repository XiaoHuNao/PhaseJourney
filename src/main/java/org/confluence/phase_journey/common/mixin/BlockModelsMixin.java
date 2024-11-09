package org.confluence.phase_journey.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
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
        BlockPhaseManager.INSTANCE.applyTargetIfPhaseIsNotAchieved(player, source, target -> {
            callback.setReturnValue(modelByStateCache.getOrDefault(target, modelManager.getMissingModel()));
        });
    }
}