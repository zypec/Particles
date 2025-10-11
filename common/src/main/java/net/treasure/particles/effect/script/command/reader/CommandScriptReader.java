package net.treasure.particles.effect.script.command.reader;

import net.treasure.particles.effect.Effect;
import net.treasure.particles.effect.script.argument.type.StaticArgument;
import net.treasure.particles.effect.script.command.CommandScript;
import net.treasure.particles.effect.script.reader.ReaderContext;
import net.treasure.particles.effect.script.reader.ScriptReader;

public class CommandScriptReader extends ScriptReader<CommandScriptReader.Context, CommandScript> {

    public CommandScriptReader() {
        addValidArgument(c -> c.script().command(StaticArgument.asString(c)), "command", "cmd");
        addValidArgument(c -> c.script().fromPlayer(StaticArgument.asBoolean(c)), "from-player");
    }

    @Override
    public Context createContext(Effect effect, String type, String line) {
        return new Context(effect, type, line);
    }

    public static class Context extends ReaderContext<CommandScript> {
        public Context(Effect effect, String type, String line) {
            super(effect, type, line, new CommandScript());
        }
    }
}