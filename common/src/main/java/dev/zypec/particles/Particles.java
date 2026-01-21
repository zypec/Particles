package dev.zypec.particles;

import co.aikar.commands.BukkitCommandManager;
import dev.zypec.particles.color.ColorManager;
import dev.zypec.particles.configuration.ConfigurationGenerator;
import dev.zypec.particles.configuration.DataHolder;
import dev.zypec.particles.database.Database;
import dev.zypec.particles.database.DatabaseManager;
import dev.zypec.particles.effect.EffectManager;
import dev.zypec.particles.gui.GUIManager;
import dev.zypec.particles.locale.Translations;
import dev.zypec.particles.permission.Permissions;
import dev.zypec.particles.player.PlayerManager;
import dev.zypec.particles.player.listener.JoinQuitListener;
import dev.zypec.particles.util.logging.ComponentLogger;
import dev.zypec.particles.util.nms.particles.PacketHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Particles {

    public static final String VERSION = "1.5.3"; // config.yml

    @Getter
    private static AbstractPlugin plugin;
    @Getter
    private static BukkitCommandManager commandManager;
    @Getter
    @Accessors(fluent = true)
    private static BukkitAudiences adventure;

    private static List<DataHolder> dataHolders;
    @Getter
    private static Translations translations;
    @Getter
    private static EffectManager effectManager;
    @Getter
    private static ColorManager colorManager;
    @Getter
    private static Permissions permissions;
    @Getter
    private static GUIManager GUIManager;

    @Getter
    private static DatabaseManager databaseManager;
    @Getter
    private static PlayerManager playerManager;

    @Getter
    private static boolean autoUpdateEnabled = true;
    @Getter
    private static boolean notificationsEnabled;

    @Getter
    @Setter
    private static boolean isPaper;

    public static void setPlugin(AbstractPlugin abstractPlugin) {
        if (plugin != null) return;
        plugin = abstractPlugin;
        try {
            Class.forName("com.destroystokyo.paper.event.player.PlayerElytraBoostEvent");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {
        }
        initialize();
    }

    private static void initialize() {
        if (!PacketHandler.initialize()) {
            plugin.disable("Couldn't initialize Particles");
            return;
        }

        commandManager = new BukkitCommandManager(plugin);
        adventure = BukkitAudiences.create(plugin);

        // Main Config
        plugin.saveDefaultConfig();
        configure();

        dataHolders = new ArrayList<>();

        // Database
        databaseManager = new DatabaseManager();
        if (!databaseManager.initialize()) {
            plugin.disable("Couldn't connect to database");
            return;
        }

        // Initialize player manager
        playerManager = new PlayerManager();

        // Translations
        translations = new Translations();
        translations.initialize();
        dataHolders.add(translations);

        // Colors
        colorManager = new ColorManager();
        if (!colorManager.initialize()) {
            plugin.disable("Couldn't initialize Particles");
            return;
        }
        dataHolders.add(colorManager);

        // Effects
        effectManager = new EffectManager();
        if (!effectManager.initialize()) {
            plugin.disable("Couldn't initialize Particles");
            return;
        }
        dataHolders.add(effectManager);

        // Permissions
        permissions = new Permissions();
        permissions.initialize();
        dataHolders.add(permissions);

        // GUI Manager
        GUIManager = new GUIManager();
        dataHolders.add(GUIManager);

        // Load translations > GUI > colors > effects > players
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            translations.loadTranslations();
            GUIManager.initialize();

            colorManager.loadColors();
            effectManager.loadEffects();

            for (var player : Bukkit.getOnlinePlayers())
                playerManager.initializePlayer(player);
        }, 5);

        // Register listeners
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(playerManager, effectManager), plugin);
    }

    public static void reload(CommandSender sender) {
        ComponentLogger.setChatReceiver(sender);
        effectManager.cancelTask();

        plugin.getLogger().info("Reloading Particles");

        // config.yml
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        configure();
        plugin.getLogger().info("..Reloaded config");

        // Data Holders
        dataHolders.forEach(DataHolder::reload);
        plugin.getLogger().info("..Reloaded data holders");

        // Player Manager
        Bukkit.getScheduler().runTaskLater(plugin, () -> playerManager.reload(), 5);
        plugin.getLogger().info("..Reloaded player manager");

        plugin.getLogger().info("Reloaded");
        ComponentLogger.setChatReceiver(null);
    }

    private static void configure() {
        plugin.reloadConfig();

        var config = plugin.getConfig();
        if (!VERSION.equals(config.getString("version"))) {
            if (autoUpdateEnabled) {
                var generator = new ConfigurationGenerator("config.yml", plugin);
                generator.reset();
                plugin.reloadConfig();
                ComponentLogger.error("[config.yml]", "Generated new file (v" + VERSION + ")");
            } else
                ComponentLogger.error("[config.yml]", "New version available (v" + VERSION + ")");
        }

        notificationsEnabled = config.getBoolean("notifications", false);
        autoUpdateEnabled = config.getBoolean("auto-update-configurations", true);

        ComponentLogger.setColored(config.getBoolean("colored-error-logs", true));
        ComponentLogger.setChatLogsEnabled(config.getBoolean("chat-logs", true));
    }

    public static File getDataFolder() {
        return plugin.getDataFolder();
    }

    public static FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public static void saveConfig() {
        plugin.saveConfig();
    }

    public static Database getDatabase() {
        return databaseManager.instance();
    }
}