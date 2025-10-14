package org.confluence.phase_journey.mixin.recipe.immersiveengineering;


import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FurnaceHandler;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.confluence.phase_journey.common.phase.recipe.RecipePhaseManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FurnaceHandler.class, remap = false)
public abstract class FurnaceHandlerMixin<R extends IESerializableRecipe> {
    @Shadow @Nullable protected abstract R getRecipe(FurnaceHandler.IFurnaceEnvironment<R> env);

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    //ALLOY
    //BLAST_FUEL

    public void tickServer(IMultiblockContext<? extends FurnaceHandler.IFurnaceEnvironment<R>> ctx, CallbackInfoReturnable<Boolean> cir) {
        IMultiblockLevel multiblockLevel = ctx.getLevel();
        FurnaceHandler.IFurnaceEnvironment<R> env = ctx.getState();
        R recipe = getRecipe(env);
        if (recipe == null){
            return;
        }
        for (RecipeHolder<?> holder : multiblockLevel.getRawLevel().getRecipeManager().byType.get(recipe.getType())) {
            if (holder.value().equals(recipe)) {
                if (RecipePhaseManager.MANAGER.isRestricted(multiblockLevel.getRawLevel(),multiblockLevel.getAbsoluteOrigin(),recipe.getType(),holder.id())){
                    cir.cancel();
                }
            }
        }

    }


}
