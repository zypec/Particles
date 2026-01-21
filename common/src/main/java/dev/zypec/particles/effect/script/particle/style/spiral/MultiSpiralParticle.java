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
import dev.zypec.particles.util.nms.particles.ParticleBuilder;
import dev.zypec.particles.util.nms.particles.ParticleEffect;
import dev.zypec.particles.util.nms.particles.PacketHandler;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor
public class MultiSpiralParticle extends ParticleSpawner {

    private RangeArgument radius = new RangeArgument(1f);
    private IntArgument spirals = new IntArgument(3);
    private IntArgument steps = new IntArgument(120);
    private boolean tickData = false;
    private boolean vertical = false;
    private int reverse = 1;

    private double step;

    public MultiSpiralParticle(ParticleEffect particle, LocationOrigin origin,
                               RangeArgument radius, IntArgument spirals, IntArgument steps,
                               boolean tickData, boolean vertical, int reverse,
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
        this.spirals = spirals;
        this.steps = steps;
        this.tickData = tickData;
        this.vertical = vertical;
        this.reverse = reverse;
    }

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        var context = tick(data, event, true, false);
        if (context == null) return TickResult.NORMAL;

        var builder = context.builder;

        var spirals = this.spirals.get(this, data);
        var radius = this.radius.get(this, data);
        var steps = this.steps.get(this, data);

        updateParticleData(builder, data);

        List<ParticleBuilder> builders = new ArrayList<>();

        var s = reverse * (step / steps) * MathUtils.PI2;
        var dP = MathUtils.PI2 / spirals;
        for (int i = 0; i < spirals; i++) {
            var r = s + dP * i;
            builders.add(builder.copy().location(location(context, r, radius, vertical)));

            if (tickData)
                updateParticleData(builder, data);
        }

        step++;
        if (step >= steps)
            step = 0;

        PacketHandler.send(builders);
        return TickResult.NORMAL;
    }

    @Override
    public MultiSpiralParticle clone() {
        return new MultiSpiralParticle(
                particle, origin,
                radius, spirals, steps,
                tickData, vertical, reverse,
                position, offset, multiplier,
                colorData == null ? null : colorData.clone(), colorAlpha,
                particleData,
                amount, speed, size,
                directionalX, directionalY, longDistance,
                entityTypeFilter, spawnEffectOnPlayer
        );
    }
}