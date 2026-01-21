package dev.zypec.particles.effect.script.sound.reader;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.script.argument.type.StaticArgument;
import dev.zypec.particles.effect.script.reader.ReaderContext;
import dev.zypec.particles.effect.script.reader.ScriptReader;
import dev.zypec.particles.effect.script.sound.PlaySound;
import org.bukkit.SoundCategory;

public class SoundReader extends ScriptReader<SoundReader.Context, PlaySound> {

    public SoundReader() {
        addValidArgument(c -> c.script().sound(c.value()), true, "name");
        addValidArgument(c -> c.script().clientSide(Boolean.parseBoolean(c.value())), "client", "client-side");
        addValidArgument(c -> c.script().volume(StaticArgument.asFloat(c)), "volume");
        addValidArgument(c -> c.script().pitch(StaticArgument.asFloat(c)), "pitch");
        addValidArgument(c -> c.script().category(StaticArgument.asEnum(c, SoundCategory.class)), "category");
    }

    @Override
    public Context createContext(Effect effect, String type, String line) {
        return new Context(effect, type, line);
    }

    public static class Context extends ReaderContext<PlaySound> {
        public Context(Effect effect, String type, String line) {
            super(effect, type, line, new PlaySound());
        }
    }
}