package com.xiaohunao.phase_journey.mixed;

import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.neoforged.fml.ModList;

public interface ILevelRenderer {
    boolean IS_SODIUM_LOADED = ModList.get().isLoaded("sodium");

    void phase_journey$rebuildAllChunks();

    static void scheduleRebuildForChunk(int x, int y, int z) {
        SodiumWorldRenderer.instance().scheduleRebuildForChunk(x, y, z, false);
    }
}
