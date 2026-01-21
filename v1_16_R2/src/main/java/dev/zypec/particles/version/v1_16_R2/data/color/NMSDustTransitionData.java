package dev.zypec.particles.version.v1_16_R2.data.color;

import dev.zypec.particles.util.nms.particles.data.color.ParticleDustTransitionData;
import org.bukkit.Color;

public class NMSDustTransitionData extends ParticleDustTransitionData {

    public NMSDustTransitionData(Color color, Color transition, float size) {
        super(color, transition, size);
    }

    @Override
    public Object toNMS() {
        return null;
    }
}