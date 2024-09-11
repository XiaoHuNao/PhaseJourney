package org.confluence.phase_journey.common.phase.block;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.common.phase.item.ItemPhaseContext;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;

public class BlockPhaseManager {
    public static final BlockPhaseManager INSTANCE = new BlockPhaseManager();

    private  final Multimap<ResourceLocation, BlockPhaseContext> BlockStatePhaseContexts = ArrayListMultimap.create();
    private  final BiMap<BlockState,BlockPhaseContext> sourceBlockPhaseContexts = HashBiMap.create();
    public Multimap<ResourceLocation, BlockPhaseContext> getBlockPhaseContexts() {
        return BlockStatePhaseContexts;
    }

    public void registerBlockPhase(ResourceLocation phase, BlockPhaseContext blockPhaseContext) {
        BlockStatePhaseContexts.put(phase,blockPhaseContext);
        sourceBlockPhaseContexts.put(blockPhaseContext.getSourceBlock(),blockPhaseContext);
        registerBlockItemPhaseContext(phase,blockPhaseContext);
    }
    public boolean checkReplaceBlock(BlockState blockState){
        return sourceBlockPhaseContexts.containsKey(blockState);
    }


    public void registerBlockItemPhaseContext(ResourceLocation phase, BlockPhaseContext blockPhaseContext){
        Item sourceItem = blockPhaseContext.getSourceBlock().getBlock().asItem();
        Item replaceItem = blockPhaseContext.getReplaceBlock().getBlock().asItem();
        ItemPhaseContext itemPhaseContext = new ItemPhaseContext(phase, sourceItem, replaceItem);
        ItemPhaseManager.INSTANCE.registerBlockPhase(phase, itemPhaseContext);
    }

}
