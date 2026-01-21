package dev.zypec.particles.effect.script.conditional;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.data.EffectData;

public interface Predicate {
    boolean test(Effect effect, EffectData data);
}