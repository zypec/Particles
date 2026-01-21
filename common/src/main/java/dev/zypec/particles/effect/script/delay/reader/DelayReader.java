package dev.zypec.particles.effect.script.delay.reader;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.script.Script;
import dev.zypec.particles.effect.script.argument.type.StaticArgument;
import dev.zypec.particles.effect.script.delay.DelayScript;
import dev.zypec.particles.effect.script.reader.ReaderContext;
import dev.zypec.particles.effect.script.reader.ScriptReader;

public class DelayReader extends ScriptReader<DelayReader.Context, DelayScript> {

    public DelayReader() {
        addValidArgument(c -> c.script().delay(StaticArgument.asInt(c, 1)), true, "value");
        addValidArgument(c -> c.script().action(StaticArgument.asEnum(c, Script.TickResult.class)), "action");
    }

    @Override
    public Context createContext(Effect effect, String type, String line) {
        return new Context(effect, type, line);
    }

    public static class Context extends ReaderContext<DelayScript> {
        public Context(Effect effect, String type, String line) {
            super(effect, type, line, new DelayScript());
        }
    }
}