package dev.zypec.particles.color.data;

import lombok.Getter;
import dev.zypec.particles.color.generator.Gradient;
import dev.zypec.particles.effect.data.EffectData;
import org.bukkit.Color;

@Getter
public class SingleColorData extends ColorData {

    private final Color color;

    public SingleColorData(Color color) {
        super(0, false);
        this.max = 1;
        this.color = color;
    }

    public SingleColorData(String hex) {
        super(0, false);
        this.max = 1;
        this.color = Gradient.hex2Rgb(hex);
    }

    @Override
    public Color next(EffectData data) {
        return color;
    }

    @Override
    public SingleColorData clone() {
        return new SingleColorData(color);
    }
}