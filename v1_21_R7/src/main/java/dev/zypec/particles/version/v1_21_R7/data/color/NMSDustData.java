package dev.zypec.particles.version.v1_21_R7.data.color;

import dev.zypec.particles.util.nms.particles.data.color.ParticleDustData;
import net.minecraft.core.particles.ParticleParamRedstone;
import org.bukkit.Color;

public class NMSDustData extends ParticleDustData {

    public NMSDustData(Color color, float size) {
        super(color, size);
    }

    @Override
    public Object toNMS() {
        return new ParticleParamRedstone(asRGB, size);
    }
}