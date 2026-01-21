package dev.zypec.particles.version.v1_16_R2;

import net.minecraft.server.v1_16_R2.EntityLightning;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.Packet;
import net.minecraft.server.v1_16_R2.PacketListenerPlayOut;
import net.minecraft.server.v1_16_R2.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_16_R2.PacketPlayOutWorldParticles;
import dev.zypec.particles.util.nms.AbstractNMSHandler;
import dev.zypec.particles.util.nms.particles.ParticleBuilder;
import dev.zypec.particles.util.nms.particles.ParticleEffect;
import dev.zypec.particles.version.v1_16_R2.data.NMSGenericData;
import dev.zypec.particles.version.v1_16_R2.data.color.NMSDustData;
import dev.zypec.particles.version.v1_16_R2.data.color.NMSDustTransitionData;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class NMSHandler extends AbstractNMSHandler {

    @Override
    public void sendParticle(ParticleBuilder builder) {
        var location = builder.location();

        var world = location.getWorld();
        if (world == null) return;

        var packet = new PacketPlayOutWorldParticles(
                builder.data(),
                builder.longDistance(),
                location.getX(),
                location.getY(),
                location.getZ(),
                builder.offsetX(),
                builder.offsetY(),
                builder.offsetZ(),
                builder.speed(),
                builder.amount());

        var filter = builder.viewers();
        for (var player : Bukkit.getOnlinePlayers()) {
            if (filter != null && !filter.test(player)) continue;
            if (!player.getWorld().equals(world)) continue;

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void sendParticles(List<ParticleBuilder> builders) {
        List<Packet<PacketListenerPlayOut>> packets = new ArrayList<>();
        Predicate<Player> filter = null;
        World world = null;

        for (var builder : builders) {
            filter = builder.viewers();

            var location = builder.location();
            world = location.getWorld();
            if (world == null) return;

            var packet = new PacketPlayOutWorldParticles(
                    builder.data(),
                    builder.longDistance(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    builder.offsetX(),
                    builder.offsetY(),
                    builder.offsetZ(),
                    builder.speed(),
                    builder.amount());

            packets.add(packet);
        }

        for (var player : Bukkit.getOnlinePlayers()) {
            if (filter != null && !filter.test(player)) continue;
            if (!player.getWorld().equals(world)) continue;

            var p = ((CraftPlayer) player).getHandle().playerConnection;
            for (var packet : packets)
                p.sendPacket(packet);
        }
    }

    @Override
    public Object getColorData(ParticleEffect effect, Color color, int alpha) {
        return getGenericData(effect, color);
    }

    @Override
    public Object getDustData(Color color, float size) {
        return new NMSDustData(color, size).toNMS();
    }

    @Override
    public Object getDustTransitionData(Color color, Color transition, float size) {
        return new NMSDustTransitionData(color, transition, size).toNMS();
    }

    @Override
    public Object getGenericData(ParticleEffect effect, Object object) {
        return new NMSGenericData(effect, object).toNMS();
    }

    @Override
    public void strikeLightning(Location location, Predicate<Player> filter) {
        var world = location.getWorld();
        if (world == null) return;

        var lightning = new EntityLightning(EntityTypes.LIGHTNING_BOLT, ((CraftWorld) location.getWorld()).getHandle());
        lightning.setPosition(location.getX(), location.getY(), location.getZ());
        lightning.setEffect(true);
        lightning.isSilent = true;

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (filter != null && !filter.test(player)) return;
            if (!player.getWorld().equals(world)) return;

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntity(lightning));
        });
    }
}