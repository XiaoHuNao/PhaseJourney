package org.confluence.phase_journey.common.phase.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.confluence.phase_journey.common.phase.PhaseContext;

public class ItemReplacementContext extends PhaseContext {
    public static final MapCodec<ItemReplacementContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(ItemReplacementContext::getPhase),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("source").forGetter(ItemReplacementContext::getSource),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("target").forGetter(ItemReplacementContext::getTarget)
    ).apply(instance, ItemReplacementContext::new));

    private final Item source;
    private final Item target;

    public ItemReplacementContext(ResourceLocation phase, Item source, Item target) {
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
    public MapCodec<ItemReplacementContext> codec() {
        return CODEC;
    }
}
