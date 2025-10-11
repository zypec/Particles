package net.treasure.particles.util.nms.particles.data.color;

import net.treasure.particles.color.generator.Gradient;
import net.treasure.particles.util.nms.particles.ParticleEffect;
import net.treasure.particles.util.nms.particles.data.ParticleData;
import org.bukkit.Particle;

public abstract class ParticleColorData extends ParticleData {

    protected Particle particle;
    protected int asARGB;

    public ParticleColorData(ParticleEffect particleEffect, int alpha, int red, int green, int blue) {
        this.particle = particleEffect.bukkit();
        this.asARGB = Gradient.asARGB(alpha, red, green, blue);
    }
}