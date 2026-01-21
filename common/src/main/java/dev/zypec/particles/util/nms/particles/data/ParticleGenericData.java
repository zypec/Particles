package dev.zypec.particles.util.nms.particles.data;

import dev.zypec.particles.util.nms.particles.ParticleEffect;
import org.bukkit.Particle;

public abstract class ParticleGenericData extends ParticleData {

    protected Particle particle;
    protected Object object;

    public ParticleGenericData(ParticleEffect particleEffect, Object object) {
        this.particle = particleEffect.bukkit();
        this.object = object;
    }
}