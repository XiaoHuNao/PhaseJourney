package com.xiaohunao.phase_journey.mixin;

import com.xiaohunao.phase_journey.common.phase.item.ItemPhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTags", at = @At("RETURN"), cancellable = true)
    private void replaceState(CallbackInfoReturnable<Stream<TagKey<Item>>> cir) {
        Level level = ServerLifecycleHooks.getCurrentServer().overworld();
        Stream<TagKey<Item>> firstContextOrReturnDefault = PhaseUtils.findFirstContextOrReturnDefault(ItemPhaseManager.MANAGER, level, null, null, (ctx, phaseManager) -> {
            return ctx.getTarget().builtInRegistryHolder().tags();
        }, cir.getReturnValue());
        cir.setReturnValue(firstContextOrReturnDefault);
    }
}
