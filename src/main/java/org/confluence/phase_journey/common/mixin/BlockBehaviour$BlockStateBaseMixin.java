package org.confluence.phase_journey.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviour$BlockStateBaseMixin {
    @Shadow
    protected abstract BlockState asState();

    @Inject(method = "onExplosionHit", at = @At("HEAD"), cancellable = true)
    private void replaceState(Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer, CallbackInfo ci) {
        BlockPhaseManager.INSTANCE.applyTargetIfLevelNotFinishedPhase(level, asState(), target -> {
            ((BlockBehaviourAccessor) target.getBlock()).callOnExplosionHit(target, level, pos, explosion, dropConsumer);
            ci.cancel();
        });
    }
}
