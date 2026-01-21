package dev.zypec.particles.effect.script.particle.style.target.circle;

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
import dev.zypec.particles.effect.script.particle.config.LocationOrigin;
import dev.zypec.particles.effect.script.particle.style.circle.CircleParticle;
import dev.zypec.particles.util.math.MathUtils;
import dev.zypec.particles.util.nms.particles.ParticleBuilder;
import dev.zypec.particles.util.nms.particles.ParticleEffect;
import dev.zypec.particles.util.nms.particles.PacketHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent = true)
public class TargetCircleParticle extends CircleParticle {

    private RangeArgument spread;
    private IntArgument duration;
    private TargetPoint targetPoint;

    public TargetCircleParticle(ParticleEffect particle, LocationOrigin origin,
                                RangeArgument spread, IntArgument duration, TargetPoint targetPoint,
                                IntArgument particles, RangeArgument radius, boolean tickData, boolean vertical,
                                VectorArgument position, VectorArgument offset, VectorArgument multiplier,
                                ColorData colorData, int colorAlpha,
                                Object particleData,
                                IntArgument amount, RangeArgument speed, RangeArgument size,
                                boolean directionalX, boolean directionalY, boolean longDistance,
                                EntityType entityTypeFilter, boolean spawnEffectOnPlayer) {
        super(particle, origin,
                particles, radius, tickData, vertical,
                position, offset, multiplier,
                colorData, colorAlpha,
                particleData,
                amount, speed, size,
                directionalX, directionalY, longDistance,
                entityTypeFilter, spawnEffectOnPlayer);
        this.spread = spread;
        this.duration = duration;
        this.targetPoint = targetPoint;
    }

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        var context = tick(data, event, false, false);
        if (context == null) return TickResult.NORMAL;

        var builder = context.builder;

        // Circle Particle Variables
        var particles = this.particles.get(this, data);
        var radius = this.radius.get(this, data);

        var color = colorData != null ? colorData.next(data) : null;

        // Spread
        var spread = this.spread.get(this, data);
        var duration = this.duration.get(this, data);

        List<ParticleBuilder> builders = new ArrayList<>();
        var p = MathUtils.PI2 / particles;
        for (int i = 0; i < particles; i++) {
            var r = p * i;
            var x = MathUtils.cos(r) * radius;
            var y = MathUtils.sin(r) * radius;

            var location = location(context, vertical ? new Vector(x, y, 0) : new Vector(x, 0, y));

            // Spread
            var target = switch (targetPoint) {
                case VERTICAL -> location.clone().add(0, spread, 0);
                case HORIZONTAL -> {
                    var angle = MathUtils.atan2(y, x);
                    yield location(context, vertical ? new Vector(MathUtils.cos(angle) * spread, MathUtils.sin(angle) * spread, 0) : new Vector(MathUtils.cos(angle) * spread, 0, MathUtils.sin(angle) * spread));
                }
            };
            builders.add(builder.copy()
                    .location(location)
                    .data(PacketHandler.NMS.getTargetData(
                            particle,
                            data,
                            color,
                            target,
                            duration
                    ))
            );

            if (tickData)
                color = colorData != null ? colorData.next(data) : null;
        }

        PacketHandler.send(builders);
        return TickResult.NORMAL;
    }

    @Override
    public TargetCircleParticle clone() {
        return new TargetCircleParticle(
                particle, origin,
                spread, duration, targetPoint,
                particles, radius, tickData, vertical,
                position, offset, multiplier,
                colorData == null ? null : colorData.clone(), colorAlpha,
                particleData,
                amount, speed, size,
                directionalX, directionalY, longDistance,
                entityTypeFilter, spawnEffectOnPlayer
        );
    }
}