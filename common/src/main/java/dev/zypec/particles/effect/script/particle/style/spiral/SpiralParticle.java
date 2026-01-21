package dev.zypec.particles.effect.script.particle.style.spiral;

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
import dev.zypec.particles.util.math.MathUtils;
import dev.zypec.particles.util.nms.particles.ParticleEffect;
import dev.zypec.particles.util.nms.particles.PacketHandler;
import org.bukkit.entity.EntityType;

@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor
public class SpiralParticle extends ParticleSpawner {

    private RangeArgument radius = new RangeArgument(1f);
    private IntArgument steps = new IntArgument(120);
    private boolean vertical = false;
    private int reverse = 1;

    private double step;

    public SpiralParticle(ParticleEffect particle, LocationOrigin origin,
                          RangeArgument radius, IntArgument steps,
                          boolean vertical, int reverse,
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
        this.radius = radius;
        this.steps = steps;
        this.vertical = vertical;
        this.reverse = reverse;
    }

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        var context = tick(data, event, true, false);
        if (context == null) return TickResult.NORMAL;

        var builder = context.builder;

        var radius = this.radius.get(this, data);
        var steps = this.steps.get(this, data);

        var r = reverse * (step / steps) * MathUtils.PI2;
        builder.location(location(context, r, radius, vertical));

        updateParticleData(builder, data);

        step++;

        if (step >= steps)
            step = 0;

        PacketHandler.send(builder);
        return TickResult.NORMAL;
    }

    @Override
    public SpiralParticle clone() {
        return new SpiralParticle(
                particle, origin,
                radius, steps,
                vertical, reverse,
                position, offset, multiplier,
                colorData == null ? null : colorData.clone(), colorAlpha,
                particleData,
                amount, speed, size,
                directionalX, directionalY, longDistance,
                entityTypeFilter, spawnEffectOnPlayer
        );
    }
}