package dev.zypec.particles.effect.script.basic;

import dev.zypec.particles.effect.data.EffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.effect.script.Script;

public class EmptyScript extends Script {

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        return TickResult.NORMAL;
    }

    @Override
    public EmptyScript clone() {
        return new EmptyScript();
    }
}