package com.xiaohunao.phase_journey.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.xiaohunao.phase_journey.common.phase.block.BlockPhaseManager;
import com.xiaohunao.phase_journey.common.phase.block.BlockReplacementPhaseContext;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @WrapOperation(method = "lambda$stillValid$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private static BlockState replace(Level instance, BlockPos blockPos, Operation<BlockState> original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) Block block) {
        BlockState blockState = original.call(instance, blockPos);

        return PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER,player.level(),player,blockPos,(ctx,phaseManager) -> {
            if (ctx.getSource().equals(blockState)){
                return ctx.getTarget();
            }
            return blockState;
        },blockState);
    }
}
