package org.confluence.phase_journey.common.phase.growth;

import java.util.Map;

import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.PhaseType;

import com.mojang.datafixers.util.Pair;

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

    @Override
    public void register(PhaseType type, ResourceLocation phase, CropGrowthPhaseContext phaseContext) {
        super.register(type, phase, phaseContext);
    }

    public boolean isRestricted(Level level, BlockState state) {
        if (!(state.getBlock() instanceof CropBlock)) {
            return false;
        }
        for (Map.Entry<PhaseType, Pair<ResourceLocation, CropGrowthPhaseContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            CropGrowthPhaseContext phaseContext = entry.getValue().getSecond();
            ResourceLocation phase = phaseContext.getPhase();

            if (!phaseContext.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            PhaseAttachment phaseAttachment = phaseType.getPhaseAttachment(level, null, null);
            if (phaseAttachment == null) {
                return false;
            }

            return phaseAttachment.ifPhaseAbsent(phase, () -> {
                if (phaseContext.disableAll()) {
                    return true;
                }
                for (ResourceKey<Block> bannedBlock : phaseContext.bannedBlocks()) {
                    if (state.is(bannedBlock)) {
                        return true;
                    }
                }
                return false;
            });
        }
        return false;
    }

    @SubscribeEvent
    public void onCropGrow(CropGrowEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        BlockState state = event.getState();
        if (isRestricted(level, state)) {
            event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
        }
    }
}
