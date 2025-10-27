package com.xiaohunao.phase_journey.mixin.client;

import com.xiaohunao.phase_journey.common.phase.item.ItemPhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import net.neoforged.neoforge.common.util.AttributeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ClientItemStackMixin {

    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    public Item getTooltipLines(ItemStack itemStack) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null){
            return itemStack.getItem();
        }

        if (!ItemPhaseManager.MANAGER.hasReplacedItem(itemStack.getItem())){
            return itemStack.getItem();
        }

        return PhaseUtils.findFirstContextOrReturnDefault(ItemPhaseManager.MANAGER,player.level(),player,null,(ctx, manager) -> {
            if (ctx.getSource().equals(itemStack.getItem())){
                return ctx.getTarget();
            }
            return ctx.getSource();
        }, itemStack.getItem());
    }

    @Inject(method = "addToTooltip", at = @At(value = "HEAD"), cancellable = true)
    public <T extends TooltipProvider> void addToTooltip(DataComponentType<T> component, Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack itemStack = (ItemStack) (Object) this;
        if (player == null) return;

        ItemStack replacement = PhaseUtils.findFirstContextOrReturnDefault(ItemPhaseManager.MANAGER,player.level(),player,null,(ctx, manager) -> {
            if (ctx.getSource().equals(itemStack.getItem())){
                return ctx.getTarget().getDefaultInstance();
            }
            return itemStack;
        }, itemStack);


        T value = replacement.get(component);

        if (value != null) {
            value.addToTooltip(context, tooltipAdder, tooltipFlag);
        }
        ci.cancel();
    }

    @Redirect(method = "getTooltipLines",at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/AttributeUtil;addAttributeTooltips(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;Lnet/neoforged/neoforge/common/util/AttributeTooltipContext;)V"))
    public void getTooltipLines(ItemStack stack, Consumer<Component> tooltip, AttributeTooltipContext attributeTooltipContext) {
        ItemStack replacement = PhaseUtils.findFirstContextOrReturnDefault(ItemPhaseManager.MANAGER,attributeTooltipContext.level(),attributeTooltipContext.player(),null,(ctx, manager) -> {
            if (ctx.getSource().equals(stack.getItem())){
                return ctx.getTarget().getDefaultInstance();
            }
            return stack;
        }, stack);

        ItemAttributeModifiers modifiers = replacement.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        if (modifiers.showInTooltip()) {
            AttributeUtil.applyModifierTooltips(replacement, tooltip, attributeTooltipContext);
        }

        NeoForge.EVENT_BUS.post(new AddAttributeTooltipsEvent(replacement, tooltip, attributeTooltipContext));
    }


}
