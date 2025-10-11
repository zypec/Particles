package net.treasure.particles.version.v1_21_R6.data.color;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.Particle;
import net.minecraft.core.particles.Particles;
import net.minecraft.core.particles.SpellParticleOption;
import net.treasure.particles.util.nms.particles.ParticleEffect;
import net.treasure.particles.util.nms.particles.data.color.ParticleColorData;
import org.bukkit.Particle.Spell;
import org.bukkit.craftbukkit.v1_21_R6.CraftParticle;

public class NMSColorData extends ParticleColorData {

    public NMSColorData(ParticleEffect effect, int alpha, int red, int green, int blue) {
        super(effect, alpha, red, green, blue);
    }

    @Override
    public Object toNMS() {
        if (particle == null) return null;
        //noinspection unchecked
        return particle.getDataType() == Spell.class ?
                SpellParticleOption.a(Particles.q, asARGB, 1f) :
                ColorParticleOption.a((Particle<ColorParticleOption>) CraftParticle.bukkitToMinecraft(particle), asARGB);
    }
}