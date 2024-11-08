package org.confluence.phase_journey.common.init;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.attachment.PhaseCapability;

import java.util.function.Supplier;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, PhaseJourney.MODID);

    public static final Supplier<AttachmentType<PhaseCapability>> PHASE_CAPABILITY = TYPES.register("phase_capability", () -> AttachmentType.serializable(PhaseCapability::new).copyOnDeath().build());
}