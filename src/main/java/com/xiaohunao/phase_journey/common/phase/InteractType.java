package com.xiaohunao.phase_journey.common.phase;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum InteractType implements StringRepresentable {
    TAME,
    BREED,
    RIDE,
    ATTACK,
    ENTITY_INTERACT,
    USE_ITEM,
    BLOCK_BREAK,
    BLOCK_PLACE,
    BLOCK_INTERACT;

    public static final Codec<InteractType> CODEC = StringRepresentable.fromEnum(InteractType::values);

    @Override
    @NotNull
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
