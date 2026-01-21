package dev.zypec.particles.util.nms.particles.data.color;

import dev.zypec.particles.util.nms.particles.ParticleEffect;

public abstract class ParticleSpellData extends ParticleColorData {

    protected float power;

    public ParticleSpellData(ParticleEffect particleEffect, int alpha, int red, int green, int blue, float power) {
        super(particleEffect, alpha, red, green, blue);
        this.power = power;
    }
}