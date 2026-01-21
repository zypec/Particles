package dev.zypec.particles.gui;

import lombok.Getter;
import lombok.experimental.Accessors;
import dev.zypec.particles.Particles;
import dev.zypec.particles.configuration.ConfigurationGenerator;
import dev.zypec.particles.configuration.DataHolder;
import dev.zypec.particles.gui.config.GUIElements;
import dev.zypec.particles.gui.config.GUILayout;
import dev.zypec.particles.gui.config.GUISounds;
import dev.zypec.particles.gui.config.GUIStyle;
import dev.zypec.particles.gui.listener.GUIListener;
import dev.zypec.particles.gui.task.GUITask;
import dev.zypec.particles.gui.type.GUIType;
import dev.zypec.particles.gui.type.admin.AdminGUI;
import dev.zypec.particles.gui.type.color.ColorsGUI;
import dev.zypec.particles.gui.type.effects.EffectsGUI;
import dev.zypec.particles.gui.type.mixer.MixerGUI;
import dev.zypec.particles.gui.type.mixer.effect.TickHandlersGUI;
import dev.zypec.particles.util.logging.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class GUIManager implements DataHolder {

    public static final String VERSION = "1.5.2";

    private final ConfigurationGenerator generator;
    private YamlConfiguration config;

    private GUIStyle style;
    private final GUIElements elements = new GUIElements();
    private final GUISounds sounds = new GUISounds();

    @Accessors(fluent = true)
    private final EffectsGUI effectsGUI;
    @Accessors(fluent = true)
    private final ColorsGUI colorsGUI;
    @Accessors(fluent = true)
    private final MixerGUI mixerGUI;
    @Accessors(fluent = true)
    private final AdminGUI adminGUI;

    private int taskId = -5, interval = 2;
    private float colorCycleSpeed = 0.85f;
    @Accessors(fluent = true)
    private boolean showSupportedEvents = true;

    public GUIManager() {
        this.generator = new ConfigurationGenerator("gui.yml");
        this.effectsGUI = new EffectsGUI(this);
        this.colorsGUI = new ColorsGUI(this);
        this.mixerGUI = new MixerGUI(this);
        this.adminGUI = new AdminGUI(this);
        TickHandlersGUI.configure(this);

        Bukkit.getPluginManager().registerEvents(new GUIListener(), Particles.getPlugin());
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public boolean initialize() {
        this.config = getConfiguration();

        showSupportedEvents = config.getBoolean("show-supported-events", showSupportedEvents);

        interval = config.getInt("animation.interval", interval);
        colorCycleSpeed = (float) config.getDouble("animation.color-cycle-speed", colorCycleSpeed);
        if (taskId != -5 && !config.getBoolean("animation.enabled", true)) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -5;
        } else if (taskId == -5 && config.getBoolean("animation.enabled", true)) {
            taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(Particles.getPlugin(), new GUITask(), 0, interval).getTaskId();
        }

        style = getCurrentStyle();
        if (style == null) {
            ComponentLogger.error(generator, "Couldn't set GUI style");
            return false;
        }

        elements.initialize(this);
        sounds.initialize(this);

        effectsGUI.reload();
        colorsGUI.reload();
        mixerGUI.reload();
        adminGUI.reload();
        TickHandlersGUI.setItems();
        return true;
    }

    @Override
    public void reload() {
        initialize();
    }

    public GUIStyle getCurrentStyle() {
        var styleId = config.getString("current-style");
        if (styleId == null) return null;
        return new GUIStyle(
                styleId,
                Stream.of(GUIType.values())
                        .collect(Collectors.toMap(
                                gui -> gui,
                                gui -> new GUILayout(
                                        styleId + "/" + gui.id(),
                                        config.getStringList("styles." + styleId + "." + gui.id() + ".layout").toArray(String[]::new)
                                )
                        ))
        );
    }
}
