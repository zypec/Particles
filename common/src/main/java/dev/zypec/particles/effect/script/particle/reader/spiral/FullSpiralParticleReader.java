package dev.zypec.particles.effect.script.particle.reader.spiral;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.script.argument.type.DoubleArgument;
import dev.zypec.particles.effect.script.argument.type.IntArgument;
import dev.zypec.particles.effect.script.argument.type.RangeArgument;
import dev.zypec.particles.effect.script.argument.type.StaticArgument;
import dev.zypec.particles.effect.script.particle.reader.ParticleReader;
import dev.zypec.particles.effect.script.particle.style.spiral.FullSpiralParticle;

public class FullSpiralParticleReader extends ParticleReader<FullSpiralParticle> {

    public FullSpiralParticleReader() {
        super();

        addValidArgument(c -> c.script().radius(RangeArgument.read(c)), "radius");
        addValidArgument(c -> c.script().spirals(IntArgument.read(c)), "spirals");
        addValidArgument(c -> c.script().steps(IntArgument.read(c)), "steps");
        addValidArgument(c -> c.script().gap(DoubleArgument.read(c)), "gap");
        addValidArgument(c -> c.script().vertical(StaticArgument.asBoolean(c)), "vertical");
        addValidArgument(c -> c.script().tickData(StaticArgument.asBoolean(c)), "tick-data", "tick");
        addValidArgument(c -> c.script().reverse(StaticArgument.asBoolean(c) ? -1 : 1), "reverse");
    }

    @Override
    public Context createParticleReaderContext(Effect effect, String type, String line) {
        return new Context(effect, type, line);
    }

    public static class Context extends ParticleReader.Context<FullSpiralParticle> {
        public Context(Effect effect, String type, String line) {
            super(effect, type, line, new FullSpiralParticle());
        }
    }
}