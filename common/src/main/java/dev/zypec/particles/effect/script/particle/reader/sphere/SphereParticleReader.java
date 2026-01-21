package dev.zypec.particles.effect.script.particle.reader.sphere;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.script.argument.type.RangeArgument;
import dev.zypec.particles.effect.script.argument.type.StaticArgument;
import dev.zypec.particles.effect.script.particle.reader.ParticleReader;
import dev.zypec.particles.effect.script.particle.style.sphere.SphereParticle;

public class SphereParticleReader extends ParticleReader<SphereParticle> {

    public SphereParticleReader() {
        super();

        addValidArgument(c -> c.script().particles(StaticArgument.asInt(c)), "particles");
        addValidArgument(c -> c.script().radius(RangeArgument.read(c)), "radius");
        addValidArgument(c -> c.script().tickData(StaticArgument.asBoolean(c)), "tick-data", "tick");
        addValidArgument(c -> c.script().fullSphere(StaticArgument.asBoolean(c)), "full");
        addValidArgument(c -> c.script().reverse(StaticArgument.asBoolean(c)), "reverse");
    }

    @Override
    public Context createParticleReaderContext(Effect effect, String type, String line) {
        return new Context(effect, type, line);
    }

    public static class Context extends ParticleReader.Context<SphereParticle> {
        public Context(Effect effect, String type, String line) {
            super(effect, type, line, new SphereParticle());
        }
    }
}