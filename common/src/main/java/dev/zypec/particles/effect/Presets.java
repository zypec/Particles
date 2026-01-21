package dev.zypec.particles.effect;

import lombok.Getter;
import dev.zypec.particles.Particles;
import dev.zypec.particles.configuration.ConfigurationGenerator;
import dev.zypec.particles.configuration.DataHolder;
import dev.zypec.particles.effect.exception.ReaderException;
import dev.zypec.particles.effect.script.Script;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Presets implements DataHolder {

    public static final String VERSION = "1.5.2";

    @Getter
    private final ConfigurationGenerator generator;
    private FileConfiguration configuration;

    public Presets() {
        this.generator = new ConfigurationGenerator("presets.yml");
    }

    @Override
    public boolean initialize() {
        this.configuration = generator.generate();
        return configuration != null;
    }

    @Override
    public void reload() {
        initialize();
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    public void load() {
        configuration = getConfiguration();
    }

    public void reset() {
        generator.reset();
        configuration = generator.generate();
    }

    public List<String> get(String key) {
        if (configuration.isList(key))
            return configuration.getStringList(key);
        var script = configuration.getString(key);
        return script == null ? null : List.of(script);
    }

    public Script read(Effect effect, String key) throws ReaderException {
        if (configuration.isList(key)) return null;
        var script = configuration.getString(key);
        if (script == null) return null;
        return Particles.getEffectManager().readLine(effect, script);
    }
}