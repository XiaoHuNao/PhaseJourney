package com.xiaohunao.phase_journey.integration.immersiveengineering.phase;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;

public class IEMultiblockPhaseManager extends PhaseManager<IEMultiblockPhaseContext> {

    public static final IEMultiblockPhaseManager MANAGER = new IEMultiblockPhaseManager();

    public boolean isRestricted(Level level,BlockPos pos,Player player, ResourceLocation multiblockId) {
        return isRestricted(level,pos,player, ctx -> {
            if (ctx.disableAll()) {
                return true;
            }
            return ctx.bannedMultiblocks().contains(multiblockId);
        });
    }

    @SubscribeEvent
    public void onMultiblockForm(MultiblockHandler.MultiblockFormEvent event) {
        BlockPos clickedBlock = event.getClickedBlock();
        Player player = event.getEntity();
        ResourceLocation id = event.getMultiblock().getUniqueName();
        if (isRestricted(player.level(),clickedBlock, player, id)) {
            event.setCanceled(true);
        }
    }
}
