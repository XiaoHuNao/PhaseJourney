package com.xiaohunao.phase_journey.mixin.integration.ftbquests;

import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.network.RebuildChunksS2C;
import com.xiaohunao.phase_journey.common.network.SyncPhasePacketS2C;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.reward.StageReward;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StageReward.class, remap = false)
public class StageRewardMixin {
    @Shadow private String stage;
    @Shadow private boolean remove;

    @Unique
    private boolean phase_journey$isLevelPhase = false;

    @Inject(method = "writeData", at = @At("HEAD"), cancellable = true)
    private void writeData(CompoundTag nbt, HolderLookup.Provider provider, CallbackInfo ci) {
        ResourceLocation phaseId = stage.isEmpty() ? null : ResourceLocation.tryParse(stage);
        nbt.putString("phase", phaseId == null ? "" : phaseId.toString());
        if (remove) {
            nbt.putBoolean("remove", true);
        }
        if (phase_journey$isLevelPhase) {
            nbt.putBoolean("isLevelPhase", true);
        }

        ci.cancel();
    }

    @Inject(method = "readData", at = @At("HEAD"), cancellable = true)
    private void readData(CompoundTag nbt, HolderLookup.Provider provider, CallbackInfo ci) {
        String s = nbt.getString("phase");
        stage = s.isEmpty() ? "" : s;
        remove = nbt.getBoolean("remove");
        phase_journey$isLevelPhase = nbt.getBoolean("isLevelPhase");

        ci.cancel();
    }

    @Inject(method = "writeNetData", at = @At("HEAD"), cancellable = true)
    private void writeNetData(RegistryFriendlyByteBuf buf, CallbackInfo ci) {
        ResourceLocation phaseId = stage.isEmpty() ? null : ResourceLocation.tryParse(stage);
        buf.writeUtf(phaseId == null ? "" : phaseId.toString(), Short.MAX_VALUE);
        buf.writeBoolean(remove);
        buf.writeBoolean(phase_journey$isLevelPhase);

        ci.cancel();
    }

    @Inject(method = "readNetData", at = @At("HEAD"), cancellable = true)
    private void readNetData(RegistryFriendlyByteBuf buf, CallbackInfo ci) {
        String s = buf.readUtf(Short.MAX_VALUE);
        stage = s.isEmpty() ? "" : s;
        remove = buf.readBoolean();
        phase_journey$isLevelPhase = buf.readBoolean();

        ci.cancel();
    }

    @Inject(method = "claim", at = @At("HEAD"), cancellable = true)
    private void claim(ServerPlayer player, boolean notify, CallbackInfo ci) {
        ResourceLocation phaseId = stage.isEmpty() ? null : ResourceLocation.tryParse(stage);
        if (phaseId == null) {
            ci.cancel();
            return;
        }

        if (phase_journey$isLevelPhase) {
            PhaseType.LEVEL.applyOrRevokePhase(player.level(),phaseId,!remove);
        } else {
            PhaseType.PLAYER.applyOrRevokePhase(player,phaseId,!remove);
        }


        if (notify) {
            MutableComponent msg = Component.literal(phaseId.toString()).withStyle(ChatFormatting.YELLOW);
            String prefix = phase_journey$isLevelPhase ?
                (remove ? "Level Phase removed: " : "Level Phase added: ") :
                (remove ? "Player Phase removed: " : "Player Phase added: ");
            player.sendSystemMessage(Component.literal(prefix).append(msg), true);
        }

        ci.cancel();
    }

    @Inject(method = "fillConfigGroup", at = @At("HEAD"), cancellable = true)
    private void fillConfigGroup(ConfigGroup config, CallbackInfo ci) {
        config.addString("stage", this.stage, (v) -> this.stage = v, "").setNameKey("ftbquests.reward.ftbquests.gamestage");
        config.addBool("remove", this.remove, (v) -> this.remove = v, false);
        config.addBool("isLevelPhase", phase_journey$isLevelPhase, v -> phase_journey$isLevelPhase = v, false).setNameKey("phase_journey.reward.isLevelPhase");
        ci.cancel();
    }

    @Inject(method = "getAltTitle()Lnet/minecraft/network/chat/MutableComponent;", at = @At("HEAD"), cancellable = true)
    private void getAltTitle(CallbackInfoReturnable<MutableComponent> cir) {
        ResourceLocation phaseId = stage.isEmpty() ? null : ResourceLocation.tryParse(stage);
        String title = phase_journey$isLevelPhase ? "Level Phase" : "Player Phase";
        cir.setReturnValue(Component.literal(title).append(": ").append(Component.literal(phaseId == null ? "" : phaseId.toString()).withStyle(ChatFormatting.YELLOW)));
    }
}
