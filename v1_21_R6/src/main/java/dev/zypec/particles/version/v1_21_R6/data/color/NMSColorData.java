package dev.zypec.particles.version.v1_21_R6.data.color;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.Particle;
import dev.zypec.particles.util.nms.particles.ParticleEffect;
import dev.zypec.particles.util.nms.particles.data.color.ParticleColorData;
import org.bukkit.craftbukkit.v1_21_R6.CraftParticle;

public class NMSColorData extends ParticleColorData {

    public NMSColorData(ParticleEffect effect, int alpha, int red, int green, int blue) {
        super(effect, alpha, red, green, blue);
    }

    @Override
    public Object toNMS() {
        if (particle == null) return null;
        //noinspection unchecked
        return ColorParticleOption.a((Particle<ColorParticleOption>) CraftParticle.bukkitToMinecraft(particle), asARGB);
    }
}