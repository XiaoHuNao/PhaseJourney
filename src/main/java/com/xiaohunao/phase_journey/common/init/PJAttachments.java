package com.xiaohunao.phase_journey.common.init;

import com.xiaohunao.phase_journey.PhaseJourney;
import com.xiaohunao.phase_journey.common.attachment.BlockOwnerAttachment;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class PJAttachments {
    public static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, PhaseJourney.MODID);

    public static final Supplier<AttachmentType<PhaseAttachment>> PHASE = TYPES.register("phase", () -> AttachmentType.serializable(PhaseAttachment::new).copyOnDeath().build());

    public static final Supplier<AttachmentType<BlockOwnerAttachment>> BLOCK_OWNER_PLAYER = TYPES.register("block_owner_player", () -> AttachmentType.serializable(BlockOwnerAttachment::new).build());
}