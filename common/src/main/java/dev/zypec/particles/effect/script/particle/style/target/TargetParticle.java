package dev.zypec.particles.effect.script.particle.style.target;

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
public class TargetParticle extends ParticleSpawner {

    protected VectorArgument target;
    protected IntArgument duration;

    public TargetParticle(ParticleEffect particle, LocationOrigin origin,
                          VectorArgument target, IntArgument duration,
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
        this.target = target;
        this.duration = duration;
    }

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        var context = tick(data, event, false, false);
        if (context == null) return TickResult.NORMAL;

        var builder = context.builder;
        var target = rotate(context, this.target.get(this, data));

        builder.data(PacketHandler.NMS.getTargetData(
                particle,
                data,
                colorData == null ? null : colorData.next(data),
                context.origin.clone().add(target),
                duration.get(this, data)
        ));
        builder.location(context.origin);

        PacketHandler.send(builder);
        return TickResult.NORMAL;
    }

    @Override
    public TargetParticle clone() {
        return new TargetParticle(
                particle, origin,
                target, duration,
                position, offset, multiplier,
                colorData == null ? null : colorData.clone(), colorAlpha,
                particleData,
                amount, speed, size,
                directionalX, directionalY, longDistance,
                entityTypeFilter, spawnEffectOnPlayer
        );
    }
}