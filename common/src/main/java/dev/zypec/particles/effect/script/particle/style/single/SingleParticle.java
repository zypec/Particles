package dev.zypec.particles.effect.script.particle.style.single;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import dev.zypec.particles.color.data.ColorData;
import dev.zypec.particles.effect.data.EffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.effect.script.argument.type.IntArgument;
import dev.zypec.particles.effect.script.argument.type.RangeArgument;
import dev.zypec.particles.effect.script.argument.type.VectorArgument;
import dev.zypec.particles.effect.script.particle.ParticleSpawner;
import dev.zypec.particles.effect.script.particle.config.LocationOrigin;
import dev.zypec.particles.util.nms.particles.ParticleEffect;
import dev.zypec.particles.util.nms.particles.PacketHandler;
import org.bukkit.entity.EntityType;

@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor
public class SingleParticle extends ParticleSpawner {

    public SingleParticle(ParticleEffect particle, LocationOrigin origin,
                          VectorArgument position, VectorArgument offset, VectorArgument multiplier,
                          ColorData colorData, int colorAlpha,
                          Object particleData,
                          IntArgument amount, RangeArgument speed, RangeArgument size,
                          boolean directionalX, boolean directionalY, boolean longDistance,
                          EntityType entityTypeFilter, boolean spawnEffectOnPlayer) {
        super(particle, origin,
                position, offset, multiplier,
                colorData, colorAlpha,
                particleData,
                amount, speed, size,
                directionalX, directionalY, longDistance,
                entityTypeFilter, spawnEffectOnPlayer);
    }

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        var context = tick(data, event, true, true);
        if (context == null) return TickResult.NORMAL;

        var builder = context.builder;
        builder.location(context.origin);

        updateParticleData(builder, data);

        PacketHandler.send(builder);
        return TickResult.NORMAL;
    }

    @Override
    public SingleParticle clone() {
        return new SingleParticle(
                particle, origin,
                position, offset, multiplier,
                colorData == null ? null : colorData.clone(), colorAlpha,
                particleData,
                amount, speed, size,
                directionalX, directionalY, longDistance,
                entityTypeFilter, spawnEffectOnPlayer
        );
    }
}