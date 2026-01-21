package dev.zypec.particles.effect.script.visual.reader;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.script.argument.type.StaticArgument;
import dev.zypec.particles.effect.script.argument.type.VectorArgument;
import dev.zypec.particles.effect.script.particle.config.LocationOrigin;
import dev.zypec.particles.effect.script.reader.ReaderContext;
import dev.zypec.particles.effect.script.reader.ScriptReader;
import dev.zypec.particles.effect.script.visual.Lightning;

public class LightningReader extends ScriptReader<LightningReader.Context, Lightning> {

    public LightningReader() {
        addValidArgument(c -> c.script().origin(StaticArgument.asEnum(c, LocationOrigin.class)), "origin");
        addValidArgument(c -> c.script().position(VectorArgument.read(c)), "position", "pos");
    }

    @Override
    public Context createContext(Effect effect, String type, String line) {
        return new Context(effect, type, line);
    }

    public static class Context extends ReaderContext<Lightning> {
        public Context(Effect effect, String type, String line) {
            super(effect, type, line, new Lightning());
        }
    }
}