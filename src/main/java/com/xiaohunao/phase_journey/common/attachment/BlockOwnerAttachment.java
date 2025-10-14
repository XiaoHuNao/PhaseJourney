package com.xiaohunao.phase_journey.common.attachment;

import com.xiaohunao.phase_journey.common.init.PJAttachments;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

public class BlockOwnerAttachment implements INBTSerializable<CompoundTag> {
    public UUID ownerPlayer;

    public static BlockOwnerAttachment of(BlockEntity blockEntity) {
        return blockEntity.getData(PJAttachments.BLOCK_OWNER_PLAYER);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        if (ownerPlayer != null) {
            nbt.putUUID("ownerPlayer", ownerPlayer);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        if (compoundTag.contains("ownerPlayer")){
            ownerPlayer = compoundTag.getUUID("ownerPlayer");
        }
    }
}
