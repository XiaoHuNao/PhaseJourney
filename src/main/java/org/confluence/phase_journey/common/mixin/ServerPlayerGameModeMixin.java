package org.confluence.phase_journey.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState onUseItemOn(Level level, BlockPos pos,ServerPlayer player){
        AtomicReference<BlockState> blockState = new AtomicReference<>(level.getBlockState(pos));
        BlockPhaseManager.INSTANCE.getBlockPhaseContexts().forEach((phase, blockPhaseContext) -> {
            if (PhaseUtils.ContainsPhase(phase,player) || PhaseUtils.ContainsPhase(phase,level)){
                return;
            }
            if (BlockPhaseManager.INSTANCE.checkReplaceBlock(blockState.get())) {
                blockState.set(blockPhaseContext.getReplaceBlock());
            }
        });

        return blockState.get();
    }

}
