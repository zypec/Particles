package dev.zypec.particles.util.nms.particles;

import dev.zypec.particles.util.nms.AbstractNMSHandler;
import dev.zypec.particles.util.nms.NMSMatcher;

import java.util.List;

public class PacketHandler {

    public static AbstractNMSHandler NMS;

    public static boolean initialize() {
        NMS = NMSMatcher.match();
        return NMS != null;
    }

    public static void send(ParticleBuilder builder) {
        NMS.sendParticle(builder);
    }

    public static void send(List<ParticleBuilder> builders) {
        NMS.sendParticles(builders);
    }
}