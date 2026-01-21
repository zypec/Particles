package dev.zypec.particles.version.v1_21_R7.data.color;

import dev.zypec.particles.util.nms.particles.data.color.ParticleDustTransitionData;
import net.minecraft.core.particles.DustColorTransitionOptions;
import org.bukkit.Color;

public class NMSDustTransitionData extends ParticleDustTransitionData {

    public NMSDustTransitionData(Color color, Color transition, float size) {
        super(color, transition, size);
    }

    @Override
    public Object toNMS() {
        return new DustColorTransitionOptions(asRGB, transitionAsRGB, size);
    }
}