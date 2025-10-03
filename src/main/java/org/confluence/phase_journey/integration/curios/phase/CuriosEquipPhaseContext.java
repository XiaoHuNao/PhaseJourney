package org.confluence.phase_journey.integration.curios.phase;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.confluence.phase_journey.common.phase.PhaseContext;

import java.util.List;

public class CuriosEquipPhaseContext extends PhaseContext {

    public static final MapCodec<CuriosEquipPhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(CuriosEquipPhaseContext::phase),
            Codec.STRING.listOf().fieldOf("banned_slots").forGetter(CuriosEquipPhaseContext::bannedSlots)
    ).apply(instance, CuriosEquipPhaseContext::new));

    private final List<String> bannedSlots;

    public CuriosEquipPhaseContext(ResourceLocation phase, List<String> bannedSlots) {
        super(phase);
        this.bannedSlots = bannedSlots;
    }

    public ResourceLocation phase() {
        return phase;
    }

    public List<String> bannedSlots() {
        return bannedSlots;
    }
}
