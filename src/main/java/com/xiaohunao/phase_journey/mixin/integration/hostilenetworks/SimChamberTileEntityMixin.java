package com.xiaohunao.phase_journey.mixin.integration.hostilenetworks;


import dev.shadowsoffire.hostilenetworks.data.DataModel;
import dev.shadowsoffire.hostilenetworks.data.DataModelInstance;
import dev.shadowsoffire.hostilenetworks.tile.SimChamberTileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * SimChamberTileEntity的Mixin
 * 在canStartSimulation方法中注入阶段判断逻辑
 */
@Mixin(SimChamberTileEntity.class)
public abstract class SimChamberTileEntityMixin {
    @Shadow protected DataModelInstance currentModel;
    @Shadow protected SimChamberTileEntity.FailureState failState;

    /**
     * 在canStartSimulation方法开始处注入阶段检查
     * 如果玩家没有达到所需阶段，则禁止启动模拟
     */
    @Inject(method = "canStartSimulation", at = @At("HEAD"), cancellable = true)
    private void phaseJourney$checkPhaseRequirement(CallbackInfoReturnable<Boolean> cir) {
        SimChamberTileEntity tileEntity = (SimChamberTileEntity) (Object) this;
        Level level = tileEntity.getLevel();
        
        if (level == null || level.isClientSide()) {
            return; // 客户端或无效世界，跳过检查
        }

        DataModel model = currentModel.getModel();
        EntityType<?> entityType = model.entity();


    }
}
