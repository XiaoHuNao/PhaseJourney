package org.confluence.phase_journey.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.BiConsumer;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
    private void denyDestroy(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (BlockPhaseManager.MANAGER.denyDestroy(player, state)) {
            cir.setReturnValue(0.0F);
        }
    }

    @ModifyVariable(method = "getDestroyProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"), argsOnly = true)
    private BlockState replace(BlockState source, @Local(argsOnly = true) Player player) {
        return BlockPhaseManager.MANAGER.replaceSourceIfPlayerNotReachedPhase(player, source);
    }

    @Mixin(BlockBehaviour.BlockStateBase.class)
    public abstract static class BlockStateBaseMixin {
        @WrapOperation(method = "onExplosionHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;onExplosionHit(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;Ljava/util/function/BiConsumer;)V"))
        private void replaceState(Block instance, BlockState blockState, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> biConsumer, Operation<Void> original) {
            BlockState target = BlockPhaseManager.MANAGER.replaceSourceIfLevelNotFinishedPhase(level, blockState);
            if (target == blockState) {
                original.call(instance, blockState, level, pos, explosion, biConsumer);
            } else {
                original.call(target.getBlock(), target, level, pos, explosion, biConsumer);
            }
        }

        @WrapOperation(method = "getDrops", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/storage/loot/LootParams$Builder;)Ljava/util/List;"))
        private List<ItemStack> replaceState(Block instance, BlockState blockState, LootParams.Builder builder, Operation<List<ItemStack>> original) {
            BlockState target = BlockPhaseManager.MANAGER.replaceSourceIfLevelNotFinishedPhase(builder.getLevel(), blockState);
            if (target == blockState) {
                return original.call(instance, blockState, builder);
            } else {
                return original.call(target.getBlock(), target, builder);
            }
        }
    }
}
