package dev.zypec.particles.command;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
public class UpdateChecker {

    public static void getVersion(JavaPlugin plugin, int resourceId, Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (var stream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId + "/~").openStream(); var scanner = new Scanner(stream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                } else {
                    plugin.getLogger().info("Unable to get latest version number from SpigotMC.");
                }
            } catch (IOException e) {
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }
}