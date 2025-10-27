package com.xiaohunao.phase_journey.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.xiaohunao.phase_journey.common.phase.block.BlockPhaseManager;
import com.xiaohunao.phase_journey.common.phase.block.BlockReplacementPhaseContext;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
    @Shadow
    @Final
    protected ServerPlayer player;

    @WrapOperation(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState onUseItemOn(Level instance, BlockPos blockPos, Operation<BlockState> original) {
        BlockState blockState = original.call(instance, blockPos);

        return PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER,player.level(),player,blockPos,(ctx, phaseManager) -> {
            if (ctx.getSource().equals(blockState)){
                return ctx.getTarget();
            }
            return blockState;
        },blockState);
    }

    @WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState replace(ServerLevel instance, BlockPos blockPos, Operation<BlockState> original) {
        BlockState blockState = original.call(instance, blockPos);

        return PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER,player.level(),player,blockPos,(ctx,phaseManager) -> {
            if (ctx.getSource().equals(blockState)){
                return ctx.getTarget();
            }
            return blockState;
        },blockState);
    }

    @WrapOperation(method = "handleBlockBreakAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState handleBlockBreakAction(ServerLevel instance, BlockPos pos, Operation<BlockState> original) {
        BlockState blockState = original.call(instance, pos);
        return PhaseUtils.findFirstContextOrReturnDefault(BlockPhaseManager.MANAGER,player.level(),player,pos,(ctx,phaseManager) -> {
            if (ctx.getSource().equals(blockState)){
                return ctx.getTarget();
            }
            return blockState;
        },blockState);
    }
}
