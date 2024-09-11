package org.confluence.phase_journey.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(method = "getDrops",
            at = @At(value = "RETURN"),
            cancellable = true)
    private void getDrops(BlockState blockState, LootParams.Builder params, CallbackInfoReturnable<List<ItemStack>> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            BlockPhaseManager.INSTANCE.getBlockPhaseContexts().forEach((phase, blockPhaseContext) -> {
                if (PhaseUtils.ContainsPhase(phase,player) || PhaseUtils.ContainsPhase(phase,player.clientLevel)){
                    return;
                }
                if (BlockPhaseManager.INSTANCE.checkReplaceBlock(blockState)) {
                    BlockState replaceBlock = blockPhaseContext.getReplaceBlock();
                    cir.setReturnValue(PhaseUtils.getReplaceBlockLoot(params,replaceBlock));
                }
            });
        }
    }
}