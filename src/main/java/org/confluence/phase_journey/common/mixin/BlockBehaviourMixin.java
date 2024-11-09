package org.confluence.phase_journey.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @ModifyVariable(method = "getDestroyProgress", at = @At("HEAD"), argsOnly = true)
    private BlockState replace(BlockState source, @Local(argsOnly = true) Player player) {
        return BlockPhaseManager.INSTANCE.replaceSourceIfPlayerNotReachedPhase(player, source);
    }
}
