package org.confluence.phase_journey.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.block.BlockPhaseContext;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

public class MixinUtil {
    public static void getBlockModel(BlockState blockState, Map<BlockState, BakedModel> modelByStateCache, ModelManager modelManager, CallbackInfoReturnable<BakedModel> callback) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ListTag phase_Journeu = player.getPersistentData().getList("phase_journeu", Tag.TAG_STRING);
            BlockPhaseContext phaseContext = PhaseManager.isReplaceBlock(blockState);
            if (phaseContext != null && phase_Journeu.contains(phaseContext.getPhaseName())){
                BlockState replaceBlock = phaseContext.getReplaceBlock();
                BakedModel orDefault = modelByStateCache.getOrDefault(replaceBlock, modelManager.getMissingModel());
                callback.setReturnValue(orDefault);
            }
        }
    }
}
