package dev.zypec.particles.effect.script.variable.reader;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.script.argument.type.DoubleArgument;
import dev.zypec.particles.effect.script.argument.type.StaticArgument;
import dev.zypec.particles.effect.script.reader.ReaderContext;
import dev.zypec.particles.effect.script.reader.ScriptReader;
import dev.zypec.particles.effect.script.variable.VariableCycle;

public class VariableCycleReader extends ScriptReader<VariableCycleReader.Context, VariableCycle> {

    public VariableCycleReader() {
        addValidArgument(c -> {
            var variable = c.value();
            if (!c.effect().hasVariable(variable)) {
                error(c, (!c.effect().isPredefinedVariable(variable) ? "Unknown variable" : "You cannot edit pre-defined variables") + ": " + variable);
                return;
            }
            c.script().variable(variable);
        }, true, "variable");

        addValidArgument(c -> c.script().operator(StaticArgument.asEnum(c, VariableCycle.Operator.class)), "operator");

        addValidArgument(c -> c.script().step(DoubleArgument.read(c)), true, "step");
        addValidArgument(c -> c.script().min(DoubleArgument.read(c)), true, "min");
        addValidArgument(c -> c.script().max(DoubleArgument.read(c)), true, "max");

        addValidArgument(c -> c.script().revertWhenDone(StaticArgument.asBoolean(c)), "revert");
    }

    @Override
    public Context createContext(Effect effect, String type, String line) {
        return new Context(effect, type, line);
    }

    public static class Context extends ReaderContext<VariableCycle> {
        public Context(Effect effect, String type, String line) {
            super(effect, type, line, new VariableCycle());
        }
    }
}