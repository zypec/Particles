package dev.zypec.particles.effect.script.particle.reader.polygon;

import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.exception.ReaderException;
import dev.zypec.particles.effect.script.argument.type.RangeArgument;
import dev.zypec.particles.effect.script.argument.type.StaticArgument;
import dev.zypec.particles.effect.script.particle.reader.ParticleReader;
import dev.zypec.particles.effect.script.particle.style.polygon.PolygonParticle;

public class PolygonParticleReader extends ParticleReader<PolygonParticle> {

    public PolygonParticleReader() {
        super();

        addValidArgument(c -> c.script().radius(RangeArgument.read(c)), "radius");
        addValidArgument(c -> c.script().rotation(RangeArgument.read(c)), "rotation");
        addValidArgument(c -> c.script().points(StaticArgument.asInt(c)), "points");
        addValidArgument(c -> c.script().step(StaticArgument.asFloat(c)), "step");
        addValidArgument(c -> c.script().vertical(StaticArgument.asBoolean(c)), "vertical");
        addValidArgument(c -> c.script().tickData(StaticArgument.asBoolean(c)), "tick-data", "tick");
    }

    @Override
    public Context createParticleReaderContext(Effect effect, String type, String line) {
        return new Context(effect, type, line);
    }

    @Override
    public boolean validate(ParticleReader.Context<PolygonParticle> context) throws ReaderException {
        if (!super.validate(context)) return false;
        if (context.script().step() <= 0) {
            error(context.effect(), context.type(), context.line(), "'step' value must be greater than 0");
            return false;
        }

        if (context.script().points() < 3) {
            error(context.effect(), context.type(), context.line(), "'points' value must be greater or equal than 3");
            return false;
        }

        context.script().initialize();
        return true;
    }

    public static class Context extends ParticleReader.Context<PolygonParticle> {
        public Context(Effect effect, String type, String line) {
            super(effect, type, line, new PolygonParticle());
        }
    }
}