package dev.zypec.particles.effect.script.basic;

import dev.zypec.particles.effect.data.EffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.effect.script.Script;

public class BreakHandlerScript extends Script {

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        return TickResult.BREAK_HANDLER;
    }

    @Override
    public BreakHandlerScript clone() {
        return new BreakHandlerScript();
    }
}
