package dev.zypec.particles.version.v1_21_R7.data.color;

import dev.zypec.particles.util.nms.particles.ParticleEffect;
import dev.zypec.particles.util.nms.particles.data.color.ParticleSpellData;
import net.minecraft.core.particles.Particle;
import net.minecraft.core.particles.SpellParticleOption;
import org.bukkit.craftbukkit.v1_21_R7.CraftParticle;

public class NMSSpellData extends ParticleSpellData {

    public NMSSpellData(ParticleEffect particleEffect, int alpha, int red, int green, int blue, float power) {
        super(particleEffect, alpha, red, green, blue, power);
    }

    @Override
    public Object toNMS() {
        //noinspection unchecked
        return SpellParticleOption.a((Particle<SpellParticleOption>) CraftParticle.bukkitToMinecraft(particle), asARGB, power);
    }
}