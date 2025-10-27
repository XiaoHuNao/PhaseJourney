package com.xiaohunao.phase_journey.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.xiaohunao.phase_journey.common.phase.block.BlockPhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
    private void denyDestroy(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (BlockPhaseManager.MANAGER.denyDestroy(player, state)) {
            cir.setReturnValue(0.0F);
        }
    }

    @Mixin(BlockBehaviour.BlockStateBase.class)
    public abstract static class BlockStateBaseMixin {
        @ModifyArg(method = "getDestroyProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDestroyProgress(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
        private BlockState replaceBlockStateForDestroyProgress(BlockState original, @Local(argsOnly = true) Player player) {
            if (player != null && player.level() instanceof Level level) {
                return PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER, level, player, null, (ctx, phaseManager) -> {
                    if (ctx.getSource().equals(original)) {
                        return ctx.getTarget();
                    }
                    return original;
                }, original);
            }
            return original;
        }

        @WrapOperation(method = "onExplosionHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;onExplosionHit(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;Ljava/util/function/BiConsumer;)V"))
        private void replaceState(Block instance, BlockState blockState, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> biConsumer, Operation<Void> original) {
            BlockState target = PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER,level,null,pos,(ctx,phaseManager) -> {
                if (ctx.getSource().equals(blockState)){
                    return ctx.getTarget();
                }
                return blockState;
            },blockState);

            if (target == blockState) {
                original.call(instance, blockState, level, pos, explosion, biConsumer);
            } else {
                original.call(target.getBlock(), target, level, pos, explosion, biConsumer);
            }
        }

        @WrapOperation(method = "getDrops", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/storage/loot/LootParams$Builder;)Ljava/util/List;"))
        private List<ItemStack> replaceState(Block instance, BlockState blockState, LootParams.Builder builder, Operation<List<ItemStack>> original) {
            BlockState target =PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER,builder.getLevel(),null,null,(ctx,phaseManager) -> {
                if (ctx.getSource().equals(blockState)){
                    return ctx.getTarget();
                }
                return blockState;
            },blockState);


            if (target == blockState) {
                return original.call(instance, blockState, builder);
            } else {
                return original.call(target.getBlock(), target, builder);
            }
        }

        @Inject(method = "getTags" , at = @At("RETURN"), cancellable = true)
        private void replaceState(CallbackInfoReturnable<Stream<TagKey<Block>>> cir) {
            Level level = ServerLifecycleHooks.getCurrentServer().overworld();
            Stream<TagKey<Block>> firstContextOrReturnDefault = PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER, level, null, null, (ctx, phaseManager) -> {
                return ctx.getTarget().getBlock().builtInRegistryHolder().tags();
            }, cir.getReturnValue());
            cir.setReturnValue(firstContextOrReturnDefault);
        }

    }
}
