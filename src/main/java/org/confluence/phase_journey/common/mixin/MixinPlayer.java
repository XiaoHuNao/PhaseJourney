package org.confluence.phase_journey.common.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @Shadow
    public abstract boolean hasCorrectToolForDrops(BlockState arg);

    @Shadow @Final private Inventory inventory;

    @Inject(method = "hasCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    public void onHasCorrectToolForDrops(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        AtomicReference<BlockState> blockState1 = new AtomicReference<>();
        BlockPhaseManager.INSTANCE.getBlockPhaseContexts().forEach((phase, blockPhaseContext) -> {
            if (PhaseUtils.ContainsPhase(phase,player) || PhaseUtils.ContainsPhase(phase,player.level())){
                blockState1.set(blockState);
            }
            if (BlockPhaseManager.INSTANCE.checkReplaceBlock(blockState)) {
                blockState1.set(blockPhaseContext.getReplaceBlock());
            }
        });
        cir.setReturnValue(ForgeEventFactory.doPlayerHarvestCheck(player, blockState1.get(), !blockState1.get().requiresCorrectToolForDrops() || this.inventory.getSelected().isCorrectToolForDrops(blockState1.get())));
    }
}