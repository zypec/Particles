package dev.zypec.particles;

import dev.zypec.particles.color.ColorManager;
import dev.zypec.particles.command.MainCommand;
import dev.zypec.particles.database.DatabaseManager;
import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.EffectManager;
import dev.zypec.particles.effect.data.LocationEffectData;
import dev.zypec.particles.integration.Expansions;
import dev.zypec.particles.locale.Translations;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.DrilldownPie;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.Map;

@Getter
public class Plugin extends AbstractPlugin {

    @Override
    public void onEnable() {
        Particles.setPlugin(this);

        // Commands & Listeners
        initializeCommands();

        // bStats
        initializeMetrics();

        // PlaceholderAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Expansions().register();
    }

    private void initializeCommands() {
        // Main command with completions
        var commandManager = Particles.getCommandManager();
        var aliases = getConfig().getString("command-aliases");
        commandManager.getCommandReplacements().addReplacement("aliases", "prtc|particles" + (aliases == null ? "" : "|" + aliases));
        commandManager.registerCommand(new MainCommand(this));
        var completions = commandManager.getCommandCompletions();
        completions.registerAsyncCompletion("effects", context -> Particles.getEffectManager().getEffects().stream().map(Effect::getKey).toList());
        completions.registerAsyncCompletion("static_effects", context -> Particles.getEffectManager().getEffects().stream().filter(Effect::isStaticSupported).map(Effect::getKey).toList());
        completions.registerAsyncCompletion("group_colors", context -> {
            var key = context.getContextValue(String.class, 1);
            var effect = Particles.getEffectManager().get(key);
            return effect == null || effect.getColorGroup() == null ? Collections.emptyList() : effect.getColorGroup().getAvailableOptions().stream().map(option -> option.colorScheme().getKey()).toList();
        });
        completions.registerAsyncCompletion("static_ids", context -> Particles.getEffectManager().getData().entrySet().stream().filter(entry -> entry.getValue() instanceof LocationEffectData).map(Map.Entry::getKey).toList());
        commandManager.usePerIssuerLocale(false, false);
    }

    private void initializeMetrics() {
        var metrics = new Metrics(this, 18854);
        metrics.addCustomChart(new SimplePie("locale", () -> Translations.LOCALE));
        metrics.addCustomChart(new SimplePie("database_type", () -> DatabaseManager.TYPE));

        metrics.addCustomChart(new DrilldownPie("effects_size", () -> Map.of(String.valueOf(Particles.getEffectManager().getEffects().size()), Map.of(EffectManager.VERSION, 1))));
        metrics.addCustomChart(new DrilldownPie("color_schemes_size", () -> Map.of(String.valueOf(Particles.getColorManager().getColors().size()), Map.of(ColorManager.VERSION, 1))));
        metrics.addCustomChart(new DrilldownPie("color_groups_size", () -> Map.of(String.valueOf(Particles.getColorManager().getGroups().size()), Map.of(ColorManager.VERSION, 1))));

        metrics.addCustomChart(new SimplePie("notifications_enabled", () -> String.valueOf(Particles.isNotificationsEnabled())));
        metrics.addCustomChart(new SimplePie("auto_update_enabled", () -> String.valueOf(Particles.isAutoUpdateEnabled())));

        metrics.addCustomChart(new SimplePie("gui_current_style", () -> Particles.getGUIManager().getCurrentStyle().getId()));
        metrics.addCustomChart(new SimplePie("gui_animation_enabled", () -> String.valueOf(Particles.getGUIManager().getTaskId() != -5)));
        metrics.addCustomChart(new SimplePie("gui_animation_interval", () -> String.valueOf(Particles.getGUIManager().getInterval())));
        metrics.addCustomChart(new SimplePie("gui_animation_speed", () -> String.valueOf(Particles.getGUIManager().getColorCycleSpeed())));
    }
}