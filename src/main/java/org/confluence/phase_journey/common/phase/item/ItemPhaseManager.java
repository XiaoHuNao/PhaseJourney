package org.confluence.phase_journey.common.phase.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.phase.PhaseType;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class ItemPhaseManager extends PhaseManager<ItemReplacementContext> {
    public static final ItemPhaseManager MANAGER = new ItemPhaseManager();


    private final BiMap<Item, ItemReplacementContext> itemReplacements = HashBiMap.create();

    public void register(ResourceLocation phase, ItemReplacementContext phaseContext) {
        // 保持旧的注册方式，不调用 super.register
        phaseContexts.put(PhaseType.PLAYER, Pair.of(phase, phaseContext));
        itemReplacements.put(phaseContext.getSource(), phaseContext);
    }

    public void applyTargetIfPhaseIsNotAchieved(Player player, Item source, Consumer<Item> targetConsumer) {
        ItemReplacementContext replacement = itemReplacements.get(source);
        if (replacement == null) return;
        // 使用新的数据结构，但保持向后兼容
        for (Map.Entry<PhaseType, Collection<Pair<ResourceLocation, ItemReplacementContext>>> entry : phaseContexts.asMap().entrySet()) {
            for (Pair<ResourceLocation, ItemReplacementContext> pair : entry.getValue()) {
                ResourceLocation phase = pair.getFirst();
                ItemReplacementContext ctx = pair.getSecond();
                if (PhaseUtils.hadPlayerOrLevelAchievedPhase(phase, player)) continue;
                if (ctx.equals(replacement)) {
                    targetConsumer.accept(replacement.getTarget());
                    return;
                }
            }
        }
    }

    public Item replaceSourceIfPhaseIsNotAchieved(Player player, Item source) {
        ItemReplacementContext replacement = itemReplacements.get(source);
        if (replacement == null) return source;
        // 使用新的数据结构，但保持向后兼容
        for (Map.Entry<PhaseType, Collection<Pair<ResourceLocation, ItemReplacementContext>>> entry : phaseContexts.asMap().entrySet()) {
            for (Pair<ResourceLocation, ItemReplacementContext> pair : entry.getValue()) {
                ResourceLocation phase = pair.getFirst();
                ItemReplacementContext ctx = pair.getSecond();
                if (PhaseUtils.hadPlayerOrLevelAchievedPhase(phase, player)) continue;
                if (ctx.equals(replacement)) {
                    return replacement.getTarget();
                }
            }
        }
        return source;
    }

    public Item getReplacedItem(Item source) {
        ItemReplacementContext replacement = itemReplacements.get(source);
        if (replacement == null) return source;
        return replacement.getTarget();
    }

    public boolean hasReplacedItem(Item source) {
        return itemReplacements.containsKey(source);
    }

    // 统一的阶段限制逻辑 - 新增方法，保持向后兼容
    public boolean isRestricted(Level level, BlockPos pos, ServerPlayer player, Item item) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, ItemReplacementContext>> entry : phaseContexts.entries()) {
            PhaseType phaseType = entry.getKey();
            ResourceLocation phase = entry.getValue().getFirst();
            ItemReplacementContext ctx = entry.getValue().getSecond();

            if (!ctx.getSupportedPhaseTypes().contains(phaseType)) {
                continue;
            }

            if (!item.equals(ctx.getSource())) {
                continue;
            }

            PhaseAttachment attachment = phaseType.getPhaseAttachment(level, pos, player);
            if (attachment == null) {
                return false;
            }

            return attachment.ifPhaseAbsent(phase, () -> true);
        }
        return false;
    }

    public boolean isRestricted(Level level, ServerPlayer player, Item item) {
        return isRestricted(level, null, player, item);
    }
}
