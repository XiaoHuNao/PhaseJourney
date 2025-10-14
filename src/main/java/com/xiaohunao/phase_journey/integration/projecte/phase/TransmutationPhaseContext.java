package com.xiaohunao.phase_journey.integration.projecte.phase;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.phase_journey.common.phase.PhaseContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;

public class TransmutationPhaseContext extends PhaseContext {
    public static final MapCodec<TransmutationPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(TransmutationPhaseContext::phase),
            Codec.BOOL.fieldOf("disable_all_items").forGetter(TransmutationPhaseContext::disableAllItems),
            BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("banned_items").forGetter(TransmutationPhaseContext::bannedItems),
            Codec.BOOL.fieldOf("allow_emc_gain_but_no_learn").forGetter(TransmutationPhaseContext::allowEmcGainButNoLearn)
    ).apply(instance, TransmutationPhaseContext::new));


    private final boolean disableAllItems;
    private final List<Item> bannedItems;
    private final boolean allowEmcGainButNoLearn;

    public TransmutationPhaseContext(ResourceLocation phase, boolean disableAllItems, List<Item> bannedItems, boolean allowEmcGainButNoLearn) {
        super(phase);
        this.disableAllItems = disableAllItems;
        this.bannedItems = bannedItems;
        this.allowEmcGainButNoLearn = allowEmcGainButNoLearn;
    }


    public ResourceLocation phase() {
        return phase;
    }

    public boolean disableAllItems() {
        return disableAllItems;
    }

    public List<Item> bannedItems() {
        return bannedItems;
    }

    public boolean allowEmcGainButNoLearn() {
        return allowEmcGainButNoLearn;
    }
}


