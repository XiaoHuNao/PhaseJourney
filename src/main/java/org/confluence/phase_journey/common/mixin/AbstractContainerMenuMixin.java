package org.confluence.phase_journey.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @WrapOperation(method = "lambda$stillValid$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private static BlockState replace(Level instance, BlockPos blockPos, Operation<BlockState> original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) Block block) {
        return BlockPhaseManager.INSTANCE.replaceSourceIfPhaseIsNotAchieved(player, original.call(instance, blockPos));
    }
}
