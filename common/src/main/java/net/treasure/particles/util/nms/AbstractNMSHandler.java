package net.treasure.particles.util.nms;

import net.treasure.particles.effect.data.EffectData;
import net.treasure.particles.util.nms.particles.ParticleBuilder;
import net.treasure.particles.util.nms.particles.ParticleEffect;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractNMSHandler {

    public abstract void sendParticle(ParticleBuilder builder);

    public abstract void sendParticles(List<ParticleBuilder> builders);

    public abstract Object getColorData(ParticleEffect effect, Color color, int alpha);

    public abstract Object getDustData(Color color, float size);

    public abstract Object getDustTransitionData(Color color, Color transition, float size);

    public abstract Object getGenericData(ParticleEffect effect, Object data);

    public Object getParticleParam(ParticleEffect effect) {
        return getGenericData(effect, null);
    }

    public Object getTargetData(ParticleEffect effect, EffectData effectData, Color color, Location target, int duration) {
        return getGenericData(effect, null);
    }

    public abstract void strikeLightning(Location location, Predicate<Player> filter);
}