package org.confluence.phase_journey.common.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.confluence.phase_journey.PhaseJourney;

public class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            PhaseJourney.asResource("network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        CHANNEL.registerMessage(packetId++, PhaseSyncS2CPack.class, PhaseSyncS2CPack::encode, PhaseSyncS2CPack::decode, PhaseSyncS2CPack::handle);
    }
}
