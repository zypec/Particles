package dev.zypec.particles.effect.script;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.data.EffectData;

public interface Cached {
    void setIndex(int index);

    int getIndex();

    void preTick(Effect effect, EffectData data, int times);
}
