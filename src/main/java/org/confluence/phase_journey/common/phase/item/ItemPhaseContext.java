package org.confluence.phase_journey.common.phase.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.confluence.phase_journey.common.phase.PhaseContext;

public class ItemPhaseContext extends PhaseContext {
    public static final MapCodec<ItemPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(ItemPhaseContext::getPhase),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("source").forGetter(ItemPhaseContext::getSource),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("target").forGetter(ItemPhaseContext::getTarget)
    ).apply(instance, ItemPhaseContext::new));

    private final Item source;
    private final Item target;

    public ItemPhaseContext(ResourceLocation phase, Item source, Item target) {
        super(phase);
        this.source = source;
        this.target = target;
    }

    public Item getSource() {
        return source;
    }

    public Item getTarget() {
        return target;
    }

    @Override
    public MapCodec<ItemPhaseContext> codec() {
        return CODEC;
    }
}
