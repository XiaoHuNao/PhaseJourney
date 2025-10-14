package com.xiaohunao.phase_journey.integration.jade;

import com.xiaohunao.phase_journey.common.phase.block.BlockPhaseManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ModJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                Player player = accessor.getPlayer();
                BlockState source = blockAccessor.getBlockState();
                BlockState target = BlockPhaseManager.MANAGER.replaceSourceIfPlayerNotReachedPhase(player, source);
                if (source != target) {
                    return registration.blockAccessor().from(blockAccessor).blockState(target).build();
                }
            }
            return accessor;
        });
    }
}
