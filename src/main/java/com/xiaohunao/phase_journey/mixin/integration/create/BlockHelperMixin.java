package com.xiaohunao.phase_journey.mixin.integration.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.xiaohunao.phase_journey.common.phase.block.BlockPhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "com.simibubi.create.foundation.utility.BlockHelper", remap = false)
public abstract class BlockHelperMixin {
    @WrapOperation(method = "destroyBlockAs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private static BlockState replace(Level instance, BlockPos pos, Operation<BlockState> original) {
        BlockState blockState = original.call(instance, pos);

        return PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER,instance,null,pos,(ctx,phaseManager) -> {
            if (ctx.getSource().equals(blockState)){
                return ctx.getTarget();
            }
            return blockState;
        },blockState);
    }
}
