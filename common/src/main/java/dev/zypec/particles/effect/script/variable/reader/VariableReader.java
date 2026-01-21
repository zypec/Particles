package dev.zypec.particles.effect.script.variable.reader;

import dev.zypec.particles.constants.Patterns;
import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.exception.ReaderException;
import dev.zypec.particles.effect.script.reader.ReaderContext;
import dev.zypec.particles.effect.script.reader.ScriptReader;
import dev.zypec.particles.effect.script.variable.Variable;

public class VariableReader extends ScriptReader<ReaderContext<Variable>, Variable> {

    @Override
    public Variable read(Effect effect, String type, String line) throws ReaderException {
        var matcher = Patterns.EVAL.matcher(line);

        if (matcher.matches()) {
            int start = matcher.start(), end = matcher.end();

            var variable = matcher.group(1);
            if (!effect.hasVariable(variable)) {
                error(effect, type, line, start, end, (!effect.isPredefinedVariable(variable) ? "Unknown variable" : "You cannot edit pre-defined variables") + ": " + variable);
                return null;
            }

            var o = matcher.group(2);
            var operator = switch (o) {
                case "" -> Variable.Operator.SET;
                case "+" -> Variable.Operator.ADD;
                case "-" -> Variable.Operator.SUBTRACT;
                case "*" -> Variable.Operator.MULTIPLY;
                case "/" -> Variable.Operator.DIVIDE;
                default -> null;
            };
            if (operator == null) {
                error(effect, type, line, start, end, "Invalid operator (" + o + ")");
                return null;
            }
            return new Variable(variable, operator, matcher.group(3));
        }
        error(effect, type, line, "Incorrect variable script usage");
        return null;
    }
}