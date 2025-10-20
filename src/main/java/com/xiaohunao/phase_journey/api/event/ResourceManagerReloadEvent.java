package com.xiaohunao.phase_journey.api.event;

import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.LogicalSide;

public class ResourceManagerReloadEvent extends Event {
    private final ResourceManager resources;
    private final LogicalSide side;

    public ResourceManagerReloadEvent(ResourceManager resources, LogicalSide side) {
        this.resources = resources;
        this.side = side;
    }

    public ResourceManager getResources() {
        return this.resources;
    }

    public LogicalSide getSide() {
        return this.side;
    }
}
