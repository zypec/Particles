package dev.zypec.particles.color.data.duo;

import dev.zypec.particles.color.data.ColorData;
import dev.zypec.particles.color.scheme.ColorScheme;
import dev.zypec.particles.util.tuples.Pair;
import org.bukkit.Color;

public class DuoColorsData extends ColorData implements DuoImpl {

    private final ColorScheme color1, color2;

    public DuoColorsData(ColorScheme color1, ColorScheme color2, float speed, boolean revertWhenDone, boolean stopCycle) {
        super(speed, revertWhenDone, stopCycle);
        this.color1 = color1;
        this.color2 = color2;
        this.max = color1.getColors().size();
    }

    @Override
    public Pair<Color, Color> nextDuo() {
        var index = index();
        return new Pair<>(color1.getColors().get(index), color2.getColors().get(index));
    }

    @Override
    public DuoColorsData clone() {
        return new DuoColorsData(color1, color2, speed, revertWhenDone, stopCycle);
    }
}