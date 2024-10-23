package org.confluence.phase_journey.common.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseContext;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Inject(method = "stillValid(Lnet/minecraft/world/inventory/ContainerLevelAccess;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/Block;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void stillValid(ContainerLevelAccess containerLevelAccess, Player player, final Block block, CallbackInfoReturnable<Boolean> cir) {
        Boolean evaluate = containerLevelAccess.evaluate((level, pos) -> {
            BlockState blockState = null;
            for (Map.Entry<ResourceLocation, BlockPhaseContext> entry : BlockPhaseManager.INSTANCE.getBlockPhaseContexts().entries()) {
                ResourceLocation phase = entry.getKey();
                BlockPhaseContext blockPhaseContext = entry.getValue();
                if (PhaseUtils.ContainsPhase(phase, player) || PhaseUtils.ContainsPhase(phase, level)) {
                    return true;
                }
                if (BlockPhaseManager.INSTANCE.checkReplaceBlock(level.getBlockState(pos))) {
                    blockState = blockPhaseContext.getReplaceBlock();
                }
            }
            return blockState != null && blockState.is(block) && player.canInteractWithBlock(pos, 4.0);
        }, true);
        //取消
        cir.setReturnValue(evaluate);
    }
}
