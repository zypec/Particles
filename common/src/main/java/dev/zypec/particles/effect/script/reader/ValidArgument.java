package dev.zypec.particles.effect.script.reader;

import lombok.AllArgsConstructor;
import dev.zypec.particles.effect.script.Script;
import dev.zypec.particles.util.unsafe.UnsafeConsumer;

@AllArgsConstructor
public class ValidArgument<C extends ReaderContext<T>, T extends Script> {
    protected String key;
    protected UnsafeConsumer<C> reader;
    protected boolean required;
}