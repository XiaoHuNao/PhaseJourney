package org.confluence.phase_journey.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
    private void denyDestroy(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (BlockPhaseManager.INSTANCE.denyDestroy(player, state)) {
            cir.setReturnValue(0.0F);
        }
    }

    @ModifyVariable(method = "getDestroyProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"), argsOnly = true)
    private BlockState replace(BlockState source, @Local(argsOnly = true) Player player) {
        return BlockPhaseManager.INSTANCE.replaceSourceIfPlayerNotReachedPhase(player, source);
    }
}
