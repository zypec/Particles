package dev.zypec.particles.version.v1_16_R3.data;

import dev.zypec.particles.util.nms.particles.ParticleEffect;
import dev.zypec.particles.util.nms.particles.data.ParticleGenericData;
import org.bukkit.craftbukkit.v1_16_R3.CraftParticle;

public class NMSGenericData extends ParticleGenericData {

    public NMSGenericData(ParticleEffect effect, Object object) {
        super(effect, object);
    }

    @Override
    public Object toNMS() {
        if (particle == null) return null;
        return CraftParticle.toNMS(particle, object);
    }
}