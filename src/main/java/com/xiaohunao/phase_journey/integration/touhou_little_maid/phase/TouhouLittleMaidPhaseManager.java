package com.xiaohunao.phase_journey.integration.touhou_little_maid.phase;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTaskEnableEvent;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.xiaohunao.phase_journey.api.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;

public class TouhouLittleMaidPhaseManager extends PhaseManager<TouhouLittleMaidPhaseContext> {

    public static final TouhouLittleMaidPhaseManager MANAGER = new TouhouLittleMaidPhaseManager();

    public boolean isRestricted(Level level, BlockPos pos, Player player, ResourceLocation taskId) {
        return PhaseUtils.anyContextMatches(this,level,player,pos,(ctx, phaseManager) -> {
            return ctx.isTaskAllowed(taskId);
        });
    }

    @SubscribeEvent
    public void onMaidTaskEnable(MaidTaskEnableEvent event) {
        EntityMaid maid = event.getEntityMaid();
        IMaidTask task = event.getTargetTask();
        Player owner = maid.getOwner() instanceof Player ? (Player) maid.getOwner() : null;
        Level level = maid.level();

        if (isRestricted(level, maid.blockPosition(), owner, task.getUid())) {
            event.setCanceled(true);
        }
    }
}
