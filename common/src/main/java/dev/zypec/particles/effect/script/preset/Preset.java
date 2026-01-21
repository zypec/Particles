package dev.zypec.particles.effect.script.preset;

import lombok.AllArgsConstructor;
import dev.zypec.particles.effect.data.EffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.effect.script.Script;

import java.util.List;

@AllArgsConstructor
public class Preset extends Script {

    private List<Script> scripts;

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        for (var script : scripts) {
            var result = script.tick(data, event, times);
            if (result != TickResult.NORMAL)
                return result;
        }
        return TickResult.NORMAL;
    }

    @Override
    public Preset clone() {
        return new Preset(scripts);
    }
}