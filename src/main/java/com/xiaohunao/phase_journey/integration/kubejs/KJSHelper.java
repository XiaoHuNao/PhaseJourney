package com.xiaohunao.phase_journey.integration.kubejs;

import com.xiaohunao.phase_journey.api.event.PhaseJourneyEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;


public class KJSHelper {
    public static void init(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(KJSHelper::onPhaseJourneyRegister);
        NeoForge.EVENT_BUS.addListener(KJSHelper::onPhaseJourneyAdd);
        NeoForge.EVENT_BUS.addListener(KJSHelper::onPhaseJourneyRemove);
    }

    public static void onPhaseJourneyRegister(PhaseJourneyEvent.Register event) {
        if (PhaseJourneyEvents.REGISTER.hasListeners()) {
            PhaseJourneyEvents.REGISTER.post(ScriptType.SERVER,new PhaseJourneyEventJS.RegisterJS());
        }
    }

    public static void onPhaseJourneyAdd(PhaseJourneyEvent.Add event) {
        if (PhaseJourneyEvents.ADD.hasListeners()) {
            PhaseJourneyEvents.ADD.post(new PhaseJourneyEventJS.AddJS(event));
        }
    }

    public static void onPhaseJourneyRemove(PhaseJourneyEvent.Remove event) {
        if (PhaseJourneyEvents.REMOVE.hasListeners()) {
            PhaseJourneyEvents.REMOVE.post(new PhaseJourneyEventJS.RemoveJS(event));
        }
    }


}
