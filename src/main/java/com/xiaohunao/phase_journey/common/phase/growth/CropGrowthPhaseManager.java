package com.xiaohunao.phase_journey.common.phase.growth;

import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;

public class CropGrowthPhaseManager extends PhaseManager<CropGrowthInhibitionContext> {

    public static final CropGrowthPhaseManager MANAGER = new CropGrowthPhaseManager();

    @Override
    public void register(PhaseType type, ResourceLocation phase, CropGrowthInhibitionContext phaseContext) {
        super.register(type, phase, phaseContext);
    }

    public boolean isRestricted(Level level,BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof CropBlock)) {
            return false;
        }

        return isRestricted(level,pos,null,ctx ->{
            if (ctx.disableAll()) {
                return true;
            }
            for (ResourceKey<Block> bannedBlock : ctx.bannedBlocks()) {
                if (state.is(bannedBlock)) {
                    return true;
                }
            }
            return false;
        });
    }

    @SubscribeEvent
    public void onCropGrow(CropGrowEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        BlockState state = event.getState();
        BlockPos pos = event.getPos();
        if (isRestricted(level,pos, state)) {
            event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
        }
    }
}
