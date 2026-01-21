package dev.zypec.particles.effect.script.basic;

import dev.zypec.particles.effect.data.EffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.effect.script.Script;

public class BreakScript extends Script {

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        return TickResult.BREAK;
    }

    @Override
    public BreakScript clone() {
        return new BreakScript();
    }
}
