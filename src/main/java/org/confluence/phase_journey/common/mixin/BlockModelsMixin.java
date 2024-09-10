package org.confluence.phase_journey.common.mixin;


import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.MixinUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(BlockModelShaper.class)
public class BlockModelsMixin {
	
	@Shadow
	private Map<BlockState, BakedModel> modelByStateCache;
	
	@Shadow
	@Final
	private ModelManager modelManager;
	
	@Inject(at = @At("HEAD"), method = "getBlockModel", cancellable = true)
	private void getBlockModel(BlockState blockState, CallbackInfoReturnable<BakedModel> callback) {
//		BakedModel overriddenModel = this.modelByStateCache.getOrDefault(Blocks.DIAMOND_BLOCK.defaultBlockState(), modelManager.getMissingModel());
//		callback.setReturnValue(overriddenModel);
//		MixinUtil.getBlockModel(blockState, modelByStateCache, modelManager, callback);
	}
	
}