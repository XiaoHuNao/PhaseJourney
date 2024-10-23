package org.confluence.phase_journey.common.mixin;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;
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
	private void getBlockModel(BlockState blockState, CallbackInfoReturnable<BakedModel> callback) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null) {
			BlockPhaseManager.INSTANCE.getBlockPhaseContexts().forEach((phase, blockPhaseContext) -> {
				if (PhaseUtils.ContainsPhase(phase,player) || PhaseUtils.ContainsPhase(phase,player.clientLevel)){
					return;
				}
				if (BlockPhaseManager.INSTANCE.checkReplaceBlock(blockState)) {
					BlockState replaceBlock = blockPhaseContext.getReplaceBlock();
					BakedModel orDefault = modelByStateCache.getOrDefault(replaceBlock, modelManager.getMissingModel());
					callback.setReturnValue(orDefault);
				}
			});
		}
	}
	
}