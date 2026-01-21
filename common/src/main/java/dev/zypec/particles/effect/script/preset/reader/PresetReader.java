package dev.zypec.particles.effect.script.preset.reader;

import dev.zypec.particles.Particles;
import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.exception.ReaderException;
import dev.zypec.particles.effect.script.reader.DefaultReader;
import dev.zypec.particles.effect.script.Script;
import dev.zypec.particles.effect.script.preset.Preset;

import java.util.ArrayList;
import java.util.List;

public class PresetReader extends DefaultReader<Script> {

    @Override
    public Script read(Effect effect, String type, String line) throws ReaderException {
        var lines = Particles.getEffectManager().getPresets().get(line);
        if (lines == null || lines.isEmpty())
            throw new ReaderException("Couldn't find any preset by name '" + line + "'");
        if (lines.size() == 1)
            return Particles.getEffectManager().readLine(effect, lines.get(0));
        else {
            List<Script> scripts = new ArrayList<>();
            for (String s : lines)
                scripts.add(Particles.getEffectManager().readLine(effect, s));
            return new Preset(scripts);
        }
    }
}