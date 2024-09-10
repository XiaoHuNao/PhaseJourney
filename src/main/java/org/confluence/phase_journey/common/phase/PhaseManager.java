package org.confluence.phase_journey.common.phase;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.block.BlockPhaseContext;

import java.util.Collection;
import java.util.List;


public class PhaseManager {
    private static final Multimap<ResourceLocation, BlockPhaseContext> blockPhaseContexts = ArrayListMultimap.create();
    private static final BiMap<BlockState,BlockPhaseContext> blockStatePhaseContexts = HashBiMap.create();

    public static void registerBlockPhase(ResourceLocation phase, BlockPhaseContext blockPhaseContext) {
        blockPhaseContexts.put(phase,blockPhaseContext);
        blockStatePhaseContexts.put(blockPhaseContext.getSourceBlock(),blockPhaseContext);
    }
    public static Collection<BlockPhaseContext> getBlockPhaseContexts(ResourceLocation phase){
        return blockPhaseContexts.get(phase);
    }
    public static BlockPhaseContext isReplaceBlock(BlockState blockState){
        return blockStatePhaseContexts.getOrDefault(blockState,null);
    }
}
