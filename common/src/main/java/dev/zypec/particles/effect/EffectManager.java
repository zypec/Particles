package dev.zypec.particles.effect;

import com.jeff_media.armorequipevent.ArmorEquipEvent;
import lombok.Getter;
import dev.zypec.particles.Particles;
import dev.zypec.particles.color.generator.Gradient;
import dev.zypec.particles.configuration.ConfigurationGenerator;
import dev.zypec.particles.configuration.DataHolder;
import dev.zypec.particles.constants.Patterns;
import dev.zypec.particles.effect.data.EffectData;
import dev.zypec.particles.effect.data.LocationEffectData;
import dev.zypec.particles.effect.exception.ReaderException;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.effect.handler.TickHandler;
import dev.zypec.particles.effect.listener.ElytraBoostListener;
import dev.zypec.particles.effect.listener.HandlerEventsListener;
import dev.zypec.particles.effect.mix.MixerOptions;
import dev.zypec.particles.effect.script.Script;
import dev.zypec.particles.effect.script.basic.BreakHandlerScript;
import dev.zypec.particles.effect.script.basic.BreakScript;
import dev.zypec.particles.effect.script.basic.EmptyScript;
import dev.zypec.particles.effect.script.basic.ReturnScript;
import dev.zypec.particles.effect.script.basic.StopScript;
import dev.zypec.particles.effect.script.basic.reader.BasicScriptReader;
import dev.zypec.particles.effect.script.command.reader.CommandScriptReader;
import dev.zypec.particles.effect.script.conditional.reader.ConditionalScriptReader;
import dev.zypec.particles.effect.script.delay.reader.DelayReader;
import dev.zypec.particles.effect.script.message.ActionBar;
import dev.zypec.particles.effect.script.message.ChatMessage;
import dev.zypec.particles.effect.script.message.reader.TitleReader;
import dev.zypec.particles.effect.script.parkour.reader.ParkourReader;
import dev.zypec.particles.effect.script.particle.reader.circle.CircleParticleReader;
import dev.zypec.particles.effect.script.particle.reader.circle.SpreadCircleParticleReader;
import dev.zypec.particles.effect.script.particle.reader.polygon.PolygonParticleReader;
import dev.zypec.particles.effect.script.particle.reader.single.SingleParticleReader;
import dev.zypec.particles.effect.script.particle.reader.sphere.SphereParticleReader;
import dev.zypec.particles.effect.script.particle.reader.spiral.FullSpiralParticleReader;
import dev.zypec.particles.effect.script.particle.reader.spiral.MultiSpiralParticleReader;
import dev.zypec.particles.effect.script.particle.reader.spiral.SpiralParticleReader;
import dev.zypec.particles.effect.script.particle.reader.target.TargetCircleParticleReader;
import dev.zypec.particles.effect.script.particle.reader.target.TargetParticleReader;
import dev.zypec.particles.effect.script.particle.reader.text.AnimatedTextParticleReader;
import dev.zypec.particles.effect.script.particle.reader.text.TextParticleReader;
import dev.zypec.particles.effect.script.preset.reader.PresetReader;
import dev.zypec.particles.effect.script.reader.DefaultReader;
import dev.zypec.particles.effect.script.sound.reader.SoundReader;
import dev.zypec.particles.effect.script.variable.reader.VariableCycleReader;
import dev.zypec.particles.effect.script.variable.reader.VariableReader;
import dev.zypec.particles.effect.script.visual.reader.LightningReader;
import dev.zypec.particles.effect.task.EffectsTask;
import dev.zypec.particles.effect.task.MovementCheck;
import dev.zypec.particles.gui.config.GUIElements;
import dev.zypec.particles.gui.type.effects.EffectsGUI;
import dev.zypec.particles.util.logging.ComponentLogger;
import dev.zypec.particles.util.message.MessageUtils;
import dev.zypec.particles.util.tuples.Pair;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class EffectManager implements DataHolder {

    public static final String VERSION = "1.0.0";

    private final ConcurrentHashMap<String, EffectData> data;

    private final ConfigurationGenerator generator;
    private final List<Effect> effects;
    private final HashMap<String, DefaultReader<?>> readers;

    private final Presets presets;
    private final StaticEffects staticEffects;

    private int effectsTaskId = -5;

    public EffectManager() {
        this.generator = new ConfigurationGenerator("effects.yml");
        this.effects = new ArrayList<>();
        this.readers = new HashMap<>();
        this.data = new ConcurrentHashMap<>();

        this.presets = new Presets();
        this.staticEffects = new StaticEffects();

        // Register listeners
        ArmorEquipEvent.registerListener(Particles.getPlugin());
        var pm = Bukkit.getPluginManager();
        pm.registerEvents(new HandlerEventsListener(Particles.getPlayerManager()), Particles.getPlugin());
        if (Particles.isPaper())
            try {
                pm.registerEvents(new ElytraBoostListener(Particles.getPlayerManager()), Particles.getPlugin());
            } catch (Exception ignored) {
            }

        // Run tasks
        runTask();
        Bukkit.getScheduler().runTaskTimerAsynchronously(Particles.getPlugin(), new MovementCheck(Particles.getPlayerManager()), 0, 5);

        // Variables
        registerReader(new VariableReader(), "variable", "var");
        registerReader(new VariableCycleReader(), "variable-cycle", "var-c");

        // Particles
        registerReader(new SingleParticleReader(), "particle", "single");
        registerReader(new TargetParticleReader(), "target-particle", "target");
        //- Circles
        registerReader(new CircleParticleReader(), "circle");
        registerReader(new SpreadCircleParticleReader(), "spread");
        registerReader(new TargetCircleParticleReader(), "target-circle");
        //- Text
        registerReader(new TextParticleReader(), "text");
        registerReader(new AnimatedTextParticleReader(), "animated-text");
        //- Misc
        registerReader(new ParkourReader(), "parkour");
        registerReader(new PolygonParticleReader(), "polygon");
        //- Spirals
        registerReader(new SpiralParticleReader(), "spiral");
        registerReader(new MultiSpiralParticleReader(), "multi-spiral");
        registerReader(new FullSpiralParticleReader(), "full-spiral");
        //- Sphere
        registerReader(new SphereParticleReader(), "sphere");

        // Command
        registerReader(new CommandScriptReader(), "execute-command", "execute");

        // Messages
        registerReader(new BasicScriptReader<>(ChatMessage::new), "chat");
        registerReader(new BasicScriptReader<>(ActionBar::new), "actionbar");
        registerReader(new TitleReader(), "title");

        // Sound
        registerReader(new SoundReader(), "play-sound", "sound");

        // Visual
        registerReader(new LightningReader(), "lightning");

        // Others
        registerReader(new PresetReader(), "preset");
        registerReader(new ConditionalScriptReader(), "conditional");
        //- Tick Handler Stuffs
        registerReader(new BasicScriptReader<>(s -> new EmptyScript()), "none");
        registerReader(new BasicScriptReader<>(s -> new ReturnScript()), "return");
        registerReader(new BasicScriptReader<>(s -> new BreakScript()), "break");
        registerReader(new BasicScriptReader<>(s -> new BreakHandlerScript()), "break-handler");
        registerReader(new BasicScriptReader<>(s -> new StopScript()), "stop");
        registerReader(new DelayReader(), "delay");
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public boolean initialize() {
        try {
            if (!presets.initialize()) return false;
            var config = generator.generate();
            if (config == null) return false;
        } catch (Exception e) {
            ComponentLogger.log("Couldn't load/create effects.yml", e);
            return false;
        }
        return true;
    }

    @Override
    public void reload() {
        data.values().removeIf(d -> d instanceof LocationEffectData);
        effects.clear();
        if (initialize()) {
            loadEffects();
            runTask();
        }
    }

    public void runTask() {
        var task = new EffectsTask(this);
        task.runTaskTimerAsynchronously(Particles.getPlugin(), 5, 1);
        effectsTaskId = task.getTaskId();
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(effectsTaskId);
    }

    public Effect get(String key) {
        return effects.stream().filter(effect -> effect.getKey().equals(key)).findFirst().orElse(null);
    }

    public boolean has(String key) {
        return effects.stream().anyMatch(effect -> effect.getKey().equals(key));
    }

    public void loadEffects() {
        var config = getConfiguration();
        presets.load();

        var section = config.getConfigurationSection("effects");
        if (section == null) {
            ComponentLogger.error(generator, "Couldn't find any effect");
            return;
        }

        var translations = Particles.getTranslations();
        var permissions = Particles.getPermissions();
        var colorManager = Particles.getColorManager();

        for (var key : section.getKeys(false)) {
            try {
                var path = section.getConfigurationSection(key);
                if (path == null) continue;

                // Display Name
                var displayName = translations.translate("effects", path.getString("display-name", key));

                // Permission
                var permission = permissions.replace(path.getString("permission"));

                // Interval
                var interval = path.getInt("interval", 1);
                if (interval < 1) {
                    ComponentLogger.error("[" + key + "]", "Interval value must be greater or equal than 1");
                    continue;
                }

                // Tick Handlers
                var onTickSection = path.getConfigurationSection("on-tick");
                if (onTickSection == null) {
                    ComponentLogger.error("[" + key + "]", "Effect must have on-tick section");
                    continue;
                }

                int tickHandlerIndex = 0;
                LinkedHashMap<String, Pair<TickHandler, List<String>>> tickHandlers = new LinkedHashMap<>();
                for (var tickHandlerKey : onTickSection.getKeys(false)) {
                    var tickHandlerSection = onTickSection.getConfigurationSection(tickHandlerKey);
                    if (tickHandlerSection == null) continue;
                    var event = tickHandlerSection.getString("event");
                    if (event == null) {
                        ComponentLogger.error("[" + key + "]", "Tick handler must have event: " + tickHandlerKey);
                        continue;
                    }

                    // Mixer Options
                    var mixerOptions = new MixerOptions();
                    if (tickHandlerSection.contains("mixer-options")) {
                        mixerOptions.lockEvent = tickHandlerSection.getBoolean("mixer-options.lock-event", mixerOptions.lockEvent);
                        mixerOptions.isPrivate = tickHandlerSection.getBoolean("mixer-options.private", mixerOptions.isPrivate);
                        mixerOptions.depends = tickHandlerSection.getStringList("mixer-options.depend");

                        if (mixerOptions.depends.stream().anyMatch(id -> !onTickSection.contains(id))) {
                            ComponentLogger.error("[" + key + "]", "Mixer options have unknown depend tick handlers: " + tickHandlerKey);
                            continue;
                        }
                    }

                    try {
                        var tickHandlerInterval = tickHandlerSection.getInt("interval", interval);
                        if (tickHandlerInterval < interval) {
                            ComponentLogger.error("[" + key + "]", "Tick handler's interval cannot be lower than the effect's interval: " + tickHandlerKey);
                            continue;
                        }

                        if (!mixerOptions.isPrivate)
                            tickHandlerIndex++;
                        tickHandlers.put(tickHandlerKey, new Pair<>(
                                new TickHandler(
                                        tickHandlerKey,
                                        tickHandlerSection.getString("display-name", "[" + tickHandlerIndex + "]"),
                                        tickHandlerInterval,
                                        tickHandlerSection.getInt("times", 1),
                                        mixerOptions,
                                        tickHandlerSection.getInt("max-executed", 0),
                                        tickHandlerSection.getBoolean("reset-event", true),
                                        event.equalsIgnoreCase("none") ? null : HandlerEvent.valueOf(event.toUpperCase(Locale.ENGLISH))
                                ),
                                tickHandlerSection.getStringList("scripts")
                        ));
                    } catch (IllegalArgumentException e) {
                        ComponentLogger.error("[" + key + "]", "Unknown event type for '" + tickHandlerKey + "' tick handler: " + event);
                    } catch (Exception e) {
                        ComponentLogger.error("[" + key + "]", "Couldn't read tick handler options: " + tickHandlerKey);
                    }
                }

                // Icon
                var icon = GUIElements.getItemStack(config, "effects." + key + ".icon", EffectsGUI.DEFAULT_ICON.item());

                // Description
                List<String> description = null;
                if (path.contains("description")) {
                    description = new ArrayList<>();
                    for (var s : path.getStringList("description")) {
                        var translated = MessageUtils.gui(translations.translate("descriptions", s));
                        description.addAll(List.of(translated.split("%nl%")));
                    }
                }

                // Color animation warning
                var colorAnimation = path.getString("color-animation");
                if (colorAnimation != null && colorManager.getColorScheme(colorAnimation) == null) {
                    try {
                        Gradient.hex2Rgb(colorAnimation);
                    } catch (Exception ignored) {
                        ComponentLogger.error("[" + key + "]", "Unknown color animation value: " + colorAnimation);
                    }
                }

                var effect = new Effect(
                        key,
                        displayName,
                        description != null ? description.toArray(String[]::new) : null,
                        icon,
                        colorAnimation,
                        permission,
                        path.getBoolean("name-color-animation", false),
                        path.getStringList("variables"),
                        interval,
                        path.getBoolean("enable-caching", false),
                        tickHandlers,
                        colorManager.getColorGroup(path.getString("color-group")),
                        path.getBoolean("only-elytra", false)
                );
                effect.configure();

                effects.add(effect);
            } catch (Exception e) {
                ComponentLogger.log("Couldn't load effect: " + key, e);
            }
        }

        staticEffects.load(this);
    }

    public void registerReader(DefaultReader<?> reader, String... aliases) {
        for (var alias : aliases)
            this.readers.put(alias, reader);
    }

    public <S extends Script> S read(Effect effect, String type, String line) throws ReaderException {
        if (!readers.containsKey(type))
            throw new ReaderException("Invalid script type: " + type);
        // noinspection unchecked
        return (S) readers.get(type).read(effect, type, line);
    }

    public Script readLine(Effect effect, String line) throws ReaderException {
        int interval = -1;
        int intervalIndex = line.lastIndexOf("~");
        if (intervalIndex != -1) {
            var args = Patterns.TILDE.split(line, 2);
            try {
                interval = Integer.parseInt(args[1]);
            } catch (Exception ignored) {
                ComponentLogger.error(effect, "", line, intervalIndex, line.length(), "Invalid interval syntax");
            }
            line = args[0];
        }

        var args = Patterns.SPACE.split(line.trim(), 2);
        String type;
        try {
            type = args[0];
        } catch (Exception e) {
            return null;
        }

        var script = read(effect, type, args.length == 1 ? null : args[1]);

        if (script != null) {
            script.setEffect(effect);
            if (interval > 0)
                script.setInterval(interval);
        }
        return script;
    }
}