package org.confluence.phase_journey.mixed;

import net.neoforged.fml.ModList;

public interface ILevelRenderer {
    boolean IS_SODIUM_LOADED = ModList.get().isLoaded("sodium");

    void phase_journey$rebuildAllChunks();
}
