package org.confluence.phase_journey.common.phase;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.confluence.phase_journey.common.attachment.BlockOwnerAttachment;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;

import javax.annotation.Nullable;

public enum PhaseType {
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
}
