package com.xiaohunao.phase_journey.common.phase;

import com.mojang.serialization.Codec;
import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.attachment.BlockOwnerAttachment;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.network.SyncPhasePacketS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Supplier;

public enum PhaseType implements StringRepresentable {
    LEVEL {
        @Override
        public PhaseAttachment getPhaseAttachment(@Nullable Level level,@Nullable BlockPos pos,@Nullable Player player) {
            if (level != null) {
                return PhaseAttachment.of(level);
            }
            return null;
        }
    },
    PLAYER {
        @Override
        public PhaseAttachment getPhaseAttachment(@Nullable Level level,@Nullable BlockPos pos,@Nullable Player player) {
            if (player != null) {
                return PhaseAttachment.of(player);
            }
            return null;
        }
    },
    NEAREST_PLAYER {
        @Override
        public PhaseAttachment getPhaseAttachment(@Nullable Level level,@Nullable BlockPos pos,@Nullable Player player) {
            if (level != null && pos != null) {
                Player nearestPlayer = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 8, true);
                if (nearestPlayer != null) {
                    return PhaseAttachment.of(nearestPlayer);
                }
            }
            return null;
        }
    },
    BLOCK_OWNER_PLAYER {
        @Override
        public PhaseAttachment getPhaseAttachment(@Nullable Level level, @Nullable BlockPos pos, @Nullable Player player) {
            if (level == null || pos == null) {
                return null;
            }
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) {
                return null;
            }

            BlockOwnerAttachment blockOwnerAttachment = BlockOwnerAttachment.of(blockEntity);
            Player playerByUUID = level.getPlayerByUUID(blockOwnerAttachment.ownerPlayer);
            if (playerByUUID != null) {
                return PhaseAttachment.of(playerByUUID);
            }

            return null;
        }
    };

    public abstract PhaseAttachment getPhaseAttachment(@Nullable Level level,@Nullable BlockPos pos,@Nullable Player player);

    public void applyOrRevokePhase(Level level,ResourceLocation phase, boolean add){
        PhaseAttachment phaseAttachment = getPhaseAttachment(level, null, null);
        if (phaseAttachment == null){
            return;
        }

        if (add) {
            phaseAttachment.addPhase(phase);
        } else {
            phaseAttachment.removePhase(phase);
        }

        if (!level.isClientSide){
            SyncPhasePacketS2C.sync2Level(add, phase);
        }

        for (PhaseContextType<?> type : PJRegistries.PHASE_CONTEXT_TYPE) {
            PhaseManager<?> manager = type.manager();
            if (manager.getPhases(this).stream().anyMatch(pair -> pair.getFirst().equals(phase))) {
                manager.applyOrRevokePhase(level,phase, add);
            }
        }
    }

    public void applyOrRevokePhase(Player player,ResourceLocation phase, boolean add){
        PhaseAttachment phaseAttachment = getPhaseAttachment(null, null, player);
        if (phaseAttachment == null){
            return;
        }

        if (add) {
            phaseAttachment.addPhase(phase);
        } else {
            phaseAttachment.removePhase(phase);
        }
        for (PhaseContextType<?> type : PJRegistries.PHASE_CONTEXT_TYPE) {
            PhaseManager<?> manager = type.manager();
            if (manager.getPhases(this).stream().anyMatch(pair -> pair.getFirst().equals(phase))) {
                manager.applyOrRevokePhase(player.level(),phase, add);
            }
        }

        if (!player.level().isClientSide){
            SyncPhasePacketS2C.sync2Player((ServerPlayer) player, add, phase);
        }
    }

    public static final Codec<PhaseType> CODEC = StringRepresentable.fromEnum(PhaseType::values);

    @Override
    @NotNull
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
