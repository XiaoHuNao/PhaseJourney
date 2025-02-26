package org.confluence.phase_journey.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.confluence.phase_journey.mixed.ILevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(value = LevelRenderer.class, priority = 900)
public abstract class LevelRendererMixin implements ILevelRenderer {
    @Shadow
    @Nullable
    private ViewArea viewArea;

    @Shadow
    public abstract void needsUpdate();

    @Override
    public void phase_journey$rebuildAllChunks() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (IS_SODIUM_LOADED) {
            phase_journey$rebuildAllChunksSodium(player);
        } else if (viewArea != null) {
            for (SectionRenderDispatcher.RenderSection chunk : viewArea.sections) {
                chunk.setDirty(true);
            }
            needsUpdate();
        }
    }

    @Unique
    private void phase_journey$rebuildAllChunksSodium(LocalPlayer player) {
        ChunkPos chunkPos = player.chunkPosition();
        int viewDistance = Minecraft.getInstance().options.renderDistance().get();
        Level level = player.level();
        int startY = level.getMinSection();
        int endY = level.getMaxSection();
        int cx, cz;
        for (int x = -viewDistance; x < viewDistance; ++x) {
            cx = chunkPos.x + x;
            for (int z = -viewDistance; z < viewDistance; ++z) {
                cz = chunkPos.z + z;
                for (int y = startY; y <= endY; ++y) {
                    ILevelRenderer.scheduleRebuildForChunk(cx, y, cz);
                }
            }
        }
    }
}
