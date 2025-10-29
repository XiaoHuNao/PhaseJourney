package com.xiaohunao.phase_journey.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.xiaohunao.phase_journey.common.phase.item.ItemPhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @ModifyReturnValue(method = "getTags", at = @At("RETURN"))
    private Stream<TagKey<Item>> replaceState(Stream<TagKey<Item>> original) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            Level level = server.overworld();
            if (level != null) {
                return PhaseUtils.findFirstContextOrReturnDefault(ItemPhaseManager.MANAGER, level, null, null, (ctx, phaseManager) -> ctx.getTarget().builtInRegistryHolder().tags(), original);
            }
        }
        return original;
    }
}
