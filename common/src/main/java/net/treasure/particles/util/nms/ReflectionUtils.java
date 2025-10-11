package net.treasure.particles.util.nms;

import org.bukkit.Bukkit;

public class ReflectionUtils {

    public static final int LATEST_MAJOR_VERSION = 21, LATEST_MINOR_VERSION = 10;

    public static final int MAJOR_MINECRAFT_VERSION, MINOR_MINECRAFT_VERSION;

    public static final String MINECRAFT_VERSION_CONVERTED;

    static {
        var bukkitVersion = Bukkit.getBukkitVersion();
        var dashIndex = bukkitVersion.indexOf("-");
        MINECRAFT_VERSION_CONVERTED = bukkitVersion.substring(2, dashIndex > -1 ? dashIndex : bukkitVersion.length());

        var args = MINECRAFT_VERSION_CONVERTED.split("\\.");
        MAJOR_MINECRAFT_VERSION = Integer.parseInt(args[0]);
        MINOR_MINECRAFT_VERSION = args.length == 1 ? 0 : Integer.parseInt(args[1]);
    }
}