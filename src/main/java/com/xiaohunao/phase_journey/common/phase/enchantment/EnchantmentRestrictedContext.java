package com.xiaohunao.phase_journey.common.phase.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.phase_journey.common.phase.PhaseContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentRestrictedContext extends PhaseContext {

    public static final MapCodec<EnchantmentRestrictedContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(EnchantmentRestrictedContext::getPhase),
            ResourceKey.codec(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(EnchantmentRestrictedContext::getEnchantment),
            com.mojang.serialization.Codec.BOOL.fieldOf("allow_in_enchantment_table").forGetter(EnchantmentRestrictedContext::isAllowInEnchantmentTable),
            com.mojang.serialization.Codec.BOOL.fieldOf("allow_anvil").forGetter(EnchantmentRestrictedContext::isAllowAnvil)
    ).apply(instance, EnchantmentRestrictedContext::new));

    private final ResourceKey<Enchantment> enchantment;
    private final boolean allowInEnchantmentTable;
    private final boolean allowAnvil;

    public EnchantmentRestrictedContext(ResourceLocation phase, ResourceKey<Enchantment> enchantment, boolean allowInEnchantmentTable, boolean allowAnvil) {
        super(phase);
        this.enchantment = enchantment;
        this.allowInEnchantmentTable = allowInEnchantmentTable;
        this.allowAnvil = allowAnvil;
    }

    public ResourceKey<Enchantment> getEnchantment() {
        return enchantment;
    }

    public boolean isAllowInEnchantmentTable() {
        return allowInEnchantmentTable;
    }

    public boolean isAllowAnvil() {
        return allowAnvil;
    }

    @Override
    public MapCodec<EnchantmentRestrictedContext> codec() {
        return CODEC;
    }
}
