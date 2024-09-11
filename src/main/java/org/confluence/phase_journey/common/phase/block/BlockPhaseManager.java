package org.confluence.phase_journey.common.phase.block;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.phase.item.ItemPhaseContext;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;

import java.lang.reflect.Field;

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

    public void ReplaceBlockBlockBehaviour(ResourceLocation phase,boolean all) {
        if (all){
            BlockStatePhaseContexts.forEach((phase1, blockPhaseContext) -> {
                ReplaceBlockBlockBehaviour(phase1,false);
            });
        }

        BlockStatePhaseContexts.get(phase).forEach(context -> {
            try {
                Class<?> blockClass = context.getSourceBlock().getBlock().getClass();

                for (Field field : blockClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object replaceValue = field.get(context.getReplaceBlock().getBlock());

                    field.set(context.getSourceBlock(), replaceValue);
                    field.set(context.getReplaceBlock().getBlock(), field.get(context.getSourceBlock().getBlock()));
                }
            } catch (IllegalAccessException e) {
                PhaseJourney.LOGGER.error("Failed to replace block behaviour", e);
            }
        });

    }
}

//    public void ReplaceBlockBlockBehaviour(BlockState sourceBlock, BlockState replaceBlock){
//        sourceBlock.getBlock().hasCollision = replaceBlock.getBlock().hasCollision;
//        sourceBlock.getBlock().explosionResistance = replaceBlock.getBlock().explosionResistance;
//        sourceBlock.getBlock().isRandomlyTicking = replaceBlock.getBlock().isRandomlyTicking;
//        sourceBlock.getBlock().soundType = replaceBlock.getBlock().soundType;
//        sourceBlock.getBlock().friction = replaceBlock.getBlock().friction;
//        sourceBlock.getBlock().speedFactor = replaceBlock.getBlock().speedFactor;
//        sourceBlock.getBlock().jumpFactor = replaceBlock.getBlock().jumpFactor;
//        sourceBlock.getBlock().dynamicShape = replaceBlock.getBlock().dynamicShape;
//        sourceBlock.getBlock().requiredFeatures = replaceBlock.getBlock().requiredFeatures;
//        sourceBlock.getBlock().properties = replaceBlock.getBlock().properties;
//        sourceBlock.getBlock().drops = replaceBlock.getBlock().drops;
//        sourceBlock.getBlock().lootTableSupplier = replaceBlock.getBlock().lootTableSupplier;
//    }
//}
