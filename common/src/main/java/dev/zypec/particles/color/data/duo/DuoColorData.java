package dev.zypec.particles.color.data.duo;

import dev.zypec.particles.color.data.ColorData;
import dev.zypec.particles.color.scheme.ColorScheme;
import dev.zypec.particles.util.tuples.Pair;
import org.bukkit.Color;

public class DuoColorData extends ColorData implements DuoImpl {

    private final ColorScheme color;
    private final Color duo;

    public DuoColorData(ColorScheme color, Color duo, float speed, boolean revertWhenDone, boolean stopCycle) {
        super(speed, revertWhenDone, stopCycle);
        this.color = color;
        this.duo = duo;
        this.max = color.getColors().size();
    }

    @Override
    public Pair<Color, Color> nextDuo() {
        var index = index();
        return new Pair<>(color.getColors().get(index), duo);
    }

    @Override
    public DuoColorData clone() {
        return new DuoColorData(color, duo, speed, revertWhenDone, stopCycle);
    }
}