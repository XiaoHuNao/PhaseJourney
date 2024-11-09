package org.confluence.phase_journey.common.phase.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.confluence.phase_journey.common.phase.PhaseContext;

public class ItemReplacement extends PhaseContext {
    public static final Codec<ItemReplacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(ItemReplacement::getPhase),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("source").forGetter(ItemReplacement::getSource),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("target").forGetter(ItemReplacement::getTarget)
    ).apply(instance, ItemReplacement::new));

    private final Item source;
    private final Item target;

    public ItemReplacement(ResourceLocation phase, Item source, Item target) {
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
}
