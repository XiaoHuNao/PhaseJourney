package org.confluence.phase_journey.common.phase;

import org.confluence.phase_journey.common.phase.block.BlockPhaseManager;
import org.confluence.phase_journey.common.phase.item.ItemPhaseManager;


public class PhaseManager {
    public static final BlockPhaseManager BLOCK = new BlockPhaseManager();
    public static final ItemPhaseManager ITEM = new ItemPhaseManager();
}
