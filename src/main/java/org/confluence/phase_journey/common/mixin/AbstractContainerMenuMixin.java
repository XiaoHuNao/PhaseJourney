package org.confluence.phase_journey.common.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @Inject(method = "stillValid(Lnet/minecraft/world/inventory/ContainerLevelAccess;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/Block;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void stillValid(ContainerLevelAccess containerLevelAccess, Player player,final Block block, CallbackInfoReturnable<Boolean> cir) {
        AtomicReference<BlockState> block1 = new AtomicReference<>();
        Boolean evaluate = containerLevelAccess.evaluate((level, pos) -> {
            BlockPhaseManager.INSTANCE.getBlockPhaseContexts().forEach((phase, blockPhaseContext) -> {
                if (PhaseUtils.ContainsPhase(phase, player) || PhaseUtils.ContainsPhase(phase, level)) {
                    block1.set(level.getBlockState(pos));
                }
                if (BlockPhaseManager.INSTANCE.checkReplaceBlock(level.getBlockState(pos))) {
                    block1.set(blockPhaseContext.getReplaceBlock());
                }
            });
            return !block1.get().is(block) ? false : player.distanceToSqr(player.getX() + 0.5, player.getY() + 0.5, player.getZ() + 0.5) <= 64.0;
        }, true);
        //取消
        cir.setReturnValue(evaluate);
    }
}
