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
import org.confluence.phase_journey.api.IPhaseCapability;

public class PhaseUtils {
    public static boolean ContainsPhase(ResourceLocation phase, Player player) {
        return IPhaseCapability.get(player)
                .orElseThrow(() -> new RuntimeException(player.getName().getString() + " has no phase capability"))
                .getPhases().contains(phase);
    }

    public static boolean ContainsPhase(ResourceLocation phase, Level level) {
        return IPhaseCapability.get(level)
                .orElseThrow(() -> new RuntimeException("Level has no phase capability"))
                .getPhases().contains(phase);
    }

    public static ObjectArrayList<ItemStack> getReplaceBlockLoot(LootParams.Builder params,BlockState blockState) {
        LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, blockState).create(LootContextParamSets.BLOCK);
        ServerLevel serverlevel = lootParams.getLevel();
        LootTable loottable = serverlevel.getServer().getLootData().getLootTable(blockState.getBlock().getLootTable());
        return loottable.getRandomItems(lootParams);
    }
}
