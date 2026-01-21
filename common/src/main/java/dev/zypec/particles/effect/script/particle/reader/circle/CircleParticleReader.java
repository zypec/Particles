package dev.zypec.particles.effect.script.particle.reader.circle;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.script.argument.type.IntArgument;
import dev.zypec.particles.effect.script.argument.type.RangeArgument;
import dev.zypec.particles.effect.script.argument.type.StaticArgument;
import dev.zypec.particles.effect.script.particle.reader.ParticleReader;
import dev.zypec.particles.effect.script.particle.style.circle.CircleParticle;

public class CircleParticleReader extends ParticleReader<CircleParticle> {

    public CircleParticleReader() {
        super();
        addValidArguments(this);
    }

    public static void addValidArguments(ParticleReader<? extends CircleParticle> reader) {
        reader.addValidArgument(c -> c.script().tickData(StaticArgument.asBoolean(c)), "tick-data", "tick");
        reader.addValidArgument(c -> c.script().particles(IntArgument.read(c, 1)), "particles");
        reader.addValidArgument(c -> c.script().radius(RangeArgument.read(c)), "radius");
        reader.addValidArgument(c -> c.script().vertical(StaticArgument.asBoolean(c)), "vertical");
    }

    @Override
    public Context createParticleReaderContext(Effect effect, String type, String line) {
        return new Context(effect, type, line);
    }

    public static class Context extends ParticleReader.Context<CircleParticle> {
        public Context(Effect effect, String type, String line) {
            super(effect, type, line, new CircleParticle());
            script.directionalX(true).directionalY(true);
        }
    }
}