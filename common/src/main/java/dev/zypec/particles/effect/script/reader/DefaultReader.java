package dev.zypec.particles.effect.script.reader;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.exception.ReaderException;

public abstract class DefaultReader<S> {
    public abstract S read(Effect effect, String type, String line) throws ReaderException;
}