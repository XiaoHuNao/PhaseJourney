package org.confluence.phase_journey.common.phase.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.confluence.phase_journey.common.phase.PhaseContext;

public class ItemPhaseContext extends PhaseContext {
    public static final Codec<ItemPhaseContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(ItemPhaseContext::getPhase),
            Codec.INT.fieldOf("sourceItem").forGetter(ItemPhaseContext::getSourceItemID),
            Codec.INT.fieldOf("targetItem").forGetter(ItemPhaseContext::getReplaceItemID)
    ).apply(instance, ItemPhaseContext::new));

    private final Item sourceItem;
    private final Item replaceItem;

    public ItemPhaseContext(ResourceLocation phase,int sourceItem, int replaceItem) {
        super(phase);
        this.sourceItem = Item.byId(sourceItem);
        this.replaceItem = Item.byId(replaceItem);
    }

    public ItemPhaseContext(ResourceLocation phase,Item sourceItem, Item replaceItem) {
        super(phase);
        this.sourceItem = sourceItem;
        this.replaceItem = replaceItem;
    }

    public Item getSourceItem(){
        return sourceItem;
    }
    public Item getReplaceItem(){
        return replaceItem;
    }
    public int getSourceItemID(){
        return Item.getId(sourceItem);
    }
    public int getReplaceItemID(){
        return Item.getId(replaceItem);
    }
}
