package org.confluence.phase_journey.common.phase.growth;

import java.util.Collection;
import java.util.Map;

import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;

public class CropGrowthPhaseManager extends PhaseManager<CropGrowthPhaseContext> {

    public static final CropGrowthPhaseManager MANAGER = new CropGrowthPhaseManager();

    private boolean deny(Level level, BlockState state) {
        if (!(state.getBlock() instanceof CropBlock)) {
            return false;
        }
        for (Map.Entry<ResourceLocation, Collection<CropGrowthPhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadLevelFinishedPhase(entry.getKey(), level)) {
                continue;
            }
            for (CropGrowthPhaseContext ctx : entry.getValue()) {
                if (ctx.disableAll()) {
                    return true;
                }
                for (ResourceKey<Block> bannedBlock : ctx.bannedBlocks()) {
                    if (state.is(bannedBlock)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onCropGrow(CropGrowEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        BlockState state = event.getState();
        if (deny(level, state)) {
            event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
        }
    }
}
