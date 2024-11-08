package org.confluence.phase_journey.common.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.confluence.phase_journey.common.init.ModAttachments;

public class PhaseUtils {
    public static boolean ContainsPhase(ResourceLocation phase, Player player) {
        return player.getData(ModAttachments.PHASE_CAPABILITY.get()).getPhases().contains(phase);
    }

    public static boolean ContainsPhase(ResourceLocation phase, Level level) {
        return level.getData(ModAttachments.PHASE_CAPABILITY.get()).getPhases().contains(phase);
    }

    public static ObjectArrayList<ItemStack> getReplaceBlockLoot(LootParams.Builder params, BlockState blockState) {
        LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, blockState).create(LootContextParamSets.BLOCK);
        ServerLevel serverlevel = lootParams.getLevel();
        LootTable loottable = serverlevel.getServer().reloadableRegistries().getLootTable(blockState.getBlock().getLootTable());
        return loottable.getRandomItems(lootParams);
    }
}
