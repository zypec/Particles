package dev.zypec.particles.effect.script.basic.reader;

import lombok.AllArgsConstructor;
import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.exception.ReaderException;
import dev.zypec.particles.effect.script.Script;
import dev.zypec.particles.effect.script.reader.ReaderContext;
import dev.zypec.particles.effect.script.reader.ScriptReader;

import java.util.function.Function;

@AllArgsConstructor
public class BasicScriptReader<T extends Script> extends ScriptReader<ReaderContext<T>, T> {

    protected final Function<String, T> callable;

    @Override
    public T read(Effect effect, String type, String line) throws ReaderException {
        try {
            return callable.apply(line);
        } catch (Exception e) {
            return null;
        }
    }
}