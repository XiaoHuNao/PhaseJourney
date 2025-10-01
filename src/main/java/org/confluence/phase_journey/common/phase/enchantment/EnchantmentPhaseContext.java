package org.confluence.phase_journey.common.phase.enchantment;

import org.confluence.phase_journey.common.phase.PhaseContext;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentPhaseContext extends PhaseContext {

    public static final MapCodec<EnchantmentPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(EnchantmentPhaseContext::getPhase),
            ResourceKey.codec(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(EnchantmentPhaseContext::getEnchantment),
            com.mojang.serialization.Codec.BOOL.fieldOf("allow_in_enchantment_table").forGetter(EnchantmentPhaseContext::isAllowInEnchantmentTable),
            com.mojang.serialization.Codec.BOOL.fieldOf("allow_anvil").forGetter(EnchantmentPhaseContext::isAllowAnvil)
    ).apply(instance, EnchantmentPhaseContext::new));

    private final ResourceKey<Enchantment> enchantment;
    private final boolean allowInEnchantmentTable;
    private final boolean allowAnvil;

    public EnchantmentPhaseContext(ResourceLocation phase, ResourceKey<Enchantment> enchantment, boolean allowInEnchantmentTable, boolean allowAnvil) {
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
    public MapCodec<EnchantmentPhaseContext> codec() {
        return CODEC;
    }
}
